package google.hashcode.model;

import java.util.HashMap;
import java.util.Map;

public class EndPoint {
   int latencyToDataCenter;
   int numberOfCacheServicesConectedTo;
   Map<CacheServer, Integer> latencyToCacheServers = new HashMap<>();

   public EndPoint() {
   }

   public EndPoint(int latencyToDataCenter, int numberOfCacheServicesConectedTo) {
      this.latencyToDataCenter = latencyToDataCenter;
      this.numberOfCacheServicesConectedTo = numberOfCacheServicesConectedTo;
   }

   public int getLatencyToDataCenter() {
      return latencyToDataCenter;
   }

   public void setLatencyToDataCenter(int latencyToDataCenter) {
      this.latencyToDataCenter = latencyToDataCenter;
   }

   public int getNumberOfCacheServicesConectedTo() {
      return numberOfCacheServicesConectedTo;
   }

   public void setNumberOfCacheServicesConectedTo(int numberOfCacheServicesConectedTo) {
      this.numberOfCacheServicesConectedTo = numberOfCacheServicesConectedTo;
   }

   public void addCacheServer(CacheServer cacheServer, int latency) {
      this.latencyToCacheServers.putIfAbsent(cacheServer, latency);
   }
}
