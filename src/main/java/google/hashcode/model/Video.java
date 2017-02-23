package google.hashcode.model;

import java.util.Objects;

public class Video {
   private final int id;
   private final int sizeInMB;

   public Video(int id, int sizeInMB) {
      this.id = id;
      this.sizeInMB = sizeInMB;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      Video video = (Video) o;
      return id == video.id;
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   public int getId() {
      return id;
   }

   public int getSizeInMB() {
      return sizeInMB;
   }
}
