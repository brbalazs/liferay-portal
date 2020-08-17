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
import React, {useContext, useEffect, useRef, useState} from 'react';
import Sheet from 'shared/components/Sheet';
import urlConstants from 'shared/util/url-constants';
import {BasePageContext} from './BasePage';
import {close, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {Formik} from 'formik';
import {matchPath} from 'react-router-dom';
import {Modal} from 'shared/types';
import {Project} from 'shared/util/records';
import {Routes} from 'shared/util/router';
import {sequence} from 'shared/util/promise';
import {sub} from 'shared/util/lang';

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

const VALIDATE_DOMAINS = /^([a-zA-Z0-9_]([a-zA-Z0-9_-]{0,61}[a-zA-Z0-9_])?\.){1,126}[a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z]$/;

export const emailDomainValidation = value => VALIDATE_DOMAINS.test(value);

export const emailDomainValidationArr = (items, inputListValue) => {
	if (inputListValue && !emailDomainValidation(inputListValue)) {
		return Liferay.Language.get(
			'please-enter-the-domain-in-this-format-domain-com'
		);
	} else if (items.length) {
		return items.reduce(
			(acc, item) =>
				!emailDomainValidation(item)
					? Liferay.Language.get(
							'please-enter-the-domain-in-this-format-domain-com'
					  )
					: acc,
			''
		);
	}
};

interface IAddWorkspaceFormProps extends React.HTMLAttributes<HTMLElement> {
	close: Modal.close;
	disabled: boolean;
	emailAddressDomains: string[];
	onSubmit: (values) => Promise<any>;
	open: Modal.open;
	project?: Project;
}

const AddWorkspaceForm: React.FC<IAddWorkspaceFormProps> = ({
	className,
	close,
	disabled = false,
	emailAddressDomains,
	onSubmit,
	open,
	project
}) => {
	const [editing, setEditing] = useState(false);
	const {currentUser} = useContext(BasePageContext);
	const formRef = useRef<Formik>();
	const isTrialPath = matchPath(location.pathname, {
		exact: true,
		path: Routes.WORKSPACE_ADD_TRIAL
	});

	const [inputListValue, setInputListValue] = useState();

	useEffect(() => {
		if (project) {
			setEditing(true);
		}
	}, []);

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
						name: (project && project.name) || '',
						serverLocation:
							(project && project.serverLocation) || US
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
						resetForm
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
										disabled={disabled || editing}
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
											emailDomainValidationArr(
												items,
												inputListValue
											)
										}
										validationFn={emailDomainValidation}
									/>
								</Sheet.Section>
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
