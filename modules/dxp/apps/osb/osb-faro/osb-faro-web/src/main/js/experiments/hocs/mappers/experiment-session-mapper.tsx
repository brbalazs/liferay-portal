import * as d3 from 'd3';
import React from 'react';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {getAxisFormatter} from 'shared/util/charts';
import {
	getFormattedHistogram,
	toThousandsABTesting
} from 'experiments/util/experiments';

export default ({experiment}) => {
	if (
		!experiment.sessionsHistogram ||
		experiment.sessionsHistogram.length === 0
	) {
		return {
			empty: true
		};
	}

	const {key, value} = getFormattedHistogram(experiment.sessionsHistogram);

	return {
		data: [
			{
				data: value,
				id: 'data1',
				label: Liferay.Language.get('total')
			},
			{
				data: key,
				id: 'x'
			}
		],
		format: getAxisFormatter('number'),
		intervals: key,
		Tooltip: ({dataPoint}) => {
			const header = [
				{
					label: Liferay.Language.get('date'),
					weight: 'semibold',
					width: 120
				},
				{
					label: Liferay.Language.get('session'),
					weight: 'semibold'
				}
			];

			const rows = [
				{
					columns: [
						{
							label: d3.utcFormat('%b %-d')(dataPoint[0].x)
						},
						{
							align: 'right',
							label: toThousandsABTesting(dataPoint[0].value)
						}
					]
				}
			];

			return <TooltipChart header={header} rows={rows} />;
		}
	};
};
