package com.calpullix.service.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableCircuitBreaker
@EnableAutoConfiguration( exclude = RabbitAutoConfiguration.class)
@EnableDiscoveryClient
@ComponentScan("com.calpullix")
@EnableAsync
public class CalpullixServiceStatisticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalpullixServiceStatisticsApplication.class, args);
	}

}
