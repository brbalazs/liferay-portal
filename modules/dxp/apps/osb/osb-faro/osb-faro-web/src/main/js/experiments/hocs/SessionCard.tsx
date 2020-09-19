import BasePage from 'shared/components/base-page';
import getSessionMapper from 'experiments/hocs/mappers/experiment-session-mapper';
import getSessionVariantsMapper from 'experiments/hocs/mappers/experiment-session-variants-mapper';
import LineChart from 'experiments/components/LineChart';
import React, {useContext} from 'react';
import SessionCard from 'experiments/components/SessionCard';
import WrappedSafeComponent from 'cerebro-shared/hocs/WrappedSafeComponent';
import {DocumentNode} from 'graphql';
import {
	EXPERIMENT_SESSION_HISTOGRAM_QUERY,
	EXPERIMENT_SESSION_VARIANTS_HISTOGRAM_QUERY
} from 'experiments/queries/ExperimentQuery';
import {useFakeLoading} from 'shared/hooks';
import {useQuery} from '@apollo/react-hooks';
import {useStateValue} from 'experiments/state';

interface IWithSessionCardProps extends React.HTMLAttributes<HTMLElement> {
	label?: string;
}

const withSessionCard: React.FC<IWithSessionCardProps> = ({label}) => {
	const [{sessionViewTriggered}]: any = useStateValue();
	const {
		router: {
			params: {id: experimentId}
		}
	} = useContext(BasePage.Context);

	let query: DocumentNode = null;
	let mapper: Function = null;

	if (sessionViewTriggered === 'total') {
		query = EXPERIMENT_SESSION_HISTOGRAM_QUERY;
		mapper = getSessionMapper;
	} else if (sessionViewTriggered === 'per-variant') {
		query = EXPERIMENT_SESSION_VARIANTS_HISTOGRAM_QUERY;
		mapper = getSessionVariantsMapper;
	}

	const {data, error, loading} = useQuery(query, {
		variables: {experimentId}
	});

	const fakeLoading = useFakeLoading(data);

	return (
		<SessionCard label={label}>
			<WrappedSafeComponent
				data={data}
				error={error}
				loading={loading || fakeLoading}
				mapper={mapper}
			>
				{props => <LineChart {...props} />}
			</WrappedSafeComponent>
		</SessionCard>
	);
};

export default withSessionCard;
