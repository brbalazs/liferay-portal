import Form, {
	validateMaxLength,
	validateRequired
} from 'shared/components/form';
import React, {useEffect} from 'react';
import {FormikErrors} from 'formik';
import {Job} from '../../utils/utils';
import {JOB_TRAINING_FREQUENCIES_LIST} from '../../utils/utils';
import {sequence} from 'shared/util/promise';

interface IBasicSettingsProps {
	disabled: boolean;
	errors: FormikErrors<Job>;
	name: string;
	onSetDisabled: (disabled: boolean) => void;
}

const BasicSettings: React.FC<IBasicSettingsProps> = ({
	errors,
	name,
	onSetDisabled
}) => {
	useEffect(() => {
		onSetDisabled(!name || !!errors.name);
	}, [name, errors]);

	return (
		<div className='basic-settings-root'>
			<Form.Group>
				<Form.Input
					label={Liferay.Language.get('model-name')}
					name='name'
					required
					validate={sequence([
						validateRequired,
						validateMaxLength(255)
					])}
				/>
			</Form.Group>

			<Form.Group>
				<Form.Select
					label={Liferay.Language.get('training-frequency')}
					name='trainingFrequency'
				>
					{JOB_TRAINING_FREQUENCIES_LIST.map(({name, value}) => (
						<Form.Select.Item key={value} value={value}>
							{name}
						</Form.Select.Item>
					))}
				</Form.Select>
			</Form.Group>
		</div>
	);
};

export default BasicSettings;
