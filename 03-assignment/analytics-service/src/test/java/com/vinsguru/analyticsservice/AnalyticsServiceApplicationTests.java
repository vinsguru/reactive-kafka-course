package com.vinsguru.analyticsservice;

import com.vinsguru.analyticsservice.dto.ProductTrendingDto;
import com.vinsguru.analyticsservice.event.ProductViewEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AutoConfigureWebTestClient(timeout = "10000") // this timeout is optional
class AnalyticsServiceApplicationTests extends AbstractIntegrationTest {

	@Autowired
	private WebTestClient client;

	@Test
	void trendingTest() {

		// emit events
		var events = Flux.just(
				createEvent(2, 2),
				createEvent(1, 1),
				createEvent(6, 3),
				createEvent(4, 2),
				createEvent(5, 5),
				createEvent(4, 2),
				createEvent(6, 3),
				createEvent(3, 3)
		).flatMap(Flux::fromIterable)
		.map(e -> this.toSenderRecord(PRODUCT_VIEW_EVENTS, e.getProductId().toString(), e));

		var resultFlux = this.<ProductViewEvent>createSender().send(events);

		StepVerifier.create(resultFlux)
				.expectNextCount(21)
				.verifyComplete();

		// verify via trending endpoint
		var mono = this.client
				.get()
				.uri("/trending")
				.accept(MediaType.TEXT_EVENT_STREAM)
				.exchange()
				.returnResult(new ParameterizedTypeReference<List<ProductTrendingDto>>() {})
				.getResponseBody()
				.next();

		StepVerifier.create(mono)
				.consumeNextWith(this::validateResult)
				.verifyComplete();

	}

	// 6,5,4,3,2   1
	private void validateResult(List<ProductTrendingDto> list){
		Assertions.assertEquals(5, list.size());
		Assertions.assertEquals(6, list.get(0).getProductId());
		Assertions.assertEquals(6, list.get(0).getViewCount());
		Assertions.assertEquals(2, list.get(4).getProductId());
		Assertions.assertEquals(2, list.get(4).getViewCount());
		Assertions.assertTrue(list.stream().noneMatch(p -> p.getProductId() == 1));
	}

	private List<ProductViewEvent> createEvent(int productId, int count){
		return IntStream.rangeClosed(1, count)
				.mapToObj(i -> new ProductViewEvent(productId))
				.collect(Collectors.toList());
	}

}
