import BasePage from 'settings/components/BasePage';
import React from 'react';
import RecommendationStepCard from '../components/recommendation-step-card';
import withRecommendation from 'shared/hoc/WithRecommendation';
import {compose} from 'redux';
import {get} from 'lodash';
import {Job} from '../utils/utils';
import {RouterType} from 'shared/types';
import {Routes, toRoute} from 'shared/util/router';
import {withAdminPermission} from 'shared/hoc';

interface IEditProps {
	job: Job;
	router: RouterType;
}

const Edit: React.FC<IEditProps> = ({job, router}) => {
	const {groupId} = router.params;

	return (
		<BasePage
			groupId={groupId}
			pageDescription={Liferay.Language.get(
				'item-similarity-model-uses-items-and-iteractions-for-training'
			)}
			pageTitle={get(job, 'name')}
		>
			<div className='row'>
				<div className='col-xl-8'>
					<RecommendationStepCard
						cancelHref={toRoute(Routes.SETTINGS_RECOMMENDATIONS, {
							groupId
						})}
						job={job}
						router={router}
					/>
				</div>
			</div>
		</BasePage>
	);
};

export default compose<any>(
	withAdminPermission,
	withRecommendation
)(Edit);
