import BasicSettings from './BasicSettings';
import Card from 'shared/components/Card';
import Form from 'shared/components/form';
import FormNavigation from 'settings/components/FormNavigation';
import Interactions from './Interactions';
import Items from './Items';
import NavigationWarning from 'shared/components/NavigationWarning';
import ProgressTimeline from 'shared/components/ProgressTimeline';
import React, {useState} from 'react';
import Summary from './Summary';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {Filter, Job, JobParameter} from '../../utils/utils';
import {
	jobRunDataPeriods,
	jobRunFrequencies,
	jobTypes
} from 'shared/util/constants';
import {
	RECOMMENDATION_MUTATION,
	RECOMMENDATION_UPDATE_MUTATION
} from '../../queries/RecommendationMutation';
import {RouterType} from 'shared/types';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useMutation} from '@apollo/react-hooks';
import {withHistory} from 'shared/hoc';

const STEPS = [
	{
		component: BasicSettings,
		title: Liferay.Language.get('basic-settings')
	},
	{
		component: Interactions,
		title: Liferay.Language.get('interactions')
	},
	{
		component: Items,
		title: Liferay.Language.get('items')
	},
	{
		component: Summary,
		title: Liferay.Language.get('summary')
	}
];

interface IRecommendationStepCardProps {
	addAlert: Alert.AddAlert;
	cancelHref: string;
	history: {
		push: (string) => void;
	};
	job?: Job;
	jobType?: jobTypes;
	router: RouterType;
}

const RecommendationStepCard: React.FC<IRecommendationStepCardProps> = ({
	addAlert,
	cancelHref,
	history,
	job,
	jobType,
	router
}) => {
	const {
		params: {groupId}
	} = router;

	const [currentStep, setCurrentStep] = useState(0);
	const [disabled, setDisabled] = useState(true);

	const [createRecommendationJob] = useMutation(RECOMMENDATION_MUTATION);
	const [updateRecommendationJob] = useMutation(
		RECOMMENDATION_UPDATE_MUTATION
	);

	const handleNext = event => {
		event.preventDefault();

		setCurrentStep(currentStep + 1);
	};

	const handleSubmit = ({
		includePreviousPeriod,
		itemFilters,
		name,
		runDataPeriod,
		runFrequency,
		runNow,
		type
	}) => {
		let parameters = [
			{
				name: 'includePreviousPeriod',
				value: includePreviousPeriod
			}
		];

		if (itemFilters.length) {
			parameters = [
				{
					name: 'includePreviousPeriod',
					value: includePreviousPeriod
				},
				...itemFilters.map(({name, value}) => ({name, value}))
			];
		}

		const mutationFn = job
			? () =>
					updateRecommendationJob({
						variables: {
							jobId: job.id,
							name,
							parameters,
							runDataPeriod,
							runFrequency,
							runNow
						}
					})
			: () =>
					createRecommendationJob({
						variables: {
							name,
							parameters,
							runDataPeriod,
							runFrequency,
							runNow,
							type
						}
					});

		const key = job ? 'updateJob' : 'createJob';

		mutationFn()
			.then(({data: {[key]: {name}}}) => {
				addAlert({
					alertType: Alert.Types.SUCCESS,
					message: sub(
						Liferay.Language.get(
							'x-recommendation-model-has-been-saved'
						),
						[name]
					) as string
				});

				history.push(
					toRoute(Routes.SETTINGS_RECOMMENDATIONS, {
						groupId
					})
				);
			})
			.catch(() => {
				addAlert({
					alertType: Alert.Types.ERROR,
					message: Liferay.Language.get(
						'there-was-an-error-processing-your-request.-please-try-again'
					)
				});
			});
	};

	const getInitialValues = () => {
		if (job) {
			const {
				id,
				name,
				parameters,
				runDataPeriod,
				runFrequency,
				type
			} = job;

			const includePreviousPeriodParameter: JobParameter = parameters.find(
				({name}) => name === 'includePreviousPeriod'
			);

			const itemFilters: Filter[] = parameters.reduce(
				(acc, {name, value}) => {
					if (name === 'includePreviousPeriod') {
						return acc;
					}

					return [...acc, {id: `${name} - ${value}`, name, value}];
				},
				[]
			);

			return {
				id,
				includePreviousPeriod:
					includePreviousPeriodParameter &&
					includePreviousPeriodParameter.value,
				itemFilters,
				name,
				runDataPeriod,
				runFrequency,
				runNow: true,
				type
			};
		}

		return {
			id: null,
			includePreviousPeriod: false,
			itemFilters: [],
			name: '',
			runDataPeriod: jobRunDataPeriods.last30Days,
			runFrequency: jobRunFrequencies.every7Days,
			runNow: true,
			type: jobType
		};
	};

	const getSubmitMessage = (lastStep: boolean, runNow: boolean): string => {
		if (lastStep) {
			if (runNow) {
				return Liferay.Language.get('save-and-train');
			}

			return Liferay.Language.get('save');
		}

		return Liferay.Language.get('next');
	};

	const StepComponent = STEPS[currentStep].component;

	const lastStep = currentStep === STEPS.length - 1;

	return (
		<Card className='recommendation-step-card-root'>
			<Form initialValues={getInitialValues()} onSubmit={handleSubmit}>
				{({
					dirty,
					errors,
					handleSubmit,
					initialValues,
					isSubmitting,
					setFieldValue,
					values
				}) => (
					<Form.Form>
						<NavigationWarning when={dirty && !isSubmitting} />

						<Card.Header>
							<ProgressTimeline
								activeIndex={currentStep}
								items={STEPS}
							/>
						</Card.Header>

						<Card.Body>
							<StepComponent
								currentStep={currentStep}
								disabled={disabled}
								errors={errors}
								groupId={groupId}
								initialValues={initialValues}
								jobRunStatus={job ? job.status : null}
								onSetDisabled={setDisabled}
								runDate={job ? job.runDate : null}
								setFieldValue={setFieldValue}
								setStep={setCurrentStep}
								{...values}
							/>
						</Card.Body>

						<Card.Footer>
							<FormNavigation
								cancelHref={cancelHref}
								enableNext={!disabled}
								onNextStep={
									lastStep ? handleSubmit : handleNext
								}
								onPreviousStep={
									currentStep
										? () => setCurrentStep(currentStep - 1)
										: null
								}
								submitMessage={getSubmitMessage(
									lastStep,
									values.runNow
								)}
								submitting={isSubmitting}
							/>
						</Card.Footer>
					</Form.Form>
				)}
			</Form>
		</Card>
	);
};

export default compose<any>(
	withHistory,
	connect(
		null,
		{addAlert}
	)
)(RecommendationStepCard);
