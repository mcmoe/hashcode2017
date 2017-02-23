package google.hascode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

   public static void main(String[] args) throws IOException, URISyntaxException {
      Files.lines(Paths.get(Main.class.getResource("kittens.in").toURI()));
      System.out.println();
   }
}
