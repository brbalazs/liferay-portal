import BasePage from 'shared/components/base-page';
import getPerDayMapper from 'experiments/hocs/mappers/experiment-variant-perday-mapper';
import LineChart from 'experiments/components/LineChart';
import React, {useContext} from 'react';
import WrappedSafeComponent from 'cerebro-shared/hocs/WrappedSafeComponent';
import {EXPERIMENT_VARIANTS_HISTOGRAM_QUERY} from 'experiments/queries/ExperimentQuery';
import {useQuery} from '@apollo/react-hooks';

interface perDayChartIProps extends React.HTMLAttributes<HTMLElement> {
	metricUnit: string;
}

const perDayChart: React.FC<perDayChartIProps> = ({metricUnit}) => {
	const {
		router: {
			params: {id: experimentId}
		}
	} = useContext(BasePage.Context);

	const {data, error, loading} = useQuery(
		EXPERIMENT_VARIANTS_HISTOGRAM_QUERY,
		{
			variables: {experimentId}
		}
	);

	return (
		<WrappedSafeComponent
			data={data}
			error={error}
			loading={loading}
			mapper={getPerDayMapper(metricUnit)}
		>
			{props => <LineChart {...props} />}
		</WrappedSafeComponent>
	);
};

export default perDayChart;
