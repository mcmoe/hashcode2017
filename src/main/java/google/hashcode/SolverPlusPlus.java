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
 * Figures out which videos to put in which caches in order to decrease overall latency of the system
 * Finally calculates the score based on the average latency reduction per request
 * Created by mkobeissi on 23/02/2017.
 */
public class SolverPlusPlus {


    /**
     * The idea is to sort the endpoints in order to start with the endpoint that will potentially provide
     * the highest latency reduction so as to  fill the caches with those requests first.
     * This way if there are endpoints that don't end up with cached videos they should be of least impact
     * to final score.
     * @param infra the model parsed from the input files containing all the info about the data set
     * @return the final score of the data set SUM((data center latency - new latency) * # requests) / SUM(# requests) * 1000
     */
    public static int solve(Infra infra) {

        List<EndPoint> sortedEndPoints = getSortedEndPoints(infra);
        sortedEndPoints.forEach(endPoint -> {
            List<Request> sortedRequests = getSortedRequests(infra, endPoint);
            List<CacheServer> sortedCaches = getSortedCaches(endPoint);

            sortedRequests.forEach(request -> {
                Video video = infra.getVideos().get(request.getVideoId());
                sortedCaches.stream().filter(cacheServer -> canHoldOrContainsVideo(cacheServer, video))
                .findFirst().ifPresent(cacheServer -> {
                    cacheServer.addVideo(video);
                    request.setCacheServer(cacheServer);
                });
            });
        });

        //printActiveCaches(infra);
        return generateSavings(infra);
    }

    private static int generateSavings(Infra infra) {
        double averageSavings = getAverageSavings(infra);
        return (int) (averageSavings * 1000);
    }

    private static List<CacheServer> getSortedCaches(EndPoint endPoint) {
        List<Map.Entry<CacheServer, Integer>> connectedViableCaches = endPoint.getLatencyToCacheServers().entrySet().stream()
                .filter(c -> c.getValue() < endPoint.getLatencyToDataCenter()).collect(Collectors.toList());

        return connectedViableCaches.stream().sorted((o1, o2) -> Integer.compare(o1.getValue(), o2.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private static List<Request> getSortedRequests(Infra infra, EndPoint endPoint) {
        List<Request> requests = infra.getRequests().stream()
                .filter(request -> request.getEndpointId() == endPoint.getId()).collect(Collectors.toList());
        return requests.stream().sorted((o1, o2) -> Integer.compare(getRequestPriority(infra, o2), getRequestPriority(infra, o1)))
                .collect(Collectors.toList());
    }

    private static List<EndPoint> getSortedEndPoints(Infra infra) {
        List<EndPoint> endPointList = infra.getEndPoints().values().stream().collect(Collectors.toList());
        return endPointList.stream().sorted((o1, o2) -> compareEndpointLatency(o1, o2, infra)).collect(Collectors.toList());
    }


    /**
     * With the given data sets, taking the number of requests into account when prioritizing did not make much difference.
     * I've opted to do without them as they slow down the run by about 30 seconds.
     * The calculation is therefore based on the latency to data center minus the minimum latency to a connected cache
     * The larger this number, the more the priority is for the endpoint
     * @param o1 first end point
     * @param o2 seconf end point
     * @param infra the model - in case needed to calculate total requests per endpoint
     * @return the comparison result as the usual -1, 0, 1
     */
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
        double totalSavings = infra.getRequests().stream().mapToDouble(request -> {
            EndPoint endPoint = infra.getEndPoints().get(request.getEndpointId());
            double latencyToDataCenter = endPoint.getLatencyToDataCenter();
            double latencyToCacheServer = latencyToDataCenter;
            if(request.getCacheServer() != null) {
                latencyToCacheServer = endPoint.getLatencyToCacheServers().get(request.getCacheServer());
            }
            return (latencyToDataCenter - latencyToCacheServer) * request.getNumberOfRequests();
        }).sum();

        double totalRequests = infra.getRequests().stream().mapToInt(Request::getNumberOfRequests).sum();

        return totalSavings / totalRequests;
    }

    private static boolean canHoldOrContainsVideo(CacheServer cacheServer, Video video) {
        return cacheServer.getFreeSpace() >= video.getSizeInMB() || cacheServer.hasVideo(video);
    }

    private static int getRequestPriority(Infra infra, Request request) {
        return request.getNumberOfRequests() / getVideoSize(infra, request);
    }

    private static int getVideoSize(Infra infra, Request request) {
        return infra.getVideos().get(request.getVideoId()).getSizeInMB();
    }
}
