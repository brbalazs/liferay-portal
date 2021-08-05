import BackButton from 'contacts/components/BackButton';
import InterestDetails from 'shared/components/InterestDetails';
import React from 'react';
import {isNil, pickBy} from 'lodash';
import {Router} from 'shared/types';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';

interface IInterestDetailsProps extends React.HTMLAttributes<HTMLDivElement> {
	router: Router;
}

const InterestDetailsPage: React.FC<IInterestDetailsProps> = ({router}) => {
	const {
		params: {channelId, groupId},
		query: {rangeKey}
	} = router;

	return (
		<div className='individuals-dashboard-interest-details-root'>
			<div className='row'>
				<div className='col-xl-12'>
					<BackButton
						href={setUriQueryValues(
							pickBy({rangeKey}, param => !isNil(param)),

							toRoute(Routes.CONTACTS_INDIVIDUALS_INTERESTS, {
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
};

export default InterestDetailsPage;
