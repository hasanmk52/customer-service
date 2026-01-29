package com.netflux;

import com.netflux.client.MovieClient;
import com.netflux.domain.Genre;
import com.netflux.dto.CustomerDto;
import com.netflux.dto.GenreUpdateRequest;
import com.netflux.dto.MovieDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.RestClient;
import java.util.List;

@Import(TestContainersConfiguration.class)
@MockitoBean(types = {RestClient.class, MovieClient.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class CustomerServiceApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(CustomerServiceApplicationTests.class);

	@Autowired
    private RestTestClient restTestClient;

	@Autowired
	private MovieClient movieClient;

    @Test
    void health() {
        restTestClient
                .get()
                .uri("/actuator/health")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void customerWithMovies() {
        // mock recommended movies
        Mockito.when(movieClient.getMovies(Mockito.any(Genre.class)))
                .thenReturn(List.of(
                        new MovieDto(1, "movie-1", 1990, Genre.ACTION),
                        new MovieDto(2, "movie-2", 1991, Genre.ACTION)
                ));

        CustomerDto customerDto = restTestClient
                .get()
                .uri("/api/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(CustomerDto.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(customerDto);
        Assertions.assertEquals("sam", customerDto.name());
        Assertions.assertEquals(2, customerDto.recommendedMovies().size());
    }

    @Test
    void customerNotFound() {
        ProblemDetail problemDetail = restTestClient
                .get()
                .uri("/api/customers/10")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .returnResult()
                .getResponseBody();

        log.info("problem detail: {}", problemDetail);
        Assertions.assertNotNull(problemDetail);
        Assertions.assertEquals("Customer Not Found", problemDetail.getTitle());
    }

    @Test
    void updateGenre() {
        GenreUpdateRequest genreUpdateRequest = new GenreUpdateRequest(Genre.DRAMA);

        restTestClient
                .patch()
                .uri("/api/customers/1/genre")
                .contentType(MediaType.APPLICATION_JSON)
                .body(genreUpdateRequest)
                .exchange()
                .expectStatus().isNoContent();
    }

}
