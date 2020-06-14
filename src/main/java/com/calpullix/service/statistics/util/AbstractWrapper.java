package com.calpullix.service.statistics.util;

import java.util.concurrent.Callable;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


public interface AbstractWrapper {
	
	static <T> Mono<T> async(Callable<T> callable) {
		return Mono.fromCallable(callable).subscribeOn(Schedulers.elastic());
	}

}
