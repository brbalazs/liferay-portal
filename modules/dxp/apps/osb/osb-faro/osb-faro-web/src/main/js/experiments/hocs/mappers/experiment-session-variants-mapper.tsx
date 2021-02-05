import React from 'react';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {CHART_COLORS} from 'shared/components/Chart';
import {getAxisFormatter} from 'shared/util/charts';
import {getFormattedDataTooltip} from 'experiments/util/experiments';

const CONTROL_COLOR = '#6B6C7E';

export default ({experiment}) => {
	if (!experiment.dxpVariants || experiment.dxpVariants.length === 0) {
		return {
			empty: true
		};
	}

	const data = experiment.dxpVariants.map(
		({control, dxpVariantName, sessionsHistogram}, index) => ({
			color: control ? CONTROL_COLOR : CHART_COLORS[index - 1],
			data: sessionsHistogram,
			name: dxpVariantName
		})
	);

	return {
		data,
		format: getAxisFormatter('number'),
		intervals: experiment.dxpVariants[0].sessionsHistogram.map(
			({key}) => key
		),
		Tooltip: ({dataPoint}) => (
			<div className='bb-tooltip-container position-static'>
				<TooltipChart {...getFormattedDataTooltip(dataPoint)} />
			</div>
		)
	};
};
