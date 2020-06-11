import BasicSettings from './BasicSettings';
import Card from 'shared/components/Card';
import Form from 'shared/components/form';
import FormNavigation from 'settings/components/FormNavigation';
import Interactions from './Interactions';
import Items from './Items';
import NavigationWarning from 'shared/components/NavigationWarning';
import ProgressTimeline from 'shared/components/ProgressTimeline';
import React, {useState} from 'react';
import {Job, JobParameter} from '../../utils/utils';
import {
	jobTrainingFrequencies,
	jobTrainingPeriods
} from 'shared/util/constants';
import {RouterType} from 'shared/types';

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
		component: () => <div>{'step 4'}</div>,
		title: Liferay.Language.get('summary')
	}
];

interface IRecommendationStepCardProps {
	cancelHref: string;
	job?: Job;
	router: RouterType;
}

const RecommendationStepCard: React.FC<IRecommendationStepCardProps> = ({
	cancelHref,
	job
}) => {
	const [currentStep, setCurrentStep] = useState(0);
	const [disabled, setDisabled] = useState(true);

	const StepComponent = STEPS[currentStep].component;

	const handleNext = event => {
		event.preventDefault();

		setCurrentStep(currentStep + 1);
	};

	const handleSubmit = ({
		includePreviousPeriod,
		itemFilters,
		name,
		trainingFrequency,
		trainingPeriod
	}) => {
		// eslint-disable-next-line no-console
		console.log({
			name,
			parameters: [
				{name: 'includePreviousPeriod', value: includePreviousPeriod},
				...itemFilters.map(({name, value}) => ({name, value}))
			],
			trainingFrequency,
			trainingPeriod
		});

		// TODO: Add submission
	};

	const getInitialValuesFromJob = () => {
		if (job) {
			const {name, parameters, trainingFrequency, trainingPeriod} = job;

			const includePreviousPeriodParameter: JobParameter = parameters.find(
				({name}) => name === 'includePreviousPeriod'
			);

			const itemFilters = parameters.reduce((acc, {name, value}) => {
				if (name === 'includePreviousPeriod') {
					return acc;
				}

				return [
					...acc,
					{count: null, id: `${name} - ${value}`, name, value}
				];
			}, []);

			return {
				includePreviousPeriod:
					includePreviousPeriodParameter &&
					includePreviousPeriodParameter.value,
				itemFilters,
				name,
				trainingFrequency,
				trainingPeriod
			};
		}

		return {
			includePreviousPeriod: false,
			itemFilters: [],
			name: '',
			trainingFrequency: jobTrainingFrequencies.every7Days,
			trainingPeriod: jobTrainingPeriods.last30Days
		};
	};

	const lastStep = currentStep === STEPS.length - 1;

	return (
		<Card className='recommendation-step-card-root'>
			<Form
				initialValues={getInitialValuesFromJob()}
				onSubmit={handleSubmit}
			>
				{({dirty, errors, handleSubmit, isSubmitting, values}) => (
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
								disabled={disabled}
								errors={errors}
								onSetDisabled={setDisabled}
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
								submitMessage={
									lastStep
										? Liferay.Language.get('done')
										: Liferay.Language.get('next')
								}
							/>
						</Card.Footer>
					</Form.Form>
				)}
			</Form>
		</Card>
	);
};

export default RecommendationStepCard;
