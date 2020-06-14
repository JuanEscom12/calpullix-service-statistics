package com.calpullix.service.statistics.conf;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.calpullix.service.statistics.handler.StatisticsHandler;


@Configuration
public class RoutesConf {

	@Value("${app.path-retrieve-statistics}")
	private String pathStatistics;
	
	
	@Bean
	public RouterFunction<ServerResponse> routesStatistics(StatisticsHandler statisticsHandler) {
		return route(POST(pathStatistics), statisticsHandler::getStatistics);
	}
	
}
