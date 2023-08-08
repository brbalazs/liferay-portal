/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.api.data.source.v1;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.dog.ExperimentDog;
import com.liferay.osb.asah.backend.dto.ExperimentDTO;
import com.liferay.osb.asah.backend.dto.ExperimentMetricDTO;
import com.liferay.osb.asah.backend.dto.ExperimentVariantsDTO;
import com.liferay.osb.asah.backend.dto.GoalDTO;
import com.liferay.osb.asah.backend.model.ExperimentSettings;
import com.liferay.osb.asah.backend.rest.controller.BaseRestController;
import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.ExperimentVariant;
import com.liferay.osb.asah.common.model.DXPVariantSettings;
import com.liferay.osb.asah.common.model.ExperimentStatus;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcellus Tavares
 */
@RequestMapping(produces = "application/json", value = "/api/1.0/experiments")
@RestController
public class ExperimentsRestController extends BaseRestController {

	@DeleteMapping("/{id}")
	public void deleteExperiment(@PathVariable Long id) {
		_experimentDog.deleteExperiment(id);
	}

	@GetMapping("/{id}/calculate-metrics")
	public ExperimentMetricDTO getCalculateExperimentMetric(
		@PathVariable Long id) {

		return new ExperimentMetricDTO(
			_experimentDog.calculateExperimentMetric(id));
	}

	@GetMapping("/{id}")
	public ExperimentDTO getExperiment(@PathVariable Long id) {
		return new ExperimentDTO(_experimentDog.getExperiment(id));
	}

	@PatchMapping("/{id}")
	public void patchExperiment(
		@PathVariable Long id,
		@RequestBody @Valid ExperimentDTO experimentDTO) {

		Experiment experiment = _objectMapper.convertValue(
			experimentDTO, Experiment.class);

		experiment.setId(id);

		ExperimentSettings experimentSettings = null;

		if (experiment.getExperimentStatus() == ExperimentStatus.RUNNING) {
			experimentSettings = _createExperimentSettings(experiment);
		}

		_experimentDog.patchExperiment(experiment, experimentSettings);
	}

	@PostMapping
	public ExperimentDTO postExperiment(
		@RequestBody @Valid ExperimentDTO experimentDTO) {

		return new ExperimentDTO(
			_experimentDog.addExperiment(
				_objectMapper.convertValue(experimentDTO, Experiment.class)));
	}

	@PostMapping("/{id}/estimated-days-duration")
	public Long postExperimentEstimatedDaysDuration(
		@PathVariable Long id,
		@RequestBody @Valid ExperimentSettings experimentSettings) {

		List<DXPVariantSettings> dxpVariantsSettings =
			experimentSettings.getDXPVariantsSettings();

		double variantsTrafficSum = _sumVariantsTraffic(dxpVariantsSettings);

		if (variantsTrafficSum != 100) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"The sum of all variant traffic splits must be 100");
		}

		return _experimentDog.getExperimentEstimatedDaysDuration(
			experimentSettings.getConfidenceLevel(), dxpVariantsSettings, id);
	}

	@PutMapping("/{id}/dxp-variants")
	public void putExperimentVariants(
		@PathVariable Long id,
		@RequestBody @Valid ExperimentVariantsDTO experimentVariantsDTO) {

		Experiment experiment = _experimentDog.getExperiment(id);

		experiment.setExperimentVariants(
			SetUtil.map(
				experimentVariantsDTO.getExperimentVariantDTOs(),
				currentDXPVariantDTO -> _objectMapper.convertValue(
					currentDXPVariantDTO, ExperimentVariant.class)));

		_experimentDog.updateExperiment(experiment);
	}

	@PutMapping("/{id}/goal")
	public void putGoal(
		@PathVariable Long id, @RequestBody @Valid GoalDTO goalDTO) {

		Experiment experiment = _experimentDog.getExperiment(id);

		experiment.setGoal(_objectMapper.convertValue(goalDTO, Goal.class));

		_experimentDog.updateExperiment(experiment);
	}

	private List<DXPVariantSettings> _createDXPVariantSettings(
		Set<ExperimentVariant> experimentVariants) {

		return ListUtil.map(
			experimentVariants,
			experimentVariant -> new DXPVariantSettings(
				experimentVariant.isControl(),
				experimentVariant.getDXPVariantId(),
				experimentVariant.getTrafficSplit()));
	}

	private ExperimentSettings _createExperimentSettings(
		Experiment experiment) {

		Double confidenceLevel = experiment.getConfidenceLevel();

		if (confidenceLevel == 0) {
			return null;
		}

		ExperimentSettings experimentSettings = new ExperimentSettings();

		experimentSettings.setConfidenceLevel(confidenceLevel);
		experimentSettings.setDXPVariantsSettings(
			_createDXPVariantSettings(experiment.getExperimentVariants()));

		return experimentSettings;
	}

	private double _sumVariantsTraffic(
		List<DXPVariantSettings> dxpVariantsSettings) {

		Stream<DXPVariantSettings> stream = dxpVariantsSettings.stream();

		return stream.map(
			DXPVariantSettings::getTrafficSplit
		).reduce(
			Double::sum
		).orElse(
			0.0
		);
	}

	@Autowired
	private ExperimentDog _experimentDog;

	@Autowired
	private ObjectMapper _objectMapper;

}