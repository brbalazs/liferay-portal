/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.experiment;

import com.liferay.osb.asah.backend.constants.ExperimentConstants;
import com.liferay.osb.asah.backend.constants.SamplerType;
import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.ExperimentMetric;
import com.liferay.osb.asah.common.entity.ExperimentVariant;
import com.liferay.osb.asah.common.entity.ExperimentVariantMetric;
import com.liferay.osb.asah.common.model.DXPVariantSettings;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.model.GoalMetric;
import com.liferay.osb.asah.common.util.ListUtil;

import io.improbable.keanu.Keanu;
import io.improbable.keanu.algorithms.NetworkSamples;
import io.improbable.keanu.algorithms.PosteriorSamplingAlgorithm;
import io.improbable.keanu.algorithms.mcmc.NetworkSamplesGenerator;
import io.improbable.keanu.network.BayesianNetwork;
import io.improbable.keanu.network.KeanuProbabilisticModelWithGradient;
import io.improbable.keanu.tensor.bool.BooleanTensor;
import io.improbable.keanu.tensor.bool.JVMBooleanTensor;
import io.improbable.keanu.tensor.dbl.DoubleTensor;

import java.math.BigDecimal;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.util.FastMath;

/**
 * @author Marcellus Tavares
 */
public abstract class BaseExperimentMetricCalculator<T>
	implements ExperimentMetricCalculator {

	@Override
	public Long estimateDaysDuration(
		List<DXPVariantSettings> dxpVariantsSettings, Experiment experiment) {

		Goal goal = experiment.getGoal();

		if (goal.getGoalMetric() == GoalMetric.CLICK_RATE) {
			return null;
		}

		List<Variant<T>> variants = ListUtil.map(
			dxpVariantsSettings,
			dxpVariantSettings -> mapVariant(dxpVariantSettings, experiment));

		for (Variant<T> variant : variants) {
			if ((variant.getFailures() < 5) || (variant.getSuccesses() < 5)) {
				return null;
			}
		}

		setVariantsEstimatedSampleSize(
			experiment.getConfidenceLevel(), variants);

		return _estimateDaysLeft(experiment, variants);
	}

	protected ExperimentMetric createEmptyExperimentMetric(
		Experiment experiment, List<Variant<T>> variants) {

		ExperimentMetric experimentMetric = new ExperimentMetric();

		for (Variant<T> variant : variants) {
			experimentMetric.addExperimentVariantMetric(
				_createEmptyExperimentVariantMetric(variant));
		}

		experimentMetric.setConfidenceLevel(0.0);
		experimentMetric.setElapsedDays(_getExperimentElapsedDays(experiment));
		experimentMetric.setEstimatedDaysLeft(null);
		experimentMetric.setProcessedLocalDateTime(
			LocalDateTime.now(ZoneOffset.UTC));

		return experimentMetric;
	}

	protected ExperimentMetric createExperimentMetric(
		Experiment experiment, Map<String, DoubleTensor> variantDoubleTensorMap,
		List<Variant<T>> variants) {

		ExperimentMetric experimentMetric = new ExperimentMetric();

		Variant<T> controlVariant = findControlVariant(variants);
		Goal goal = experiment.getGoal();

		for (Variant<T> variant : variants) {
			experimentMetric.addExperimentVariantMetric(
				_createExperimentVariantMetric(
					experiment.getConfidenceLevel(), controlVariant,
					goal.getGoalMetric(), variant, variantDoubleTensorMap));
		}

		setVariantsEstimatedSampleSize(
			experiment.getConfidenceLevel(), variants);

		experimentMetric.setCompletion(_getExperimentCompletion(variants));
		experimentMetric.setConfidenceLevel(experiment.getConfidenceLevel());
		experimentMetric.setElapsedDays(_getExperimentElapsedDays(experiment));
		experimentMetric.setEstimatedDaysLeft(
			_estimateDaysLeft(experiment, variants));
		experimentMetric.setProcessedLocalDateTime(
			LocalDateTime.now(ZoneOffset.UTC));

		return experimentMetric;
	}

	protected NetworkSamples createPosteriorNetworkSamples(
		BayesianNetwork bayesianNetwork, int dropCount, int sampleSize,
		SamplerType samplerType) {

		PosteriorSamplingAlgorithm posteriorSamplingAlgorithm;

		if (samplerType == SamplerType.METROPOLIS_HASTINGS) {
			posteriorSamplingAlgorithm =
				Keanu.Sampling.MetropolisHastings.withDefaultConfig();
		}
		else if (samplerType == SamplerType.NO_U_TURN_SAMPLER) {
			posteriorSamplingAlgorithm = Keanu.Sampling.NUTS.builder(
			).build();
		}
		else {
			throw new IllegalStateException(
				"Unrecognized sampler type: " + samplerType);
		}

		NetworkSamplesGenerator networkSamplesGenerator =
			posteriorSamplingAlgorithm.generatePosteriorSamples(
				new KeanuProbabilisticModelWithGradient(bayesianNetwork),
				bayesianNetwork.getLatentVertices());

		networkSamplesGenerator.dropCount(dropCount);

		return networkSamplesGenerator.generate(sampleSize);
	}

	protected Variant<T> findControlVariant(List<Variant<T>> variants) {
		Stream<Variant<T>> stream = variants.stream();

		Optional<Variant<T>> controlVariantOptional = stream.filter(
			Variant::isControl
		).findFirst();

		return controlVariantOptional.orElseThrow(IllegalStateException::new);
	}

	protected List<Variant<T>> getVariants(Experiment experiment) {
		Set<ExperimentVariant> experimentVariants =
			experiment.getExperimentVariants();

		if (experimentVariants == null) {
			return Collections.emptyList();
		}

		return ListUtil.map(
			experimentVariants,
			experimentVariant -> mapVariant(experiment, experimentVariant));
	}

	protected abstract Variant<T> mapVariant(
		DXPVariantSettings dxpVariantSettings, Experiment experiment);

	protected abstract Variant<T> mapVariant(
		Experiment experiment, ExperimentVariant experimentVariant);

	protected abstract void setVariantsEstimatedSampleSize(
		double confidenceLevel, List<Variant<T>> variants);

	private BigDecimal[] _calculateConfidenceIntervals(
		double confidenceLevel, DoubleTensor doubleTensor) {

		Percentile percentile = new Percentile();

		double lowerQuantile = 100 - confidenceLevel;
		double upperQuantile = confidenceLevel;

		double[] data = doubleTensor.asFlatDoubleArray();

		double lowerBound = percentile.evaluate(data, lowerQuantile);
		double upperBound = percentile.evaluate(data, upperQuantile);

		return new BigDecimal[] {
			BigDecimal.valueOf(lowerBound), BigDecimal.valueOf(upperBound)
		};
	}

	private double _calculateImprovement(
		double controlMedian, GoalMetric goalMetric, double variantMedian) {

		if (controlMedian == 0) {
			return 0;
		}

		double improvement =
			(variantMedian - controlMedian) / controlMedian * 100;

		if (goalMetric.isInverseMetric()) {
			return -improvement;
		}

		return improvement;
	}

	private long _calculateMaxExperimentDaysLeft(List<Variant<T>> variants) {
		Stream<Variant<T>> stream = variants.stream();

		return stream.map(
			variant ->
				(variant.getEstimatedSampleSize() - variant.getTrials()) /
					variant.getEstimatedTrafficRate()
		).mapToLong(
			value -> (long)Math.max(FastMath.ceil(value), 0)
		).max(
		).orElse(
			0L
		);
	}

	private double _calculateMedian(DoubleTensor doubleTensor) {
		Percentile percentile = new Percentile();

		return percentile.evaluate(
			doubleTensor.asFlatDoubleArray(), _PERCENTILE_FOR_MEDIAN);
	}

	private double _calculateProbabilityToWin(
		GoalMetric goalMetric, Variant<T> variant,
		DoubleTensor variantDoubleTensor,
		Map<String, DoubleTensor> variantDoubleTensorMap) {

		long[] shape = variantDoubleTensor.getShape();

		int tensorLength = (int)shape[0];

		boolean[] trueArray = new boolean[tensorLength];

		Arrays.fill(trueArray, true);

		BooleanTensor booleanTensor = JVMBooleanTensor.create(trueArray, shape);

		for (Map.Entry<String, DoubleTensor> entry :
				variantDoubleTensorMap.entrySet()) {

			if (Objects.equals(variant.getDXPVariantId(), entry.getKey())) {
				continue;
			}

			DoubleTensor otherVariantDoubleTensor = variantDoubleTensorMap.get(
				entry.getKey());

			if (goalMetric.isInverseMetric()) {
				booleanTensor.andInPlace(
					variantDoubleTensor.lessThan(otherVariantDoubleTensor));
			}
			else {
				booleanTensor.andInPlace(
					variantDoubleTensor.greaterThan(otherVariantDoubleTensor));
			}
		}

		DoubleTensor doubleTensor = booleanTensor.toDoubleMask();

		return doubleTensor.sum() / tensorLength * 100;
	}

	private ExperimentVariantMetric _createEmptyExperimentVariantMetric(
		Variant<T> variant) {

		ExperimentVariantMetric experimentVariantMetric =
			new ExperimentVariantMetric(
				variant.isControl(), variant.getDXPVariantId());

		experimentVariantMetric.setConfidenceIntervals(
			new BigDecimal[] {
				BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)
			});
		experimentVariantMetric.setImprovement(0.0);
		experimentVariantMetric.setMedian(0.0);
		experimentVariantMetric.setProbabilityToWin(0.0);

		return experimentVariantMetric;
	}

	private ExperimentVariantMetric _createExperimentVariantMetric(
		double confidenceLevel, Variant<T> controlVariant,
		GoalMetric goalMetric, Variant<T> variant,
		Map<String, DoubleTensor> variantDoubleTensorMap) {

		ExperimentVariantMetric experimentVariantMetric =
			new ExperimentVariantMetric(
				variant.isControl(), variant.getDXPVariantId());

		DoubleTensor variantDoubleTensor = variantDoubleTensorMap.get(
			variant.getDXPVariantId());

		experimentVariantMetric.setConfidenceIntervals(
			_calculateConfidenceIntervals(
				confidenceLevel, variantDoubleTensor));
		experimentVariantMetric.setMedian(
			_calculateMedian(variantDoubleTensor));
		experimentVariantMetric.setProbabilityToWin(
			_calculateProbabilityToWin(
				goalMetric, variant, variantDoubleTensor,
				variantDoubleTensorMap));

		if (!variant.isControl()) {
			double controlMedian = _calculateMedian(
				variantDoubleTensorMap.get(controlVariant.getDXPVariantId()));

			experimentVariantMetric.setImprovement(
				_calculateImprovement(
					controlMedian, goalMetric,
					experimentVariantMetric.getMedian()));
		}

		return experimentVariantMetric;
	}

	private Long _estimateDaysLeft(
		Experiment experiment, List<Variant<T>> variants) {

		long daysLeftMax = _calculateMaxExperimentDaysLeft(variants);
		long daysLeftMin =
			ExperimentConstants.MINIMUM_EXPERIMENT_DURATION_IN_DAYS -
				_getExperimentElapsedDays(experiment);

		return Math.max(daysLeftMax, daysLeftMin);
	}

	private Double _getExperimentCompletion(List<Variant<T>> variants) {
		Stream<Variant<T>> stream = variants.stream();

		return stream.filter(
			variant ->
				(variant.getEstimatedSampleSize() > 0) &&
				(variant.getTrials() > 0)
		).mapToDouble(
			variant ->
				(double)variant.getTrials() / variant.getEstimatedSampleSize() *
					100
		).map(
			ratio -> Math.min(ratio, 100)
		).average(
		).orElse(
			0D
		);
	}

	private long _getExperimentElapsedDays(Experiment experiment) {
		Date experimentStartedDate = experiment.getStartedDate();

		if (experimentStartedDate == null) {
			return 0;
		}

		return ChronoUnit.DAYS.between(
			experimentStartedDate.toInstant(), Instant.now());
	}

	private static final double _PERCENTILE_FOR_MEDIAN = 50.0;

}