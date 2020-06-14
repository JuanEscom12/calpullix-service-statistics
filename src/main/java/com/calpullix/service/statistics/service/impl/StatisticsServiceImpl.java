package com.calpullix.service.statistics.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calpullix.db.process.branch.model.Branch;
import com.calpullix.db.process.branch.repository.BranchRepository;
import com.calpullix.db.process.product.model.Product;
import com.calpullix.db.process.product.repository.ProductRepository;
import com.calpullix.db.process.statistics.box.plot.StatisticsBloxPlot;
import com.calpullix.db.process.statistics.box.plot.repository.StatisticsBloxPlotRepository;
import com.calpullix.db.process.statistics.model.Statistics;
import com.calpullix.db.process.statistics.model.StatisticsAnova;
import com.calpullix.db.process.statistics.model.StatisticsCorrelation;
import com.calpullix.db.process.statistics.model.StatisticsCorrelationVariableRelation;
import com.calpullix.db.process.statistics.model.StatisticsGroupby;
import com.calpullix.db.process.statistics.model.StatisticsGroupbyVariableRelation;
import com.calpullix.db.process.statistics.model.StatisticsHeatmap;
import com.calpullix.db.process.statistics.model.StatisticsVariableRelation;
import com.calpullix.db.process.statistics.repository.StatisticsAnovaRepository;
import com.calpullix.db.process.statistics.repository.StatisticsCorrelationRepository;
import com.calpullix.db.process.statistics.repository.StatisticsGroupbyRepository;
import com.calpullix.db.process.statistics.repository.StatisticsHeatMapRepository;
import com.calpullix.db.process.statistics.repository.StatisticsRepository;
import com.calpullix.service.statistics.model.AnovaDetailDTO;
import com.calpullix.service.statistics.model.BoxPlotDetailDTO;
import com.calpullix.service.statistics.model.StatisticsRequestDTO;
import com.calpullix.service.statistics.model.StatisticsResponseDTO;
import com.calpullix.service.statistics.service.StatisticsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

	private final static String LABEL_HEAT_MAP = "Heat-Map del agrupamiento:  ";
	
	private final static String LABEL_STATISTICS = "Estadisticas descriptivas";

	private final static String LABEL_GROUP_BY = "Agrupamiento por: ";
	
	private final static String LABEL_CORRELATION = "Tabla de correlación y causa";
	
	private final static String LABEL_BLOX_PLOT = "Blox Plot: ";
	
	private final static String LABEL_DESC = "Variable";
	
	private static final String VERSUS_LABEL  = " vs ";
	
	@Autowired
	private BranchRepository branchRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StatisticsRepository statisticsRepository;

	@Autowired
	private StatisticsHeatMapRepository statisticsHeatMapRepository;

	@Autowired
	private StatisticsCorrelationRepository statisticsCorrelationRepository;

	@Autowired
	private StatisticsAnovaRepository statisticsAnovaRepository;

	@Autowired
	private StatisticsBloxPlotRepository statisticsBloxPlotRepository;
	
	@Autowired
	private StatisticsGroupbyRepository statisticsGroupbyRepository;

	// year 2019, Month 2, product 2, branch 1
	@Override
	public StatisticsResponseDTO getStatistics(StatisticsRequestDTO request) {
		log.info(":: Service getStatistics {} ", request);
		final StatisticsResponseDTO result = new StatisticsResponseDTO();

		result.setLabelStatisticsTable(LABEL_STATISTICS);
		result.setLabelCausationCorrelation(LABEL_CORRELATION);
		result.setName("");

		Optional<Branch> idbranch = branchRepository.findById(request.getIdBranch());
		result.setBranch(idbranch.get().getName());
		Product idproduct;
		CompletableFuture<Statistics> statistics;
		CompletableFuture<StatisticsHeatmap> heatMap;
		CompletableFuture<StatisticsCorrelation> correlation;
		CompletableFuture<StatisticsGroupby> groupby;
		CompletableFuture<List<StatisticsAnova>> anova;
		CompletableFuture<List<StatisticsBloxPlot>> boxPlot;
		

		if (BooleanUtils.negate(request.getProduct() == null) && BooleanUtils.negate(request.getMonth() == null)) {

			idproduct = setProductInformation(result, request);
			statistics = statisticsRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), idproduct,
					request.getYear(), request.getMonth());
			heatMap = statisticsHeatMapRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), idproduct,
					request.getYear(), request.getMonth());
			correlation = statisticsCorrelationRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), idproduct,
					request.getYear(), request.getMonth());
			anova = statisticsAnovaRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), idproduct,
					request.getYear(), request.getMonth());
			boxPlot = statisticsBloxPlotRepository.findAllByIdbranchAndIdproductAndYearAndMonth(idbranch.get(),
					idproduct, request.getYear(), request.getMonth());
			groupby = statisticsGroupbyRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), idproduct,
					request.getYear(), request.getMonth());
		} else if (BooleanUtils.negate(request.getProduct() == null) && request.getMonth() == null) {
			idproduct = setProductInformation(result, request);
			statistics = statisticsRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), idproduct,
					request.getYear(), null);
			heatMap = statisticsHeatMapRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), idproduct,
					request.getYear(), null);
			correlation = statisticsCorrelationRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), idproduct,
					request.getYear(), null);
			anova = statisticsAnovaRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), idproduct,
					request.getYear(), null);
			boxPlot = statisticsBloxPlotRepository.findAllByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), idproduct,
					request.getYear(), null);
			groupby = statisticsGroupbyRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), idproduct,
					request.getYear(), null);
		} else if (BooleanUtils.negate(request.getMonth() == null) && request.getProduct() == null) {
			statistics = statisticsRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), request.getMonth());
			heatMap = statisticsHeatMapRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), request.getMonth());
			correlation = statisticsCorrelationRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), request.getMonth());
			anova = statisticsAnovaRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), request.getMonth());
			boxPlot = statisticsBloxPlotRepository.findAllByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), request.getMonth());
			groupby = statisticsGroupbyRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), request.getMonth());
		} else {
			statistics = statisticsRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), null);
			heatMap = statisticsHeatMapRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), null);
			correlation = statisticsCorrelationRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), null);
			anova = statisticsAnovaRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), null);
			boxPlot = statisticsBloxPlotRepository.findAllByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), null);
			groupby = statisticsGroupbyRepository.findOneByIdbranchAndIdproductAndYearAndMonth(idbranch.get(), null,
					request.getYear(), null);
		}
		try {
			log.info(":: Results statistics {} ", statistics.get(), heatMap.get(), correlation.get(), anova.get(),
					boxPlot.get());
			
			if (BooleanUtils.negate(boxPlot.get() == null)) {
				setBloxPlot(boxPlot.get(), result);
			}
			if (BooleanUtils.negate(statistics.get() == null)) {
				setStatistics(statistics.get(), result);
			}
			if (BooleanUtils.negate(groupby.get() == null)) {
				setGroupBy(groupby.get(), result);
			}
			if (BooleanUtils.negate(heatMap.get() == null)) {
				result.setHeatMap(heatMap.get().getImage());
				result.setLabelGroupByTable(LABEL_GROUP_BY + heatMap.get().getKeyvar() + " y " + heatMap.get().getVerticalvarone());
				result.setLabelHeatMap(LABEL_HEAT_MAP + heatMap.get().getKeyvar() + VERSUS_LABEL + heatMap.get().getVerticalvarone()
						+ " - " + heatMap.get().getVerticalvartwo());
			}
			if (BooleanUtils.negate(correlation.get() == null)) {
				setCorrelationCausation(correlation.get(), result);
			}
			if (BooleanUtils.negate(anova.get() == null)) {
				setAnova(anova.get(), result);
			}
		} catch (InterruptedException | ExecutionException e) {
			log.error(":: Error getting response statistics service ", e);
		}
		return result;
	}

	private Product setProductInformation(StatisticsResponseDTO result, StatisticsRequestDTO request) {
		final Optional<Product> idproduct = productRepository.findById(request.getProduct());
		result.setIdProduct(idproduct.get().getId());
		result.setName(idproduct.get().getName());
		return idproduct.get();
	}
		
	private void setBloxPlot(List<StatisticsBloxPlot> bloxPlot, StatisticsResponseDTO result) {
		final List<BoxPlotDetailDTO> boxPlot = new ArrayList<>();
		BoxPlotDetailDTO detail;
		for (final StatisticsBloxPlot item: bloxPlot) {
			detail = new BoxPlotDetailDTO();
			detail.setHeaderDetail(LABEL_BLOX_PLOT + 
					item.getVardepname() + VERSUS_LABEL + item.getVarindname());
			detail.setBoxPlot(item.getImage());
			boxPlot.add(detail);
		}
		result.setBoxPlot(boxPlot);		
	}

	private void setStatistics(Statistics statistics, StatisticsResponseDTO result) {
		List<String> statisticsHeaders = new ArrayList<>();
		List<List<String>> rows = new ArrayList<>();
		List<String> row;
		statisticsHeaders.add(LABEL_DESC);
		statisticsHeaders.add(statistics.getVarnameone());
		statisticsHeaders.add(statistics.getVarnametwo());
		statisticsHeaders.add(statistics.getVarnamethree());
		statisticsHeaders.add(statistics.getVarnamefour());
		for (final StatisticsVariableRelation item: statistics.getStatistics()) {
			row = new ArrayList<>();
			row.add(item.getStatisticvar());
			row.add(item.getVarvalueone());
			row.add(item.getVarvaluetwo());
			row.add(item.getVarvaluethree());
			row.add(item.getVarvaluefour());
			rows.add(row);
		}
		result.setHeadersStatistics(statisticsHeaders);
		result.setRowsStatistics(rows);
	}
	
	private void setGroupBy(StatisticsGroupby gropuBy, StatisticsResponseDTO result) {
		final List<List<String>> rows = new ArrayList<>();
		final List<String> statisticsHeaders = new ArrayList<>();
		List<String> row;
		statisticsHeaders.add(gropuBy.getHorizontalvarnameone());
		statisticsHeaders.add(gropuBy.getHorizontalvarnametwo());
		statisticsHeaders.add(gropuBy.getHorizontalvarnamethree());
		if (BooleanUtils.negate(gropuBy.getHorizontalvarnamefour() == null)) {
			statisticsHeaders.add(gropuBy.getHorizontalvarnamefour());
		}
		for (final StatisticsGroupbyVariableRelation item: gropuBy.getStatisticsGroupbyVariableRelation()) {
			row = new ArrayList<>();
			row.add(item.getRelationvalueone());
			row.add(item.getRelationvaluetwo());
			row.add(item.getRelationvaluethree());
			if (BooleanUtils.negate(item.getRelationvaluefour() == null)) {
				row.add(item.getRelationvaluefour());
			}
			rows.add(row);
		}
		result.setHeadersGroupBy(statisticsHeaders);
		result.setRowsGroupBy(rows);
	}
	
	private void setCorrelationCausation(StatisticsCorrelation correlation, StatisticsResponseDTO result) {
		final List<String> statisticsHeaders = new ArrayList<>();
		statisticsHeaders.add(correlation.getVerticalvarname());
		statisticsHeaders.add(correlation.getHorizontalvarnameone());
		statisticsHeaders.add(correlation.getHorizontalvarnametwo());
		statisticsHeaders.add(correlation.getHorizontalvarnamethree());
		statisticsHeaders.add(correlation.getHorizontalvarnamefour());
		final List<List<String>> rows = new ArrayList<>();
		List<String> row;
		int index = 1;
		for (final StatisticsCorrelationVariableRelation item: correlation.getStatisticsCorrelationVariableRelation()) {
			row = new ArrayList<>();
			row.add(statisticsHeaders.get(index));
			index++;
			row.add(item.getRelationvalueone());
			row.add(item.getRelationvaluetwo());
			row.add(item.getRelationvaluethree());
			row.add(item.getRelationvaluefour());
			rows.add(row);
		}
		result.setHeadersCorrelationCausation(statisticsHeaders);
		result.setRowsCorrelationCausation(rows);
	}
	
	private void setAnova(List<StatisticsAnova> anova, StatisticsResponseDTO result) {
		final List<AnovaDetailDTO> anovaDetail = new ArrayList<>();
		AnovaDetailDTO detailAnova = new AnovaDetailDTO();
		for (final StatisticsAnova item: anova) {
			detailAnova = new AnovaDetailDTO();
			detailAnova.setNameVariable(item.getVarsindname() + VERSUS_LABEL + item.getVardepname());
			detailAnova.setFTestScore(item.getFtestscore());
			detailAnova.setPValue(item.getPvalue());
			anovaDetail.add(detailAnova);
		}
		result.setAnovaDetail(anovaDetail);
	}
	
}
