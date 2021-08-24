import * as API from 'shared/api';
import Distribution, {CONTEXT_OPTIONS} from 'contacts/components/Distribution';
import React from 'react';
import {compose, withQuery} from 'shared/hoc';
import {connect, ConnectedProps} from 'react-redux';
import {
	fetchIndividualsDistribution,
	INDIVIDUALS_DASHBOARD_DISTRUBTIONS_KEY
} from 'shared/actions/distributions';
import {get} from 'lodash';

const connector = connect(null, {
	fetchDistribution: fetchIndividualsDistribution
});

type PropsFromRedux = ConnectedProps<typeof connector>;

interface IIndividualsDistributionProps extends PropsFromRedux {
	knownIndividualCount: number | null;
	router: object;
}

export const IndividualsDistribution: React.FC<IIndividualsDistributionProps> = ({
	knownIndividualCount,
	...otherProps
}) => (
	<div className='individuals-dashboard-distribution-root container-fluid'>
		<div className='row'>
			<div className='col-xl-12'>
				<Distribution
					contextOptions={[CONTEXT_OPTIONS[0]]}
					distributionsKey={INDIVIDUALS_DASHBOARD_DISTRUBTIONS_KEY}
					knownIndividualCount={knownIndividualCount}
					{...otherProps}
				/>
			</div>
		</div>
	</div>
);

export default compose<any>(
	withQuery(
		({channelId, groupId}) =>
			API.individuals.search({
				channelId,
				groupId,
				includeAnonymousUsers: false
			}),
		val => val,
		({data, error}) => ({
			knownIndividualCount: error ? 0 : get(data, 'total', null)
		})
	),
	connector
)(IndividualsDistribution);
