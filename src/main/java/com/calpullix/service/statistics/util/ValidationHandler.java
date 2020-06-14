package com.calpullix.service.statistics.util;

import java.util.function.Function;

import javax.validation.Validator;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ValidationHandler {

	private static final int DOESNT_EXIST = -1;

	private final Validator validator;

	public ValidationHandler(Validator validator) {
		this.validator = validator;
	}

	public <T> Mono<ServerResponse> handle(Function<Mono<T>, Mono<ServerResponse>> block, ServerRequest serverRequest,
			Class<T> bodyClass) {
		log.info(":: Validating request ");
		return serverRequest.bodyToMono(bodyClass).flatMap(request -> {
			if (validator.validate(request).isEmpty()) {
				return block.apply(Mono.just(request));
			} else {
				return processValidation(request);
			}
		});
	}
	
	private <T> Mono<ServerResponse> processValidation(T request) {
		final StringBuilder location = new StringBuilder();
		validator.validate(request).forEach(item -> {
			if(location.indexOf(item.getPropertyPath().toString()) == DOESNT_EXIST) {
				location.append(item.getPropertyPath());
				location.append(" ");
			}
		});
		return Mono.error(new Exception(location.toString()));
	}

}
