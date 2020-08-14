import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Form, {
	validateMaxLength,
	validateRequired
} from 'shared/components/form';
import Icon from 'shared/components/Icon';
import PreferenceMutation from 'settings/data-privacy/queries/PreferenceMutation';
import PreferenceQuery from 'settings/data-privacy/queries/PreferenceQuery';
import React, {useRef} from 'react';
import Spinner from 'shared/components/Spinner';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {
	ArrayHelpers,
	FieldArray,
	Formik,
	FormikTouched,
	FormikValues
} from 'formik';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withHistory} from 'shared/hoc';
import {connect} from 'react-redux';
import {Routes, toRoute} from 'shared/util/router';
import {sequence} from 'shared/util/promise';
import {useMutation, useQuery} from '@apollo/react-hooks';

const SEARCH_QUERY_STRINGS_KEY = 'search-query-strings';

interface ISearchCardProps {
	addAlert: Alert.AddAlert;
	close: () => void;
	groupId: string;
	history: {
		push: (string) => void;
	};
	open: (string, object) => void;
}

export const SearchCard: React.FC<ISearchCardProps> = ({
	addAlert,
	close,
	groupId,
	history,
	open
}) => {
	const {data: searchQueryStringsData, loading} = useQuery(PreferenceQuery, {
		fetchPolicy: 'no-cache',
		variables: {key: SEARCH_QUERY_STRINGS_KEY}
	});

	const [updatePreference] = useMutation(PreferenceMutation);

	const _formRef = useRef<Formik>();

	const handleSubmit = ({queryStringList}) => {
		const currentForm = _formRef.current;

		updatePreference({
			variables: {
				key: SEARCH_QUERY_STRINGS_KEY,
				value: JSON.stringify(queryStringList)
			}
		})
			.then(() => {
				addAlert({
					alertType: Alert.Types.SUCCESS,
					message: Liferay.Language.get(
						'search-query-definition-has-been-saved'
					)
				});

				history.push(toRoute(Routes.SETTINGS_DEFINITIONS, {groupId}));
			})
			.catch(() => {
				currentForm.setSubmitting(false);

				addAlert({
					alertType: Alert.Types.ERROR,
					message: Liferay.Language.get('error')
				});
			});
	};

	const handleRemoveField = (
		arrayHelpers: ArrayHelpers,
		index: number,
		currentLength: number
	) => {
		const apiResult =
			JSON.parse(searchQueryStringsData.preference.value) || [];

		apiResult.length === 1 && currentLength === 1
			? arrayHelpers.replace(index, '')
			: arrayHelpers.remove(index);
	};

	const handleCancel = (touchedFields: FormikTouched<FormikValues>) => {
		Object.keys(touchedFields).length > 0
			? open(modalTypes.CONFIRMATION_MODAL, {
					cancelMessage: Liferay.Language.get('cancel'),
					message: Liferay.Language.get(
						'edits-made-to-search-queries-have-not-been-saved-do-you-want-to-exit-without-saving'
					),
					modalVariant: 'modal-warning',
					onClose: close,
					onSubmit: () => {
						history.push(
							toRoute(Routes.SETTINGS_DEFINITIONS, {groupId})
						);
					},
					submitButtonDisplay: 'warning',
					submitMessage: Liferay.Language.get('exit'),
					title: Liferay.Language.get('exit-without-saving'),
					titleIcon: 'warning-full'
			  })
			: history.push(toRoute(Routes.SETTINGS_DEFINITIONS, {groupId}));
	};

	const handleBlur = (
		fieldIdentifier: string,
		fieldValue: string,
		setFieldValue: Function,
		setFieldTouched: Function
	) => {
		setFieldValue(fieldIdentifier, fieldValue.replace(/[^\w\s]/gi, ''));
		setFieldTouched(fieldIdentifier, true);
	};

	const shouldRenderAddButton = (
		currentIndex: number,
		currentLength: number
	) => currentIndex === currentLength - 1 && currentLength <= 4;

	const shouldRenderRemoveButton = (currentLength: number) => {
		const apiResult =
			JSON.parse(searchQueryStringsData.preference.value) || [];

		return (
			(apiResult.length === 1 && currentLength === 1) || currentLength > 1
		);
	};

	return (
		<Card className='query-card-root'>
			<Card.Header className='mb-1'>
				<Card.Title>{Liferay.Language.get('query-string')}</Card.Title>
			</Card.Header>

			<Card.Body>
				{loading ? (
					<Spinner alignCenter spacer />
				) : (
					<Form
						initialValues={{
							queryStringList: JSON.parse(
								searchQueryStringsData.preference.value
							) || ['']
						}}
						onSubmit={handleSubmit}
						ref={_formRef}
					>
						{({
							handleSubmit,
							isSubmitting,
							setFieldTouched,
							setFieldValue,
							touched,
							values
						}) => (
							<Form.Form onSubmit={handleSubmit}>
								<FieldArray
									name='queryStringList'
									render={arrayHelpers => (
										<>
											{values.queryStringList.map(
												(queryString, index) => (
													<div
														className='form-inline mb-3'
														key={index}
													>
														<Form.Input
															className='query-input'
															name={`queryStringList.${index}`}
															onBlur={() =>
																handleBlur(
																	`queryStringList.${index}`,
																	queryString,
																	setFieldValue,
																	setFieldTouched
																)
															}
															validate={sequence([
																validateRequired,
																validateMaxLength(
																	50
																)
															])}
														/>

														{shouldRenderRemoveButton(
															values
																.queryStringList
																.length
														) && (
															<Button
																borderless
																className='ml-1'
																disabled={
																	isSubmitting
																}
																display='secondary'
																onClick={() =>
																	handleRemoveField(
																		arrayHelpers,
																		index,
																		values
																			.queryStringList
																			.length
																	)
																}
															>
																<Icon symbol='trash' />
															</Button>
														)}

														{shouldRenderAddButton(
															index,
															values
																.queryStringList
																.length
														) && (
															<Button
																borderless
																className='ml-1'
																disabled={
																	isSubmitting
																}
																display='secondary'
																onClick={() =>
																	arrayHelpers.push(
																		''
																	)
																}
															>
																<Icon symbol='plus' />
															</Button>
														)}
													</div>
												)
											)}
										</>
									)}
								/>

								<div className='mt-4'>
									<Button
										display='primary'
										loading={isSubmitting}
										type='submit'
									>
										{Liferay.Language.get('save')}
									</Button>

									<Button
										className={'ml-4'}
										display='secondary'
										onClick={() => handleCancel(touched)}
									>
										{Liferay.Language.get('cancel')}
									</Button>
								</div>
							</Form.Form>
						)}
					</Form>
				)}
			</Card.Body>
		</Card>
	);
};

export default compose<any>(
	withHistory,
	connect(
		null,
		{addAlert, close, open}
	)
)(SearchCard);
