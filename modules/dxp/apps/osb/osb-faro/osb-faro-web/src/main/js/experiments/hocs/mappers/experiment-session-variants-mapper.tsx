import * as d3 from 'd3';
import React from 'react';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {CHART_COLORS} from 'shared/components/Chart';
import {getAxisFormatter, getIntervals} from 'shared/util/charts';
import {
	getFormattedHistogram,
	toThousandsABTesting
} from 'experiments/util/experiments';

const CONTROL_COLOR = '#6B6C7E';

export default ({experiment}) => {
	if (!experiment.dxpVariants || experiment.dxpVariants.length === 0) {
		return {
			empty: true
		};
	}

	const histograms = experiment.dxpVariants.map(
		({control, dxpVariantName, sessionsHistogram}, index) => {
			const {value} = getFormattedHistogram(sessionsHistogram);

			return {
				color: control && CONTROL_COLOR,
				data: value,
				id: `data${index + 1}`,
				label: dxpVariantName
			};
		}
	);

	const {key} = getFormattedHistogram(
		experiment.dxpVariants[0].sessionsHistogram
	);

	return {
		data: [
			...histograms,
			{
				data: key,
				id: 'x'
			}
		],
		format: getAxisFormatter('number'),
		intervals: getIntervals(null, key, null),
		Tooltip: ({dataPoint}) => {
			const COLORS = [...CHART_COLORS];

			const header = [
				{
					label: d3.utcFormat('%-d %b')(dataPoint[0].x),
					weight: 'semibold'
				},
				{
					label: Liferay.Language.get('sessions'),
					weight: 'semibold'
				}
			];

			const rows = dataPoint
				.map(({id, value}) => {
					const {
						control,
						dxpVariantName
					} = experiment.dxpVariants.find(
						(variant, index) => id === `data${index + 1}`
					);

					return {
						columns: [
							{
								color: control ? CONTROL_COLOR : COLORS.shift(),
								label: dxpVariantName
							},
							{
								align: 'right',
								label: toThousandsABTesting(value)
							}
						]
					};
				})
				.sort((a, b) => b.columns[1].label - a.columns[1].label);

			return <TooltipChart header={header} rows={rows} />;
		}
	};
};
