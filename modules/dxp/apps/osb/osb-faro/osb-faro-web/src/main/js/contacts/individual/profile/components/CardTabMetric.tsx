import MetricValue from 'cerebro-shared/components/MetricValue';
import React from 'react';
import Trend from 'cerebro-shared/components/Trend';
import {getMetricFormatter} from 'shared/util/charts';
import {getStatsColor} from 'shared/util/metrics';
import {toRounded} from 'shared/util/numbers';

export enum MetricValueType {
	Number = 'number',
	Percentage = 'percentage',
	Time = 'time',
	Engagement = 'engagement',
	Ratings = 'ratings'
}

interface ICardTabMetricProps extends React.HTMLAttributes<HTMLElement> {
	change: number;
	type: MetricValueType;
	value: number;
}

const CardTabMetric: React.FC<ICardTabMetricProps> = ({
	change,
	type,
	value
}) => {
	const formatter = getMetricFormatter(type);
	const icon =
		change === 0 ? undefined : change < 0 ? 'caret-bottom' : 'caret-top';
	const trendClassification =
		change === 0 ? 'NEUTRAL' : change < 0 ? 'NEGATIVE' : 'POSITIVE';

	const color = getStatsColor(trendClassification);
	const changeLabel = `${toRounded(Math.abs(change))}%`;

	return (
		<span>
			<span className='primary-content'>
				<MetricValue type={type} value={formatter(value)} />
			</span>

			{changeLabel && (
				<Trend color={color} icon={icon} label={changeLabel} />
			)}
		</span>
	);
};

export default CardTabMetric;
