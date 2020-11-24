import Button from 'shared/components/Button';
import ClayIcon from '@clayui/icon';
import Constants from 'shared/util/constants';
import Form, {
	validateMaxLength,
	validateMinLength,
	validatePattern,
	validateRequired
} from 'shared/components/form';
import getCN from 'classnames';
import NavigationWarning from 'shared/components/NavigationWarning';
import React, {useContext, useRef, useState} from 'react';
import Sheet from 'shared/components/Sheet';
import TimeZonePicker from '../form/TimeZonePicker';
import urlConstants from 'shared/util/url-constants';
import {BasePageContext} from './BasePage';
import {close, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {DEVELOPER_MODE} from 'shared/util/constants';
import {Formik} from 'formik';
import {matchPath} from 'react-router-dom';
import {Modal} from 'shared/types';
import {Project, TimeZone} from 'shared/util/records';
import {Routes} from 'shared/util/router';
import {sequence} from 'shared/util/promise';
import {sub} from 'shared/util/lang';
import {
	validateEmail,
	validateEmailArr,
	validateEmailDomain,
	validateEmailDomainArr
} from 'shared/util/email-validators';

const {
	faroURL,
	projectLocations: {EU, EU2, SA, US}
} = Constants;

const projectLocations = [
	{label: Liferay.Language.get('location-eu'), value: EU},
	{label: Liferay.Language.get('location-eu2'), value: EU2},
	{label: Liferay.Language.get('location-sa'), value: SA},
	{label: Liferay.Language.get('location-us'), value: US}
];

interface IAddWorkspaceFormProps extends React.HTMLAttributes<HTMLElement> {
	close: Modal.close;
	disabled: boolean;
	editing: boolean;
	emailAddressDomains: string[];
	onSubmit: (values) => Promise<any>;
	open: Modal.open;
	project?: Project;
}

const AddWorkspaceForm: React.FC<IAddWorkspaceFormProps> = ({
	className,
	close,
	disabled = false,
	editing = false,
	emailAddressDomains,
	onSubmit,
	open,
	project
}) => {
	const {currentUser} = useContext(BasePageContext);
	const formRef = useRef<Formik>();
	const isTrialPath = matchPath(location.pathname, {
		exact: true,
		path: Routes.WORKSPACE_ADD_TRIAL
	});

	const [inputListValue, setInputListValue] = useState();
	const [
		emailAddressesInputValues,
		setEmailAddressesInputValues
	] = useState();

	const handleSubmit = (
		values,
		{resetForm, setFieldError, setSubmitting}
	) => {
		const {initialValues} = formRef.current;
		const {friendlyURL: initialFriendlyURL} = initialValues;

		const {friendlyURL: newFriendlyURL} = values;

		const submitFn = () =>
			onSubmit(values)
				.then(() => {
					setSubmitting(false);

					if (initialFriendlyURL === newFriendlyURL) {
						resetForm(values);
					}
				})
				.catch(({field, message}) => {
					setSubmitting(false);

					if (field) {
						setFieldError(field, message);
					}
				});

		if (newFriendlyURL !== initialFriendlyURL) {
			setSubmitting(false);

			open(Modal.modalTypes.CONFIRMATION_MODAL, {
				message: (
					<div>
						<p className='text-secondary'>
							{Liferay.Language.get(
								'you-can-only-set-your-friendly-workspace-url-once.-are-you-sure-you-would-like-to-save-it-as-the-following-url'
							)}
						</p>

						<p>
							<span className='text-secondary'>
								{`${faroURL}/workspace/`}
							</span>

							<b>{newFriendlyURL}</b>
						</p>
					</div>
				),
				modalVariant: 'modal-info',
				onClose: close,
				onSubmit: submitFn,
				submitMessage: editing
					? Liferay.Language.get('save')
					: Liferay.Language.get('create-workspace'),
				title: Liferay.Language.get('setting-friendly-workspace-url'),
				titleIcon: 'info-circle'
			});
		} else {
			submitFn();
		}
	};

	return (
		<div className={getCN('add-workspace-form-root', className)}>
			<Sheet>
				<Form
					enableReinitialize
					initialValues={{
						emailAddressDomains: emailAddressDomains || [],
						friendlyURL:
							project && project.friendlyURL
								? project.friendlyURL.replace('/', '')
								: '',
						incidentReportEmailAddresses:
							(project &&
								project.incidentReportEmailAddresses.toArray()) ||
							[],
						name: (project && project.name) || '',
						serverLocation:
							(project && project.serverLocation) || US,
						timeZoneId:
							(project &&
								project.getIn(['timeZone', 'timeZoneId'])) ||
							'UTC' // TODO: [LRAC-6981] Make the default value to be an empty string for 2.10.0
					}}
					onSubmit={handleSubmit}
					ref={formRef}
				>
					{({
						dirty,
						handleSubmit,
						initialValues,
						isSubmitting,
						isValid,
						resetForm,
						setFieldTouched,
						setFieldValue
					}) => (
						<Form.Form onSubmit={handleSubmit}>
							<NavigationWarning when={!!project && dirty} />

							<Sheet.Header>
								{!editing && (
									<>
										<h3 className='title'>
											{isTrialPath
												? Liferay.Language.get(
														'create-a-new-free-trial-workspace'
												  )
												: Liferay.Language.get(
														'configure-your-new-workspace'
												  )}
										</h3>
										<p>
											{sub(
												Liferay.Language.get(
													'x-will-be-the-owner-of-this-workspace'
												),
												[currentUser.emailAddress]
											)}
										</p>
									</>
								)}

								<Sheet.Subtitle>
									{Liferay.Language.get('general')}
								</Sheet.Subtitle>

								<Sheet.Section className='input-name'>
									<Form.Input
										disabled={disabled}
										label={
											<>
												{Liferay.Language.get(
													'workspace-name'
												)}

												<span className='reference-mark'>
													<ClayIcon symbol='asterisk' />
												</span>
											</>
										}
										name='name'
										validate={sequence([
											validateRequired,
											validateMaxLength(255)
										])}
									/>
								</Sheet.Section>

								<Sheet.Section className='input-server'>
									<Form.Select
										data-testid='server-location-input'
										disabled={
											disabled ||
											editing ||
											(project && project.serverLocation)
										}
										label={
											<>
												{Liferay.Language.get(
													'data-center-location'
												)}

												<span className='reference-mark'>
													<ClayIcon symbol='asterisk' />
												</span>

												<p className='instructions'>
													{Liferay.Language.get(
														'select-a-server-to-store-your-data.-this-could-have-implications-to-your-organizations-policy-on-user-data-storage'
													)}
												</p>
											</>
										}
										name='serverLocation'
									>
										{projectLocations.map(
											({label, value}) => (
												<Form.Select.Item
													key={value}
													value={value}
												>
													{label}
												</Form.Select.Item>
											)
										)}
									</Form.Select>

									{/* <p class="extra-instruction text-secondary">
											{sub(
												Liferay.Language.get(
													'cant-find-the-right-server?-send-us-a-x'
												),
												[
													// TODO: This should in the future direct to a
													// suggestion form in the app
													<a href="#1" key="suggestion">
														{Liferay.Language.get(
															'suggestion-fragment'
														)}
													</a>
												],
												false
											)}
									</p> */}
								</Sheet.Section>

								{/* TODO: [LRAC-6981] Enable this again for 2.10.0 release */}
								{DEVELOPER_MODE && (
									<Sheet.Section>
										<Form.Label>
											{Liferay.Language.get('timezone')}

											<span className='reference-mark'>
												<ClayIcon symbol='asterisk' />
											</span>

											<p className='instructions'>
												{Liferay.Language.get(
													'select-a-timezone-that-will-be-used-for-all-data-reporting-in-your-workspace'
												)}

												<strong className='ml-1'>
													{Liferay.Language.get(
														'cannot-be-changed-after-creation'
													)}
												</strong>
											</p>
										</Form.Label>

										<TimeZonePicker
											disabled={disabled || editing}
											fieldName='timeZoneId'
											initialTimeZone={
												project &&
												new TimeZone(
													project.getIn(['timeZone'])
												)
											}
											setFieldTouched={setFieldTouched}
											setFieldValue={setFieldValue}
										/>
									</Sheet.Section>
								)}

								<Sheet.Section>
									<Form.Input
										data-testid='friendly-url-input'
										disabled={
											disabled ||
											(project && project.friendlyURL)
										}
										label={
											<>
												{Liferay.Language.get(
													'set-a-friendly-workspace-url'
												)}

												{!editing && (
													<p className='instructions'>
														{Liferay.Language.get(
															'you-can-only-set-your-friendly-workspace-url-once'
														)}
													</p>
												)}

												<p className='instructions form-text'>
													{`${faroURL}/workspace`}
												</p>
											</>
										}
										name='friendlyURL'
										text={{
											content: '/',
											position: 'prepend'
										}}
										validate={sequence([
											validateMinLength(2),
											validateMaxLength(255),
											validatePattern(
												/^(?=.*[a-z])[a-z0-9._-]+$/,
												sub(
													Liferay.Language.get(
														'workspace-url-must-only-contain-x-and-at-least-one-letter'
													),
													["a-z, 0-9, '.', '_', '-'"]
												) as string
											)
										])}
									/>
								</Sheet.Section>

								<Sheet.Section>
									<Form.InputList
										disabled={disabled}
										errorMessage={Liferay.Language.get(
											'please-enter-the-domain-in-this-format-domain-com'
										)}
										label={
											<>
												{Liferay.Language.get(
													'allowed-email-domains'
												)}

												<p className='instructions'>
													{Liferay.Language.get(
														'anyone-with-an-email-address-at-these-domains-can-request-access-to-your-workspace'
													)}
												</p>
											</>
										}
										name='emailAddressDomains'
										onChangeInputList={setInputListValue}
										text={{
											content: '@',
											position: 'prepend'
										}}
										validate={items =>
											validateEmailDomainArr(
												items,
												inputListValue
											)
										}
										validationFn={validateEmailDomain}
									/>
								</Sheet.Section>

								{DEVELOPER_MODE && ( // TODO: LRAC-6933 Remove DEVELOPER_MODE Flag 2.10.0
									<>
										<Sheet.Subtitle>
											{Liferay.Language.get('security')}
										</Sheet.Subtitle>

										<Sheet.Section>
											<Form.InputList
												errorMessage={Liferay.Language.get(
													'please-enter-the-email-in-this-format-sample-email-com'
												)}
												label={
													<>
														{Liferay.Language.get(
															'add-incident-report-contacts'
														)}

														<p className='instructions'>
															{Liferay.Language.get(
																'who-should-we-contact-in-case-of-a-security-breach'
															)}
														</p>
													</>
												}
												name='incidentReportEmailAddresses'
												onChangeInputList={
													setEmailAddressesInputValues
												}
												validate={items =>
													validateEmailArr(
														items,
														emailAddressesInputValues
													)
												}
												validationFn={validateEmail}
											/>
										</Sheet.Section>
									</>
								)}
							</Sheet.Header>

							<Sheet.Footer divider={false}>
								{!editing ? (
									<>
										<div className='terms'>
											<Form.Checkbox
												label={Liferay.Language.get(
													'i-agree'
												)}
												name='termsAcceptance'
												validate={validateRequired}
											/>

											<p>
												{sub(
													Liferay.Language.get(
														'by-selecting-i-agree-,-you-agree-to-our-x-including-our-x'
													),
													[
														<a
															href={
																urlConstants.TERMS_AND_CONDITIONS
															}
															key="'terms-and-conditions'"
														>
															{Liferay.Language.get(
																'terms-and-conditions'
															)}
														</a>,
														<a
															href={
																urlConstants.PRIVACY_POLICY
															}
															key='privacy-policy'
														>
															{Liferay.Language.get(
																'privacy-policy'
															)}
														</a>
													],
													false
												)}
											</p>
										</div>

										<Button
											block
											disabled={
												disabled ||
												isSubmitting ||
												!isValid
											}
											display='primary'
											loading={isSubmitting}
											type='submit'
										>
											{isSubmitting
												? Liferay.Language.get(
														'creating-new-workspace'
												  )
												: Liferay.Language.get(
														'create-workspace'
												  )}
										</Button>
									</>
								) : (
									<>
										<Button
											className='mr-3'
											disabled={
												disabled ||
												isSubmitting ||
												!isValid
											}
											display='primary'
											loading={isSubmitting}
											type='submit'
										>
											{isSubmitting
												? Liferay.Language.get('saving')
												: Liferay.Language.get('save')}
										</Button>

										<Button
											disabled={disabled || !dirty}
											display='secondary'
											onClick={() =>
												resetForm(initialValues)
											}
										>
											{Liferay.Language.get('cancel')}
										</Button>
									</>
								)}
							</Sheet.Footer>
						</Form.Form>
					)}
				</Form>
			</Sheet>
		</div>
	);
};

export default connect(
	null,
	{close, open}
)(AddWorkspaceForm);
