package google.hashcode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import google.hashcode.model.CacheServer;
import google.hashcode.model.EndPoint;
import google.hashcode.model.Infra;
import google.hashcode.model.Request;
import google.hashcode.model.Video;

public class Main {

   public static void main(String[] args) throws IOException, URISyntaxException {
      Infra infra = parseIn("kittens.in");
      // TODO
   }

   public static Infra parseIn(String inputFile) throws IOException, URISyntaxException {
      final List<String> lines = Files.readAllLines(Paths.get(Main.class.getResource(inputFile).toURI()));
      String infraAsString = lines.get(0);
      final Matcher matcher = Pattern.compile("(\\d+){1} (\\d+){1} (\\d+){1} (\\d+){1} (\\d+){1}").matcher(infraAsString);

      /* Parse first line. Basic infra data */
      matcher.find();
      Infra infra = new Infra(Integer.valueOf(matcher.group(1)),
        Integer.valueOf(matcher.group(2)),
        Integer.valueOf(matcher.group(3)),
        Integer.valueOf(matcher.group(4)),
        Integer.valueOf(matcher.group(5)));

      /* Parse second line. Add all videos */
      String[] videosSize = lines.get(1).split(" ");
      if(videosSize.length != infra.getNumberOfVideos()) {
         throw new IllegalArgumentException();
      }
      IntStream.range(0, videosSize.length)
        .mapToObj(i -> new Video(i, Integer.valueOf(videosSize[i])))
        .forEach(infra::addVideo);


      Map<Integer, CacheServer> cacheServerMap = new HashMap<>();

      for(int i = 2 ; i < lines.size();) {
         List<Integer> l = parseLine(lines.get(i));

         if(l.size() == 2) {
            /* Add the endpoints */
            EndPoint endPoint = new EndPoint(i - 2, l.get(0), l.get(1));
            ++i;
            IntStream.range(i, i + endPoint.getNumberOfCacheServicesConectedTo()).forEach(j -> {
               List<Integer> cacheServerInfo = parseLine(lines.get(j));
               final CacheServer cacheServer = new CacheServer(cacheServerInfo.get(0), infra.getCapacityOfCacheServersInMB());
               infra.addCacheServer(cacheServer);
               endPoint.addCacheServer(cacheServer, cacheServerInfo.get(1));
            });
            infra.addEndPoint(endPoint);
            i += endPoint.getNumberOfCacheServicesConectedTo();
         } else {
            /* Add the requests */
            List<Integer> requestInfo = parseLine(lines.get(i));
            Request request = new Request(requestInfo.get(0), requestInfo.get(1), requestInfo.get(2));
            infra.addRequest(request);
            ++i;
         }
      }

      return infra;
   }


   private static List<Integer> parseLine(String s) {
      return Arrays.stream(s.split(" ")).map(Integer::valueOf).collect(Collectors.toList());
   }
}
