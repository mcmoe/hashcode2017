package google.hashcode;

import google.hashcode.model.CacheServer;
import google.hashcode.model.Infra;

public class Solver {
   static void findCostVideosForCache(Infra infra) {

      infra.getRequests().parallelStream().forEach(r -> {
         r.getEndpointId();
      });
   }
}
