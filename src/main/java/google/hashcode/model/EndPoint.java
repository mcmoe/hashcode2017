package google.hashcode.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EndPoint {
   final int id;
   final int latencyToDataCenter;
   final int numberOfCacheServicesConectedTo;
   Map<CacheServer, Integer> latencyToCacheServers = new HashMap<>();


   public EndPoint(int id, int latencyToDataCenter, int numberOfCacheServicesConectedTo) {
      this.latencyToDataCenter = latencyToDataCenter;
      this.numberOfCacheServicesConectedTo = numberOfCacheServicesConectedTo;
      this.id = id;
   }

   public int getLatencyToDataCenter() {
      return latencyToDataCenter;
   }


   public int getNumberOfCacheServicesConectedTo() {
      return numberOfCacheServicesConectedTo;
   }

   public int getId() {
      return id;
   }

   public void addCacheServer(CacheServer cacheServer, int latency) {
      this.latencyToCacheServers.putIfAbsent(cacheServer, latency);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      EndPoint endPoint = (EndPoint) o;
      return id == endPoint.id;
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }
}
