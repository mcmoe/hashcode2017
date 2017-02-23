package google.hashcode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
      Infra infra = new Infra();
      final List<String> lines = Files.readAllLines(Paths.get(Main.class.getResource("kittens.in").toURI()));
      String infraAsString = lines.get(0);
      final Matcher matcher = Pattern.compile("(\\d+){1} (\\d+){1} (\\d+){1} (\\d+){1} (\\d+){1}").matcher(infraAsString);

      /* Parse first line */
      matcher.find();
      infra.setNumberOfVideos(Integer.valueOf(matcher.group(1)));
      infra.setNumberOfEndpoints(Integer.valueOf(matcher.group(2)));
      infra.setNumberOfRequests(Integer.valueOf(matcher.group(3)));
      infra.setNumberOfCacheServers(Integer.valueOf(matcher.group(4)));
      infra.setCapacityOfCacheServersInMB(Integer.valueOf(matcher.group(5)));

      /* Parse second line */
      String[] videosSize = lines.get(1).split(" ");
      if(videosSize.length != infra.getNumberOfVideos()) {
         throw new IllegalArgumentException();
      }
      final List<Video> videos = Arrays.stream(videosSize).map(Integer::valueOf).map(Video::new).collect(Collectors.toList());

      Map<Integer, CacheServer> cacheServerMap = new HashMap<>();

      for(int i = 2; i < lines.size();) {
         List<Integer> l = parseLine(lines.get(i));
         if(l.size() == 2) {
            ++i;
            EndPoint endPoint = new EndPoint(l.get(0), l.get(1));
            IntStream.range(i, i + endPoint.getNumberOfCacheServicesConectedTo()).forEach(j -> {
               List<Integer> cacheServerInfo = parseLine(lines.get(j));
               final CacheServer cacheServer = cacheServerMap.putIfAbsent(cacheServerInfo.get(0), new CacheServer(cacheServerInfo.get(0)));
               endPoint.addCacheServer(cacheServer, cacheServerInfo.get(1));
            });
            i += endPoint.getNumberOfCacheServicesConectedTo();
         } else {
            List<Integer> requestInfo = parseLine(lines.get(i));
            Request request = new Request(requestInfo.get(0), requestInfo.get(1), requestInfo.get(2));
            infra.addRequest(request);
            ++i;
         }




      }
      System.out.println();
   }

   private static List<Integer> parseLine(String s) {
      return Arrays.stream(s.split(" ")).map(Integer::valueOf).collect(Collectors.toList());
   }
}
