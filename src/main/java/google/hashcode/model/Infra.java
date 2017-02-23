package google.hashcode.model;

import java.util.ArrayList;
import java.util.List;

public class Infra {
   int numberOfVideos;
   int numberOfEndpoints;
   int numberOfRequests;
   int numberOfCacheServers;
   int capacityOfCacheServersInMB;

   final List<Request> requests = new ArrayList<>();

   public int getNumberOfVideos() {
      return numberOfVideos;
   }

   public void setNumberOfVideos(int numberOfVideos) {
      this.numberOfVideos = numberOfVideos;
   }

   public int getNumberOfEndpoints() {
      return numberOfEndpoints;
   }

   public void setNumberOfEndpoints(int numberOfEndpoints) {
      this.numberOfEndpoints = numberOfEndpoints;
   }

   public int getNumberOfRequests() {
      return numberOfRequests;
   }

   public void setNumberOfRequests(int numberOfRequests) {
      this.numberOfRequests = numberOfRequests;
   }

   public int getNumberOfCacheServers() {
      return numberOfCacheServers;
   }

   public void setNumberOfCacheServers(int numberOfCacheServers) {
      this.numberOfCacheServers = numberOfCacheServers;
   }

   public int getCapacityOfCacheServersInMB() {
      return capacityOfCacheServersInMB;
   }

   public void setCapacityOfCacheServersInMB(int capacityOfCacheServersInMB) {
      this.capacityOfCacheServersInMB = capacityOfCacheServersInMB;
   }

   public Request addRequest(Request r) {
      requests.add(r);
      return r;
   }
}
