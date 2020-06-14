package com.calpullix.service.statistics.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.calpullix.service.statistics.model.StatisticsRequestDTO;
import com.calpullix.service.statistics.service.StatisticsService;
import com.calpullix.service.statistics.util.AbstractWrapper;
import com.calpullix.service.statistics.util.ValidationHandler;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class StatisticsHandler {
	
	@Autowired
	private StatisticsService statisticsService;
	
	@Autowired
	private ValidationHandler validationHandler;

	@Value("${app.message-error-location-body}")
	private String messageErrorLocationBody;

	@Timed(value = "calpullix.service.statistics.metrics", description = "Retrieve statistics ")
	public Mono<ServerResponse> getStatistics(ServerRequest serverRequest) {
		log.info(":: Retrieve Statistics Handler {} ", serverRequest);

		return validationHandler.handle(
				input -> input.flatMap(request -> AbstractWrapper.async(() -> statisticsService.getStatistics(request)
				)).flatMap(response -> ServerResponse.ok().body(BodyInserters.fromObject(response))), serverRequest,
				StatisticsRequestDTO.class).switchIfEmpty(Mono.error(new Exception(messageErrorLocationBody)));
	}
	
}
