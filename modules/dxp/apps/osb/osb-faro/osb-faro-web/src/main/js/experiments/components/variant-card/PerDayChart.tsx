import BasePage from 'shared/components/base-page';
import getPerDayMapper from 'experiments/hocs/mappers/experiment-variant-perday-mapper';
import LineChart from 'experiments/components/LineChart';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React, {useContext} from 'react';
import {EXPERIMENT_VARIANTS_HISTOGRAM_QUERY} from 'experiments/queries/ExperimentQuery';
import {getMetricUnit} from 'experiments/util/experiments';
import {SafeResults} from 'shared/hoc/util';
import {useQuery} from '@apollo/react-hooks';

const getMetricUnitFromGoal = ({experiment: {goal}}) => {
	const metric = goal ? goal.metric : null;
	const metricUnit = getMetricUnit(metric);

	return metricUnit;
};

const perDayChart = () => {
	const {
		router: {
			params: {id: experimentId}
		}
	} = useContext(BasePage.Context);

	const result = useQuery(EXPERIMENT_VARIANTS_HISTOGRAM_QUERY, {
		variables: {experimentId}
	});

	return (
		<SafeResults {...result}>
			{props => {
				const metricUnit = getMetricUnitFromGoal(props);

				const {
					data,
					empty,
					format,
					intervals,
					Tooltip,
					tooltipConfig,
					type
				} = getPerDayMapper(metricUnit)(props);

				return empty ? (
					<NoResultsDisplay
						title={Liferay.Language.get('sorry-an-error-occurred')}
					/>
				) : (
					<LineChart
						data={data}
						format={format}
						intervals={intervals}
						Tooltip={Tooltip}
						tooltipConfig={tooltipConfig}
						type={type}
					/>
				);
			}}
		</SafeResults>
	);
};

export default perDayChart;
