package google.hashcode.model;

import google.hashcode.Main;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.fest.assertions.Assertions.assertThat;

/**
 *
 *
 * 5 videos, 2 endpoints, 4 request descriptions, 3 caches 100MB each.
 Videos 0, 1, 2, 3, 4 have sizes 50MB, 50MB, 80MB, 30MB, 110MB.
 Endpoint 0 has 1000ms datacenter latency and is connected to 3 caches:
 The latency (of endpoint 0) to cache 0 is 100ms.
 The latency (of endpoint 0) to cache 2 is 200ms.
 The latency (of endpoint 0) to cache 1 is 200ms.
 Endpoint 1 has 500ms datacenter latency and is not connected to a cache.
 1500 requests for video 3 coming from endpoint 0.
 1000 requests for video 0 coming from endpoint 1.
 500 requests for video 4 coming from endpoint 0.
 1000 requests for video 1 coming from endpoint 0.
 *
 * Created by mkobeissi on 23/02/2017.
 */
public class ParserTest {

    @Test
    public void ensure_parser_matches_problem_example() throws IOException, URISyntaxException {
        Infra infra = Main.parseIn("example.in");
        assertThat(infra.getNumberOfVideos()).isEqualTo(5);
        assertThat(infra.getNumberOfEndpoints()).isEqualTo(2);
        assertThat(infra.getNumberOfRequests()).isEqualTo(4);
        assertThat(infra.getNumberOfCacheServers()).isEqualTo(3);
        assertThat(infra.cacheServers.size()).isEqualTo(3);
    }


}
