package com.calpullix.service.statistics.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatisticsResponseDTO {

	private Integer idProduct;
	
	private String name;
	
	private String branch;
	
	private List<BoxPlotDetailDTO> boxPlot;
	
	private String labelStatisticsTable;
	
	private List<String> headersStatistics;
	
	private List<List<String>> rowsStatistics;
	
	private String labelGroupByTable;
	
	private List<String> headersGroupBy;
	
	private List<List<String>> rowsGroupBy;
	
	private String labelHeatMap;
	
	private byte[] heatMap;
	
	private String labelCausationCorrelation;
	
	private List<String> headersCorrelationCausation;
	
	private List<List<String>> rowsCorrelationCausation;
	
	private List<AnovaDetailDTO> anovaDetail;

	
}
