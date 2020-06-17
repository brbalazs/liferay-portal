import * as d3 from 'd3';
import React from 'react';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {CHART_COLORS} from 'shared/components/Chart';
import {
	dateFormatter,
	formatHistogramKeyValue,
	formatYAxis,
	getFormattedVariantHistogram,
	normalizeHistogram,
	TOOLTIP_METRICS
} from 'experiments/util/experiments';

const CONTROL_COLOR = '#6B6C7E';

export default metricUnit => ({experiment}) => {
	if (!experiment.dxpVariants || experiment.dxpVariants.length === 0) {
		return {
			empty: true
		};
	}

	const normalizedHistogram = normalizeHistogram(experiment);

	const variantsKeyValue = formatHistogramKeyValue(
		normalizedHistogram,
		metricUnit
	);

	const histograms = normalizedHistogram.map(
		({control, dxpVariantName, variantsHistogram}, index) => {
			const {value} = getFormattedVariantHistogram(variantsHistogram);

			return {
				color: control && CONTROL_COLOR,
				data: value,
				id: `data${index + 1}`,
				label: dxpVariantName
			};
		}
	);

	const {key} = getFormattedVariantHistogram(
		normalizedHistogram[0].variantsHistogram
	);

	return {
		data: [
			...histograms,
			{
				data: key,
				id: 'x'
			}
		],
		format: formatYAxis(metricUnit),
		intervals: key,
		Tooltip: ({dataPoint}) => {
			const date = dateFormatter(dataPoint[0].x);
			const variant = variantsKeyValue[dataPoint[0].id][date];

			let header: Array<Object>;
			let rows: Array<Object>;

			if (dataPoint.length > 1) {
				header = [
					{
						label: `${Liferay.Language.get(
							'variants'
						)} - ${d3.utcFormat('%m/%d/%Y')(dataPoint[0].x)}`,
						weight: 'semibold',
						width: 100
					},
					...TOOLTIP_METRICS.map(({title}) => ({
						align: 'right',
						label: title,
						weight: 'semibold',
						width: 60
					}))
				];

				rows = dataPoint.map(point => {
					const variant = variantsKeyValue[point.id][date];
					const colorPosition =
						parseInt(point.id.substr(point.id.length - 1)) - 2;
					const color = variant.control
						? CONTROL_COLOR
						: CHART_COLORS[colorPosition];

					return {
						columns: [
							{
								color,
								label: variant.name,
								truncated: true
							},
							...TOOLTIP_METRICS.map(
								({accessor, dataRenderer}) => ({
									align: 'right',
									className: 'align-items-end',
									label: dataRenderer
										? dataRenderer(
												variantsKeyValue[point.id][date]
										  )
										: variantsKeyValue[point.id][date][
												accessor
										  ],
									weight: 'semibold'
								})
							)
						]
					};
				});
			} else {
				header = [
					{
						label: `${variant.name} - ${d3.utcFormat('%m/%d/%Y')(
							dataPoint[0].x
						)}`,
						weight: 'semibold'
					},
					{
						label: ''
					}
				];

				rows = TOOLTIP_METRICS.map(
					({accessor, dataRenderer, title}) => ({
						columns: [
							{
								label: title
							},
							{
								align: 'right',
								label: dataRenderer
									? dataRenderer(
											variantsKeyValue[dataPoint[0].id][
												date
											]
									  )
									: variantsKeyValue[dataPoint[0].id][date][
											accessor
									  ],
								weight: 'semibold'
							}
						]
					})
				);
			}

			return <TooltipChart header={header} rows={rows} />;
		},
		tooltipConfig: {
			grouped: false
		},
		type: 'area'
	};
};
