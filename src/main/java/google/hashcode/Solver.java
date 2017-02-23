package google.hashcode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import google.hashcode.model.CacheServer;
import google.hashcode.model.EndPoint;
import google.hashcode.model.Infra;
import google.hashcode.model.Request;

public class Solver {
   public static int findCostVideosForCache(Infra infra) {
      Map<CacheServer, Integer> remaningCapacity = new HashMap<>();
      infra.getCacheServers().values().forEach(c -> {
         remaningCapacity.put(c, c.getCapacity());
      });
      int result = infra.getVideos().values().stream().mapToInt(v -> {
         Gain gain = infra.getCacheServers().values().stream().map(cache -> {
            int totalGain = 0;
            if(remaningCapacity.get(cache) >= v.getSizeInMB()) {
               totalGain = infra.getRequests().stream().mapToInt(r -> {
                  final EndPoint endPoint = infra.getEndPoints().get(r.getEndpointId());
                  if(endPoint != null) {
                     final Integer latency = endPoint.getLatencyToCacheServers().get(cache);
                     if(latency != null && r.getVideoId() == v.getId()) {
                        return endPoint.getLatencyToDataCenter() - latency;
                     }
                  }

                  return 0;
               }).sum();
            }
            return new Gain(cache, totalGain);
         }).sorted(Comparator.<Gain>naturalOrder().reversed()).findFirst().get();
         remaningCapacity.put(gain.cacheServer, remaningCapacity.get(gain.cacheServer) - v.getSizeInMB());
         return gain.gain;
      }).sum();

      int totalRequests = infra.getRequests().stream().mapToInt(Request::getNumberOfRequests).sum();
      int finalResult = (result * 1000) / totalRequests;
      return finalResult;
   }

   static class Gain implements Comparable<Gain> {
      CacheServer cacheServer;
      int gain;

      public Gain(CacheServer cacheServer, int gain) {
         this.cacheServer = cacheServer;
         this.gain = gain;
      }

      @Override
      public int compareTo(Gain o) {
         return Integer.compare(this.gain, o.gain);
      }
   }
}
