import BarChartHTML from 'cerebro-shared/components/BarChartHTML';
import BasePage from 'shared/components/base-page';
import getMedianMapper from 'experiments/hocs/mappers/experiment-variant-median-mapper';
import Legend from 'cerebro-shared/components/Legend';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React, {useContext} from 'react';
import {EXPERIMENT_QUERY} from 'experiments/queries/ExperimentQuery';
import {SafeResults} from 'shared/hoc/util';
import {useQuery} from '@apollo/react-hooks';

const mediansChart = () => {
	const {
		router: {
			params: {id: experimentId}
		}
	} = useContext(BasePage.Context);

	const result = useQuery(EXPERIMENT_QUERY, {
		variables: {experimentId}
	});

	return (
		<SafeResults {...result}>
			{props => {
				const {empty, legend, mediansData} = getMedianMapper(props);

				return empty ? (
					<NoResultsDisplay
						icon={{
							border: false,
							size: 'xl',
							symbol: 'ac-chart'
						}}
					>
						<div>
							{Liferay.Language.get(
								'we-are-currently-collecting-data'
							)}
						</div>
						<div>
							{Liferay.Language.get(
								'metrics-will-show-once-there-are-visitors-to-your-variants'
							)}
						</div>
					</NoResultsDisplay>
				) : (
					<>
						<BarChartHTML {...mediansData} />
						<Legend data={legend} />
					</>
				);
			}}
		</SafeResults>
	);
};

export default mediansChart;
