package google.hashcode.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CacheServer {
   private final int id;
   private final int capacity;

   private final List<Video> videosThatCanBeRequestedByEndpoint = new ArrayList<>();

   public CacheServer(int id, int capacity) {
      this.id = id;
      this.capacity = capacity;
   }

   public int getId() {
      return id;
   }

   public int getCapacity() {
      return capacity;
   }

   public List<Video> getVideosThatCanBeRequestedByEndpoint() {
      return Collections.unmodifiableList(videosThatCanBeRequestedByEndpoint);
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
