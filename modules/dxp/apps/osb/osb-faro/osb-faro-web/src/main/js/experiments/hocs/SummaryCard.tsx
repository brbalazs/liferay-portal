import BasePage from 'shared/components/base-page';
import getSummaryMapper from 'experiments/hocs/mappers/experiment-summary-mapper';
import React, {useContext} from 'react';
import SummaryCardDraft from 'experiments/components/summary-card-draft';
import SummaryCardRun from 'experiments/components/summary-card-run';
import WrappedSafeComponent from 'cerebro-shared/hocs/WrappedSafeComponent';
import {DocumentNode} from 'graphql';
import {
	EXPERIMENT_DRAFT_QUERY,
	EXPERIMENT_QUERY
} from 'experiments/queries/ExperimentQuery';
import {useAddRefetch} from 'experiments/util/experiments';
import {useQuery} from '@apollo/react-hooks';

interface IWithSummaryCard extends React.HTMLAttributes<HTMLElement> {
	status?: string;
	timeZoneId: string;
}

const withSummaryCard: React.FC<IWithSummaryCard> = ({status, timeZoneId}) => {
	const {
		router: {
			params: {id: experimentId}
		}
	} = useContext(BasePage.Context);

	let query: DocumentNode = null;
	let Component: React.FC = null;

	if (status === 'DRAFT') {
		query = EXPERIMENT_DRAFT_QUERY;
		Component = SummaryCardDraft;
	} else {
		query = EXPERIMENT_QUERY;
		Component = SummaryCardRun;
	}

	const {data, refetch, ...result} = useQuery(query, {
		variables: {experimentId}
	});

	useAddRefetch(refetch);

	return (
		<WrappedSafeComponent
			{...result}
			data={{
				...data,
				experimentId,
				timeZoneId
			}}
			mapper={getSummaryMapper}
		>
			{props => <Component {...props} />}
		</WrappedSafeComponent>
	);
};

export default withSummaryCard;
