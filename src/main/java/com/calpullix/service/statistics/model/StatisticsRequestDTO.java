package com.calpullix.service.statistics.model;

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
public class StatisticsRequestDTO {
	
	private Integer idBranch;
	
	private Integer product;
	
	private Integer year;
	
	private Integer month;
	
}
