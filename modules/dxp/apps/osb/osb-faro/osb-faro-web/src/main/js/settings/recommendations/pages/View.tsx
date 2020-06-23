import BasePage from 'settings/components/BasePage';
import React from 'react';
import withRecommendation from 'shared/hoc/WithRecommendation';
import {get} from 'lodash';
import {getRecommendations} from 'shared/util/breadcrumbs';
import {Job} from '../utils/utils';
import {RouterType} from 'shared/types';
import {Routes, toRoute} from 'shared/util/router';

interface IViewProps {
	job: Job;
	router: RouterType;
}

const View: React.FC<IViewProps> = ({job, router}) => {
	const {groupId, jobId} = router.params;

	const name = get(job, 'name');

	return (
		<BasePage
			breadcrumbItems={[
				getRecommendations({groupId}),
				{
					active: true,
					label: name
				}
			]}
			groupId={groupId}
			pageActions={[
				{
					label: Liferay.Language.get('retrain')
				},
				{
					href: toRoute(Routes.SETTINGS_RECOMMENDATION_EDIT, {
						groupId,
						jobId
					}),
					label: Liferay.Language.get('edit')
				},
				{
					label: Liferay.Language.get('delete')
				}
			]}
			pageActionsDisplayLimit={3}
			pageTitle={name}
		>
			{'test'}
		</BasePage>
	);
};

export default withRecommendation(View);
