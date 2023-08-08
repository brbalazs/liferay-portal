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
import com.liferay.osb.asah.common.model.DXPVariantSettings;
import com.liferay.osb.asah.common.model.TimeRange;

import io.improbable.keanu.algorithms.NetworkSamples;
import io.improbable.keanu.network.BayesianNetwork;
import io.improbable.keanu.tensor.dbl.DoubleTensor;
import io.improbable.keanu.vertices.dbl.DoubleVertexSamples;
import io.improbable.keanu.vertices.dbl.nonprobabilistic.ConstantDoubleVertex;
import io.improbable.keanu.vertices.dbl.probabilistic.BetaVertex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Edward Kwok-Yu Wong
 * @author André Miranda
 * @author Marcellus Tavares
 */
@Component
public class DichotomousDataExperimentMetricCalculator
	extends BaseExperimentMetricCalculator<Double> {

	@Override
	public ExperimentMetric calculateExperimentMetric(Experiment experiment) {
		List<Variant<Double>> variants = getVariants(experiment);

		for (Variant<Double> variant : variants) {
			if ((variant.getFailures() < 5) || (variant.getSuccesses() < 5)) {
				return createEmptyExperimentMetric(experiment, variants);
			}
		}

		Pair<double[], double[]> alphasAndBetasPair =
			_calculateAlphasAndBetasPair(variants);

		long[] shape = {variants.size(), 1};

		ConstantDoubleVertex alphaConstantDoubleVertex =
			new ConstantDoubleVertex(alphasAndBetasPair.getFirst(), shape);

		ConstantDoubleVertex betaConstantDoubleVertex =
			new ConstantDoubleVertex(alphasAndBetasPair.getSecond(), shape);

		BetaVertex betaVertex = new BetaVertex(
			shape, alphaConstantDoubleVertex, betaConstantDoubleVertex);

		NetworkSamples posteriorNetworkSamples = createPosteriorNetworkSamples(
			new BayesianNetwork(betaVertex.getConnectedGraph()),
			ExperimentConstants.DISCARDED_SAMPLES,
			ExperimentConstants.MONTE_CARLO_SAMPLE_SIZE,
			SamplerType.NO_U_TURN_SAMPLER);

		DoubleVertexSamples doubleTensorSamples =
			posteriorNetworkSamples.getDoubleTensorSamples(betaVertex);

		DoubleTensor doubleTensor = doubleTensorSamples.asTensor();

		doubleTensor.timesInPlace(100D);

		Map<String, DoubleTensor> variantDoubleTensorMap = new HashMap<>();

		for (int i = 0; i < variants.size(); i++) {
			Variant variant = variants.get(i);

			variantDoubleTensorMap.put(
				variant.getDXPVariantId(), doubleTensor.slice(1, i));
		}

		return createExperimentMetric(
			experiment, variantDoubleTensorMap, variants);
	}

	@Override
	protected Variant<Double> mapVariant(
		DXPVariantSettings dxpVariantSettings, Experiment experiment) {

		Variant<Double> variant = new Variant<>(
			dxpVariantSettings.isControl(),
			dxpVariantSettings.getDXPVariantId(),
			dxpVariantSettings.getTrafficSplit());

		TimeRange timeRange = _getTimeRange(null);

		ExperimentDataPoint<Double> experimentDataPoint =
			_experimentDataDog.fetchDichotomousDataPoint(
				experiment.getPageURL(),
				ExperimentUtil.getPageMetricType(experiment), timeRange);

		double rate = dxpVariantSettings.getTrafficSplit() / 100;

		variant.addExperimentDataPoint(
			new ExperimentDataPoint<>(
				(long)(experimentDataPoint.getTrials() * rate),
				experimentDataPoint.getValue() * rate));

		setVariantProperties(timeRange.getDeltaDays(), variant);

		return variant;
	}

	@Override
	protected Variant<Double> mapVariant(
		Experiment experiment, ExperimentVariant experimentVariant) {

		Variant<Double> variant = new Variant<>(
			experimentVariant.isControl(), experimentVariant.getDXPVariantId(),
			experimentVariant.getTrafficSplit());

		TimeRange timeRange = _getTimeRange(
			experiment.getStartedDateLocalDateTime());

		ExperimentDataPoint<Double> experimentDataPoint =
			_experimentDataDog.fetchDichotomousDataPoint(
				experiment.getId(),
				ExperimentUtil.getPageMetricType(experiment), timeRange,
				experimentVariant.getDXPVariantId());

		variant.addExperimentDataPoint(experimentDataPoint);

		setVariantProperties(timeRange.getDeltaDays(), variant);

		return variant;
	}

	protected void setVariantProperties(
		long deltaDays, Variant<Double> variant) {

		_setVariantEstimatedTrafficRate(deltaDays, variant);
		_setVariantSuccessAndTrials(variant);
		_setVariantSuccessRate(variant);
	}

	@Override
	protected void setVariantsEstimatedSampleSize(
		double confidenceLevel, List<Variant<Double>> variants) {

		Variant<Double> controlVariant = findControlVariant(variants);

		double alpha = ExperimentUtil.calculateAlpha(
			confidenceLevel, variants.size());

		NormalDistribution normalDistribution = new NormalDistribution();

		double statisticAlpha = FastMath.abs(
			normalDistribution.inverseCumulativeProbability(alpha));

		double statisticBeta = normalDistribution.inverseCumulativeProbability(
			ExperimentConstants.DEFAULT_POWER_LEVEL);

		int largestControlSampleSize = 0;

		for (Variant variant : variants) {
			if (variant.isControl()) {
				continue;
			}

			double trafficSplitRatio =
				variant.getTrafficSplit() / controlVariant.getTrafficSplit();

			int sampleSize = _calculateSampleSize(
				controlVariant.getSuccessRate(), statisticAlpha, statisticBeta,
				trafficSplitRatio);

			if (sampleSize > largestControlSampleSize) {
				largestControlSampleSize = sampleSize;
			}
		}

		for (Variant variant : variants) {
			double trafficSplitRatio =
				variant.getTrafficSplit() / controlVariant.getTrafficSplit();

			double estimatedSampleSize = FastMath.ceil(
				largestControlSampleSize * trafficSplitRatio);

			variant.setEstimatedSampleSize((long)estimatedSampleSize);
		}
	}

	private Pair<double[], double[]> _calculateAlphasAndBetasPair(
		List<Variant<Double>> variants) {

		long totalSuccesses = 0;
		long totalTrials = 0;

		for (Variant<Double> variant : variants) {
			totalSuccesses += variant.getSuccesses();
			totalTrials += variant.getTrials();
		}

		double[] alphas = new double[variants.size()];
		double[] betas = new double[variants.size()];

		double totalSuccessRate = 0;

		if (totalTrials > 0) {
			totalSuccessRate = (double)totalSuccesses / totalTrials;
		}

		double globalWeight = 0;

		if (variants.size() > 2) {
			globalWeight = _GROUP_WEIGHT;
		}

		int i = 0;

		for (Variant variant : variants) {
			double alphaSuccessRate = globalWeight * totalSuccessRate;

			double alphaVariantSuccessRate =
				alphaSuccessRate +
					((1 - globalWeight) * variant.getSuccessRate());

			alphas[i] =
				(alphaVariantSuccessRate * variant.getTrials()) + _ALPHA_PRIOR;

			double betaSuccessRate = globalWeight * (1 - totalSuccessRate);

			double betaVariantSuccessRate =
				betaSuccessRate +
					((1 - globalWeight) * (1 - variant.getSuccessRate()));

			betas[i] =
				(betaVariantSuccessRate * variant.getTrials()) + _BETA_PRIOR;

			i++;
		}

		return new Pair<>(alphas, betas);
	}

	private int _calculateSampleSize(
		double controlSuccessRate, double statisticAlpha, double statisticBeta,
		double trafficSplitRatio) {

		double epsilon =
			controlSuccessRate *
				ExperimentConstants.
					DEFAULT_MINIMUM_DETECTABLE_EFFECT_PERCENTAGE;

		double target = controlSuccessRate + epsilon;

		double controlSuccessRateComplement = 1 - controlSuccessRate;

		double controlRate =
			(target * (1 - target) / trafficSplitRatio) +
				(controlSuccessRate * controlSuccessRateComplement);

		return (int)FastMath.ceil(
			FastMath.pow(statisticAlpha + statisticBeta, 2) /
				FastMath.pow(epsilon, 2) * controlRate);
	}

	private TimeRange _getTimeRange(LocalDateTime startedDateLocalDateTime) {
		LocalDate startLocalDate = null;

		if (startedDateLocalDateTime == null) {
			LocalDate localDate = LocalDate.now(ZoneOffset.UTC);

			startLocalDate = localDate.minusDays(
				ExperimentConstants.MINIMUM_TRAFFIC_SAMPLE_SIZE);
		}
		else {
			startLocalDate = startedDateLocalDateTime.toLocalDate();
		}

		return TimeRange.of(startLocalDate);
	}

	private void _setVariantEstimatedTrafficRate(
		long deltaDays, Variant<Double> variant) {

		List<ExperimentDataPoint<Double>> experimentDataPoints =
			variant.getExperimentDataPoints();

		Stream<ExperimentDataPoint<Double>> stream =
			experimentDataPoints.stream();

		stream.map(
			ExperimentDataPoint::getTrials
		).reduce(
			Long::sum
		).map(
			variantTraffic -> (double)variantTraffic / deltaDays
		).ifPresent(
			variant::setEstimatedTrafficRate
		);
	}

	private void _setVariantSuccessAndTrials(Variant<Double> variant) {
		long successes = 0;
		long trials = 0;

		List<ExperimentDataPoint<Double>> experimentDataPoints =
			variant.getExperimentDataPoints();

		for (ExperimentDataPoint<Double> experimentDataPoint :
				experimentDataPoints) {

			successes += experimentDataPoint.getValue();
			trials += experimentDataPoint.getTrials();
		}

		variant.setSuccesses(successes);
		variant.setTrials(trials);
	}

	private void _setVariantSuccessRate(Variant<Double> variant) {
		if (variant.getTrials() == 0) {
			variant.setSuccessRate(0);

			return;
		}

		double successRate =
			(double)variant.getSuccesses() / variant.getTrials();

		if (successRate > 1) {
			successRate = 1;
		}

		variant.setSuccessRate(successRate);
	}

	private static final double _ALPHA_PRIOR = 1.0;

	private static final double _BETA_PRIOR = 1.0;

	private static final double _GROUP_WEIGHT = .25;

	@Autowired
	private ExperimentDataDog _experimentDataDog;

}