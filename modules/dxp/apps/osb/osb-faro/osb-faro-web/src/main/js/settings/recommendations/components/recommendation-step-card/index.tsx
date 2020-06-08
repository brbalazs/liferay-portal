import BasicSettings from './BasicSettings';
import Card from 'shared/components/Card';
import Form from 'shared/components/form';
import FormNavigation from 'settings/components/FormNavigation';
import NavigationWarning from 'shared/components/NavigationWarning';
import ProgressTimeline from 'shared/components/ProgressTimeline';
import React, {useState} from 'react';
import {Job} from '../../utils/utils';
import {jobTrainingFrequencies} from 'shared/util/constants';
import {RouterType} from 'shared/types';

const STEPS = [
	{
		component: BasicSettings,
		title: Liferay.Language.get('basic-settings')
	},

	{
		component: () => <div>{'step 2'}</div>,
		title: Liferay.Language.get('items')
	},
	{
		component: () => <div>{'step 3'}</div>,
		title: Liferay.Language.get('interactions')
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

	const lastStep = currentStep === STEPS.length;

	const handleSubmit = () => {
		if (lastStep) {
			// TODO: Add submission
		}

		setDisabled(true);

		setCurrentStep(currentStep + 1);
	};

	const initialValues = job
		? job
		: {
				name: '',
				trainingFrequency: jobTrainingFrequencies.every7Days
		  };

	return (
		<Card className='recommendation-step-card-root'>
			<Form initialValues={initialValues} onSubmit={handleSubmit}>
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
								onNextStep={handleSubmit}
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
