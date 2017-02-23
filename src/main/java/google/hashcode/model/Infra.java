package google.hashcode.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Infra {
   final int numberOfVideos;
   final int numberOfEndpoints;
   final int numberOfRequests;
   final int numberOfCacheServers;
   final int capacityOfCacheServersInMB;

   final List<Request> requests = new ArrayList<>();
   final List<Video> videos = new ArrayList<>();
   final List<EndPoint> endPoints = new ArrayList<>();
   final Set<CacheServer> cacheServers = new HashSet<>();

   public Infra(int numberOfVideos, int numberOfEndpoints, int numberOfRequests, int numberOfCacheServers, int capacityOfCacheServersInMB) {
      this.numberOfVideos = numberOfVideos;
      this.numberOfEndpoints = numberOfEndpoints;
      this.numberOfRequests = numberOfRequests;
      this.numberOfCacheServers = numberOfCacheServers;
      this.capacityOfCacheServersInMB = capacityOfCacheServersInMB;
   }

   public int getNumberOfVideos() {
      return numberOfVideos;
   }

   public int getNumberOfEndpoints() {
      return numberOfEndpoints;
   }

   public int getNumberOfRequests() {
      return numberOfRequests;
   }

   public int getNumberOfCacheServers() {
      return numberOfCacheServers;
   }

   public int getCapacityOfCacheServersInMB() {
      return capacityOfCacheServersInMB;
   }

   public Request addRequest(Request r) {
      requests.add(r);
      return r;
   }

   public Video addVideo(Video v) {
      videos.add(v);
      return v;
   }

   public EndPoint addEndPoint(EndPoint endPoint) {
      endPoints.add(endPoint);
      return endPoint;
   }

   public CacheServer addCacheServer(CacheServer cacheServer) {
      cacheServers.add(cacheServer);
      return cacheServer;
   }
}
