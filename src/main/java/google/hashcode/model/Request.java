package google.hashcode.model;

public class Request {
   final int videoId;
   final int endpointId;
   final int numberOfRequests;

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
}
