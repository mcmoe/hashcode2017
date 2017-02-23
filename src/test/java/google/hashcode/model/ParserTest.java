package google.hashcode.model;

import google.hashcode.Main;
import google.hashcode.Solver;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * 5 videos, 2 endpoints, 4 request descriptions, 3 caches 100MB each.
 * Videos 0, 1, 2, 3, 4 have sizes 50MB, 50MB, 80MB, 30MB, 110MB.
 * Endpoint 0 has 1000ms datacenter latency and is connected to 3 caches:
 * The latency (of endpoint 0) to cache 0 is 100ms.
 * The latency (of endpoint 0) to cache 2 is 200ms.
 * The latency (of endpoint 0) to cache 1 is 200ms.
 * Endpoint 1 has 500ms datacenter latency and is not connected to a cache.
 * 1500 requests for video 3 coming from endpoint 0.
 * 1000 requests for video 0 coming from endpoint 1.
 * 500 requests for video 4 coming from endpoint 0.
 * 1000 requests for video 1 coming from endpoint 0.
 *
 * Created by mkobeissi on 23/02/2017.
 */
public class ParserTest {

    @Test
    public void solve() throws IOException, URISyntaxException {
        Infra infra = Main.parseIn("example.in");
        int costVideosForCache = Solver.findCostVideosForCache(infra);
        System.out.println("cost reduction: " + costVideosForCache);
    }

    @Test
    public void ensure_parser_matches_problem_example() throws IOException, URISyntaxException {
        Infra infra = Main.parseIn("example.in");
        assertThat(infra.getNumberOfVideos()).isEqualTo(5);
        assertThat(infra.getNumberOfEndpoints()).isEqualTo(2);
        assertThat(infra.getNumberOfRequests()).isEqualTo(4);
        assertThat(infra.getNumberOfCacheServers()).isEqualTo(3);
        assertThat(infra.cacheServers.size()).isEqualTo(3);
        infra.getCacheServers().forEach((integer, cacheServer) -> assertThat(cacheServer.getCapacity()).isEqualTo(100));

        assertThat(infra.getVideos().get(0).getSizeInMB()).isEqualTo(50);
        assertThat(infra.getVideos().get(1).getSizeInMB()).isEqualTo(50);
        assertThat(infra.getVideos().get(2).getSizeInMB()).isEqualTo(80);
        assertThat(infra.getVideos().get(3).getSizeInMB()).isEqualTo(30);
        assertThat(infra.getVideos().get(4).getSizeInMB()).isEqualTo(110);

        assertThat(infra.getEndPoints().get(0).getLatencyToDataCenter()).isEqualTo(1000);
        assertThat(infra.getEndPoints().get(0).getNumberOfCacheServicesConectedTo()).isEqualTo(3);
        assertThat(infra.getEndPoints().get(0).latencyToCacheServers.get(infra.getCacheServers().get(0))).isEqualTo(100);
        assertThat(infra.getEndPoints().get(0).latencyToCacheServers.get(infra.getCacheServers().get(1))).isEqualTo(300);
        assertThat(infra.getEndPoints().get(0).latencyToCacheServers.get(infra.getCacheServers().get(2))).isEqualTo(200);

        assertThat(infra.getEndPoints().get(1).getLatencyToDataCenter()).isEqualTo(500);
        assertThat(infra.getEndPoints().get(1).getNumberOfCacheServicesConectedTo()).isEqualTo(0);


        Request requestVideo3 = infra.getRequests().stream().filter(request -> request.getVideoId() == 3).findFirst().get();
        assertThat(requestVideo3.endpointId).isEqualTo(0);
        assertThat(requestVideo3.numberOfRequests).isEqualTo(1500);

        Request requestVideo0 = infra.getRequests().stream().filter(request -> request.getVideoId() == 0).findFirst().get();
        assertThat(requestVideo0.endpointId).isEqualTo(1);
        assertThat(requestVideo0.numberOfRequests).isEqualTo(1000);
        Request requestVideo4 = infra.getRequests().stream().filter(request -> request.getVideoId() == 4).findFirst().get();
        assertThat(requestVideo4.endpointId).isEqualTo(0);
        assertThat(requestVideo4.numberOfRequests).isEqualTo(500);
        Request requestVideo1 = infra.getRequests().stream().filter(request -> request.getVideoId() == 1).findFirst().get();
        assertThat(requestVideo1.endpointId).isEqualTo(0);
        assertThat(requestVideo1.numberOfRequests).isEqualTo(1000);
    }

}
