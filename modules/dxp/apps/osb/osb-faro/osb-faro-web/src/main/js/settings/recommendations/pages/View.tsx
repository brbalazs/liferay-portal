import BasePage from 'settings/components/BasePage';
import OutputVersionsCard from '../components/OutputVersionsCard';
import React from 'react';
import TrainingItemsCard from '../components/TrainingItemsCard';
import withRecommendation from 'shared/hoc/WithRecommendation';
import {Filter, Job} from '../utils/utils';
import {get} from 'lodash';
import {getRecommendations} from 'shared/util/breadcrumbs';
import {RouterType} from 'shared/types';
import {Routes, toRoute} from 'shared/util/router';

interface IViewProps {
	job: Job;
	router: RouterType;
}

const View: React.FC<IViewProps> = ({job, router}) => {
	const {groupId, jobId} = router.params;

	const itemFilters: Filter[] = get(job, 'parameters', []).filter(
		({name}) => name !== 'includePreviousPeriod'
	);

	const name = get(job, 'name');

	return (
		<div className='row'>
			<div className='col-xl-8'>
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
					<OutputVersionsCard
						router={router}
						trainingFrequency={get(job, 'trainingFrequency')}
					/>
					<TrainingItemsCard itemFilters={itemFilters} />
				</BasePage>
			</div>
		</div>
	);
};

export default withRecommendation(View);
