import BasePage from 'settings/components/BasePage';
import OutputVersionsCard from '../components/OutputVersionsCard';
import React from 'react';
import RecommendationJobRunsQuery from '../queries/RecommendationJobRunsQuery';
import TrainingItemsCard from '../components/TrainingItemsCard';
import withRecommendation from 'shared/hoc/WithRecommendation';
import {addAlert} from 'shared/actions/alerts';
import {Alert, Modal, RouterType} from 'shared/types';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {Filter, Job} from '../utils/utils';
import {get} from 'lodash';
import {getOperationName} from 'apollo-link';
import {getRecommendations} from 'shared/util/breadcrumbs';
import {
	RECOMMENDATION_DELETE_MUTATION,
	RECOMMENDATION_RUN_MUTATION
} from '../queries/RecommendationMutation';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useMutation} from '@apollo/react-hooks';
import {User} from 'shared/util/records';
import {withCurrentUser, withHistory} from 'shared/hoc';

interface IViewProps {
	addAlert: Alert.AddAlert;
	close: Modal.close;
	currentUser: User;
	history: {
		push: (string) => void;
	};
	job: Job;
	open: Modal.open;
	router: RouterType;
}

const View: React.FC<IViewProps> = ({
	addAlert,
	close,
	currentUser,
	history,
	job,
	open,
	router
}) => {
	const {groupId, jobId} = router.params;

	const [deleteRecommendationJobs] = useMutation(
		RECOMMENDATION_DELETE_MUTATION
	);
	const [runRecommendationJob] = useMutation(RECOMMENDATION_RUN_MUTATION);

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
					pageActions={
						currentUser.isAdmin()
							? [
									{
										label: Liferay.Language.get('retrain'),
										onClick: () => {
											open(
												modalTypes.MANUALLY_RETRAIN_MODEL_MODAL,
												{
													job,
													onClose: close,
													onSubmit: ({
														trainingPeriod
													}) => {
														runRecommendationJob({
															awaitRefetchQueries: true,
															refetchQueries: [
																getOperationName(
																	RecommendationJobRunsQuery
																)
															],
															variables: {
																jobId,
																trainingPeriod
															}
														})
															.then(() => {
																addAlert({
																	alertType:
																		Alert
																			.Types
																			.SUCCESS,
																	message: Liferay.Language.get(
																		'retraining-has-been-started'
																	)
																});

																close();
															})
															.catch(() => {
																addAlert({
																	alertType:
																		Alert
																			.Types
																			.ERROR,
																	message: Liferay.Language.get(
																		'there-was-an-error-processing-your-request.-please-try-again'
																	),
																	timeout: false
																});
															});
													},
													trainingPeriod: get(
														job,
														'trainingPeriod'
													)
												}
											);
										}
									},
									{
										href: toRoute(
											Routes.SETTINGS_RECOMMENDATION_EDIT,
											{
												groupId,
												jobId
											}
										),
										label: Liferay.Language.get('edit')
									},
									{
										label: Liferay.Language.get('delete'),
										onClick: () => {
											open(
												modalTypes.CONFIRMATION_MODAL,
												{
													message: (
														<div>
															<h4 className='text-secondary'>
																{sub(
																	Liferay.Language.get(
																		'delete-x-and-its-historical-training-output-data'
																	),
																	[name]
																)}
															</h4>

															<p>
																{Liferay.Language.get(
																	'components-using-this-model-will-need-to-be-reconfigured'
																)}
															</p>
														</div>
													),
													modalVariant:
														'modal-warning',
													onClose: close,
													onSubmit: () => {
														deleteRecommendationJobs(
															{
																variables: {
																	jobIds: [
																		jobId
																	]
																}
															}
														)
															.then(() => {
																addAlert({
																	alertType:
																		Alert
																			.Types
																			.SUCCESS,
																	message: sub(
																		Liferay.Language.get(
																			'x-has-been-deleted'
																		),
																		[name]
																	) as string
																});

																history.push(
																	toRoute(
																		Routes.SETTINGS_RECOMMENDATIONS,
																		{
																			groupId
																		}
																	)
																);
															})
															.catch(() => {
																addAlert({
																	alertType:
																		Alert
																			.Types
																			.ERROR,
																	message: Liferay.Language.get(
																		'there-was-an-error-processing-your-request.-please-try-again'
																	),
																	timeout: false
																});
															});
													},
													submitButtonDisplay:
														'warning',
													submitMessage: Liferay.Language.get(
														'delete'
													),
													title: sub(
														Liferay.Language.get(
															'deleting-x'
														),
														[name]
													),
													titleIcon: 'warning-full'
												}
											);
										}
									}
							  ]
							: []
					}
					pageActionsDisplayLimit={3}
					pageTitle={name}
				>
					<OutputVersionsCard
						nextTrainingDate={get(job, 'nextTrainingDate')}
						router={router}
						trainingFrequency={get(job, 'trainingFrequency')}
					/>
					<TrainingItemsCard itemFilters={itemFilters} />
				</BasePage>
			</div>
		</div>
	);
};

export default compose<any>(
	withRecommendation,
	withHistory,
	withCurrentUser,
	connect(
		null,
		{addAlert, close, open}
	)
)(View);
