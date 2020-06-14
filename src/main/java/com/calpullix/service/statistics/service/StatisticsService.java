package com.calpullix.service.statistics.service;

import com.calpullix.service.statistics.model.StatisticsRequestDTO;
import com.calpullix.service.statistics.model.StatisticsResponseDTO;

public interface StatisticsService {

	StatisticsResponseDTO getStatistics(StatisticsRequestDTO request);
	
}
