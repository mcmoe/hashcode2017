package google.hashcode.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CacheServer {
   final int id;

   final List<Video> videosThatCanBeRequestedByEndpoint = new ArrayList<>();


   public CacheServer(int id) {
      this.id = id;
   }

   public int getId() {
      return id;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      CacheServer that = (CacheServer) o;
      return id == that.id;
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   public void addVideo(Video v) {
      videosThatCanBeRequestedByEndpoint.add(v);
   }
}