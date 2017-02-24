package google.hashcode;

import google.hashcode.model.CacheServer;
import google.hashcode.model.EndPoint;
import google.hashcode.model.Infra;
import google.hashcode.model.Request;
import google.hashcode.model.Video;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mkobeissi on 23/02/2017.
 */
public class SolverPlusPlus {


    public static int solve(Infra infra) {

        List<EndPoint> endPointList = infra.getEndPoints().values().stream().collect(Collectors.toList());
        List<EndPoint> sortedEndPoints = endPointList.stream().sorted((o1, o2) -> compareEndpointLatency(o1, o2, infra)).collect(Collectors.toList());

        sortedEndPoints.forEach(endPoint -> {
            List<Request> requests = infra.getRequests().stream()
                    .filter(request -> request.getEndpointId() == endPoint.getId()).collect(Collectors.toList());

            List<Request> sortedRequests = requests.stream().sorted((o1, o2) -> Integer.compare(getRequestPriority(infra, o2), getRequestPriority(infra, o1)))
                    .collect(Collectors.toList());

            List<Map.Entry<CacheServer, Integer>> connectedCaches = endPoint.getLatencyToCacheServers().entrySet().stream().collect(Collectors.toList());
            List<Map.Entry<CacheServer, Integer>> sortedCaches = connectedCaches.stream().sorted((o1, o2) -> Integer.compare(o1.getValue(), o2.getValue())).collect(Collectors.toList());

            sortedRequests.forEach(request -> {
                Video video = infra.getVideos().get(request.getVideoId());
                sortedCaches.stream().filter(cacheServerIntegerEntry -> canHoldVideo(cacheServerIntegerEntry.getKey(), video))
                .findFirst().ifPresent(cacheServerIntegerEntry -> {
                    CacheServer cacheServer = cacheServerIntegerEntry.getKey();
                    cacheServer.addVideo(video);
                    request.setCacheServer(cacheServer);
                });
            });
        });


        //printActiveCaches(infra);
        double averageSavings = getAverageSavings(infra);
        return (int) (averageSavings * 1000);
    }

    private static int compareEndpointLatency(EndPoint o1, EndPoint o2, Infra infra) {
        Integer o2MinCache = o2.getLatencyToCacheServers().values().stream().min(Comparator.naturalOrder()).orElse(o2.getLatencyToDataCenter());
        Integer o1MinCache = o1.getLatencyToCacheServers().values().stream().min(Comparator.naturalOrder()).orElse(o1.getLatencyToDataCenter());

        //int o2Requests = infra.getRequests().stream().filter(request -> request.getEndpointId() == o2.getId()).mapToInt(Request::getNumberOfRequests).sum();
        //int o1Requests = infra.getRequests().stream().filter(request -> request.getEndpointId() == o1.getId()).mapToInt(Request::getNumberOfRequests).sum();

        return Integer.compare((o2.getLatencyToDataCenter() - o2MinCache) /* * o2Requests */, (o1.getLatencyToDataCenter() - o1MinCache) /* * o1Requests */);
    }

    public static void printActiveCaches(Infra infra) {
        int activeCaches = infra.getCacheServers().values().stream().mapToInt(c -> c.hasVideos() ? 1 : 0).sum();
        System.out.println("----------------");
        System.out.println(activeCaches);
        infra.getCacheServers().forEach((integer, cacheServer) -> System.out.println(cacheServer.getId() + " "
                + cacheServer.getCachedVideos().stream().map(video -> video.getId() + "").collect(Collectors.joining(" "))));
        System.out.println("----------------");
    }

    private static double getAverageSavings(Infra infra) {
        int totalSavings = infra.getRequests().stream().mapToInt(request -> {
            EndPoint endPoint = infra.getEndPoints().get(request.getEndpointId());
            int latencyToDataCenter = endPoint.getLatencyToDataCenter();
            Integer latencyToCacheServer = latencyToDataCenter;
            if(request.getCacheServer() != null) {
                latencyToCacheServer = endPoint.getLatencyToCacheServers().get(request.getCacheServer());
            }
            return (latencyToDataCenter - latencyToCacheServer) * request.getNumberOfRequests();
        }).sum();

        int totalRequests = infra.getRequests().stream().mapToInt(Request::getNumberOfRequests).sum();

        return (double) totalSavings / (double) totalRequests;
    }

    private static boolean canHoldVideo(CacheServer cacheServer, Video video) {
        return cacheServer.getFreeSpace() >= video.getSizeInMB();
    }

    private static int getRequestPriority(Infra infra, Request request) {
        return request.getNumberOfRequests() / getVideoSize(infra, request);
    }

    private static int getVideoSize(Infra infra, Request request) {
        return infra.getVideos().get(request.getVideoId()).getSizeInMB();
    }
}
