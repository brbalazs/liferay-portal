import Button from 'shared/components/Button';
import Form, {
	validateMaxLength,
	validateRequired
} from 'shared/components/form';
import Modal from 'shared/components/modal';
import React from 'react';
import {IHelpWidgetScreenProps} from './types';
import {sequence} from 'shared/util/promise';

const ReportIssue: React.FC<IHelpWidgetScreenProps> = ({onClose, onNext}) => {
	// TODO: LRAC-7604 Connect modal form to backend
	const onSubmit = () => {
		onNext();
	};

	return (
		<>
			<Modal.Header
				onClose={onClose}
				title={Liferay.Language.get('report-an-issue')}
			/>

			<Form
				initialValues={{description: '', issueTitle: ''}}
				onSubmit={onSubmit}
			>
				{({handleSubmit, isSubmitting, isValid}) => (
					<Form.Form onSubmit={handleSubmit}>
						<Modal.Body>
							<Form.Group>
								<Form.GroupItem className='mb-4'>
									<Form.Input
										label={Liferay.Language.get(
											'issue-title'
										)}
										name='issueTitle'
										required
										validate={sequence([
											validateRequired,
											validateMaxLength(150)
										])}
									/>
								</Form.GroupItem>

								<Form.GroupItem>
									<Form.Input
										label={Liferay.Language.get(
											'description'
										)}
										name='description'
										required
										type='textarea'
										validate={validateRequired}
									/>
								</Form.GroupItem>

								<Form.GroupItem className='text-secondary'>
									{Liferay.Language.get(
										'please-include-as-many-details-as-possible'
									)}
								</Form.GroupItem>
							</Form.Group>
						</Modal.Body>

						<Modal.Footer>
							<Button display='secondary' onClick={onClose}>
								{Liferay.Language.get('cancel')}
							</Button>

							<Button
								disabled={!isValid}
								display='primary'
								loading={isSubmitting}
								type='submit'
							>
								{Liferay.Language.get('submit')}
							</Button>
						</Modal.Footer>
					</Form.Form>
				)}
			</Form>
		</>
	);
};

export default ReportIssue;
