import Button from 'shared/components/Button';
import Form from 'shared/components/form';
import Modal from 'shared/components/modal';
import React, {useRef} from 'react';
import TimeZonePicker from '../form/TimeZonePicker';
import {Formik} from 'formik';
import {TimeZone} from 'shared/util/records';

interface ITimeZoneSelectionModal {
	onClose: Function;
	timeZone: TimeZone;
}

const TimeZoneSelectionModal: React.FC<ITimeZoneSelectionModal> = ({
	onClose,
	timeZone
}) => {
	const _formRef = useRef<Formik>();
	// TODO: LRAC-6681 Add the timezone update request in modal
	const onSubmit = () => {};

	return (
		<Modal>
			<Modal.Header title={Liferay.Language.get('workspace-timezone')} />

			<Form
				initialValues={{
					timeZoneId: ''
				}}
				onSubmit={onSubmit}
				ref={_formRef}
			>
				{({
					handleSubmit,
					isSubmitting,
					isValid,
					setFieldTouched,
					setFieldValue
				}) => (
					<Form.Form onSubmit={handleSubmit}>
						<Modal.Body>
							<div className='mb-4'>
								{Liferay.Language.get(
									'your-workspace-now-supports-custom-timezones-setting-timezones-will-only-impact-future-data-expect-spiked-or-flat-data-for-1-2-days-following-a-change'
								)}
							</div>

							<div className='picker-root-container'>
								<div className='time-zone-spaced-select'>
									<TimeZonePicker
										fieldName='timeZoneId'
										initialTimeZone={timeZone}
										setFieldTouched={setFieldTouched}
										setFieldValue={setFieldValue}
									/>
								</div>

								<span className='current-time-display'>
									{Liferay.Language.get('current-time-colon')}
								</span>
								<span className='current-time-value ml-4'>
									{'09:00 a.m.'}
								</span>
							</div>
						</Modal.Body>

						<Modal.Footer>
							<Button onClick={() => onClose()}>
								{Liferay.Language.get('do-this-later')}
							</Button>

							<Button
								disabled={!isValid}
								display='primary'
								loading={isSubmitting}
								type='submit'
							>
								{Liferay.Language.get('set-timezone')}
							</Button>
						</Modal.Footer>
					</Form.Form>
				)}
			</Form>
		</Modal>
	);
};

export default TimeZoneSelectionModal;
