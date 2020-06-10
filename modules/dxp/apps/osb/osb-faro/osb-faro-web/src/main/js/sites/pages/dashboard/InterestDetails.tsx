import BackButton from 'contacts/components/BackButton';
import InterestDetails from 'sites/hocs/InterestDetails';
import React from 'react';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {pickBy} from 'lodash';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';

interface IInterestDetailsProps extends React.HTMLAttributes<HTMLDivElement> {
	router: {
		params: {
			channelId: string;
			groupId: string;
		};
		query: {
			rangeKey: string;
		};
	};
}

export default class InterestDetailsPage extends React.Component<
	IInterestDetailsProps
> {
	render() {
		const {router} = this.props;

		const {
			params: {channelId, groupId},
			query
		} = router;

		const rangeSelectors = getRangeSelectorsFromQuery(query);

		return (
			<div className='sites-dashboard-interest-details-root'>
				<div className='row'>
					<div className='col-xl-12'>
						<BackButton
							href={setUriQueryValues(
								pickBy({...rangeSelectors}),

								toRoute(Routes.SITES_INTERESTS, {
									channelId,
									groupId
								})
							)}
							label={Liferay.Language.get('back-to-interests')}
						/>

						<InterestDetails router={router} />
					</div>
				</div>
			</div>
		);
	}
}
