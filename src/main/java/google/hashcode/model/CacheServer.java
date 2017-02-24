package google.hashcode.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CacheServer {
   private final int id;
   private final int capacity;
   private int freeSpace;

   private final Map<Integer, Video> videos = new HashMap<>();

   public CacheServer(int id, int capacity) {
      this.id = id;
      this.capacity = capacity;
      this.freeSpace = capacity;
   }

   public int getId() {
      return id;
   }

   public int getCapacity() {
      return capacity;
   }

   public int getFreeSpace() {
      return freeSpace;
   }

   public boolean hasVideos() {
      return !videos.isEmpty();
   }

   public boolean hasVideo(Video v) {
      return videos.containsKey(v.getId());
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
      if(!videos.containsKey(v.getId()) && freeSpace >= v.getSizeInMB()) {
         videos.putIfAbsent(v.getId(), v);
         freeSpace -= v.getSizeInMB();
      }
   }

   public Collection<Video> getCachedVideos() {
      return Collections.unmodifiableCollection(videos.values());
   }
}
