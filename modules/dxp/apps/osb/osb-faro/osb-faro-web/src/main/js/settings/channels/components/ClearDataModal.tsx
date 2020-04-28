import Button from 'shared/components/Button';
import Form, {validateInputMessage} from 'shared/components/form';
import getCN from 'classnames';
import Modal from 'shared/components/modal';
import React from 'react';
import {sub} from 'shared/util/lang';

interface IClearDataModalProps extends React.HTMLAttributes<HTMLElement> {
	channelName: string;
	onClose: () => void;
	onSubmit: () => void;
	title?: string;
}

const ClearDataModal: React.FC<IClearDataModalProps> = ({
	channelName,
	className,
	onClose,
	onSubmit,
	title = Liferay.Language.get('confirm')
}) => (
	<Modal
		className={getCN('confirmation-modal-root', 'modal-warning', className)}
	>
		<Form
			initialValues={{
				delete: ''
			}}
			onSubmit={onSubmit}
		>
			{({handleSubmit, isSubmitting, isValid}) => (
				<Form.Form onSubmit={handleSubmit}>
					<Modal.Header
						iconSymbol='warning-full'
						onClose={onClose}
						title={title}
					/>

					<Modal.Body>
						<div className='text-secondary'>
							<p>
								<strong>
									{sub(
										Liferay.Language.get(
											'to-clear-data-from-x,-copy-the-sentence-below-to-confirm-your-intention-to-clear-data-from-this-property'
										),
										[channelName]
									)}
								</strong>
							</p>

							<p>
								{Liferay.Language.get(
									'this-will-result-in-the-complete-removal-of-this-property-and-its-historical-events.-you-will-not-be-able-to-undo-this-operation'
								)}
							</p>
						</div>

						<div className='font-weight-bold mb-3'>
							{sub(
								Liferay.Language.get('copy-the-following-x'),
								[
									<span
										className='font-weight-normal text-secondary'
										key='deletePropertyText'
									>
										{sub(Liferay.Language.get('clear-x'), [
											channelName
										])}
									</span>
								],
								false
							)}
						</div>

						<Form.Input
							name='delete'
							validate={validateInputMessage(sub(
								Liferay.Language.get('clear-x'),
								[channelName]
							) as string)}
						/>
					</Modal.Body>

					<Modal.Footer>
						<Button onClick={onClose}>
							{Liferay.Language.get('cancel')}
						</Button>

						<Button
							disabled={!isValid || isSubmitting}
							display='warning'
							loading={isSubmitting}
							type='submit'
						>
							{Liferay.Language.get('clear-data')}
						</Button>
					</Modal.Footer>
				</Form.Form>
			)}
		</Form>
	</Modal>
);

export default ClearDataModal;
