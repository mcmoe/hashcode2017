package google.hashcode.model;

import java.util.Objects;

public class Request {
   final int videoId;
   final int endpointId;
   final int numberOfRequests;
   private CacheServer cacheServer;

   public Request(int videoId, int endpointId, int numberOfRequests) {
      this.videoId = videoId;
      this.endpointId = endpointId;
      this.numberOfRequests = numberOfRequests;
   }

   public int getVideoId() {
      return videoId;
   }

   public int getEndpointId() {
      return endpointId;
   }

   public int getNumberOfRequests() {
      return numberOfRequests;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      Request request = (Request) o;
      return videoId == request.videoId &&
        endpointId == request.endpointId;
   }

   @Override
   public int hashCode() {
      return Objects.hash(videoId, endpointId);
   }

   public void setCacheServer(CacheServer cacheServer) {
      this.cacheServer = cacheServer;
   }

   public CacheServer getCacheServer() {
      return cacheServer;
   }
}
