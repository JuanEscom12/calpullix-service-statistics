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
public class AnovaDetailDTO {

	private String nameVariable;
	
	private String fTestScore;
	
	private String pValue;
	
}
