import * as Types from 'shared/types';
import Button from 'shared/components/Button';
import client from 'shared/apollo/client';
import Form, {toPromise, validateMaxLength} from 'shared/components/form';
import Modal from 'shared/components/modal';
import React from 'react';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {Attribute, DataTypes, Event} from '../utils/types';
import {connect} from 'react-redux';
import {DATA_TYPE_LABELS_MAP} from '../utils/utils';
import {debounce, get} from 'lodash/fp';
import {DocumentNode} from 'graphql';
import {
	EventAttributeDefinitionData,
	EventAttributeDefinitionVariables,
	UpdateEventAttributeDefinitionVariables
} from '../queries/EventAttributeDefinitionQuery';
import {
	EventDefinitionData,
	EventDefinitionVariables,
	UpdateEventDefinitionVariables
} from '../queries/EventDefinitionQuery';
import {SafeResults} from 'shared/hoc/util';
import {sequence} from 'shared/util/promise';
import {sub} from 'shared/util/lang';
import {useMutation, useQuery} from '@apollo/react-hooks';

const DATA_TYPE_OPTIONS = [
	DataTypes.Boolean,
	DataTypes.Date,
	DataTypes.Duration,
	DataTypes.Number,
	DataTypes.String
];

interface IEditAttributeEventModalProps {
	addAlert: Alert.AddAlert;
	id: string;
	mutation: DocumentNode;
	onCancel: Types.Modal.close;
	query: DocumentNode;
	showTypecast?: boolean;
}

const EditAttributeEventModal: React.FC<IEditAttributeEventModalProps> = ({
	addAlert,
	id,
	mutation,
	onCancel,
	query,
	showTypecast
}) => {
	const [update] = useMutation<
		EventDefinitionData | EventAttributeDefinitionData,
		UpdateEventDefinitionVariables | UpdateEventAttributeDefinitionVariables
	>(mutation);
	const result = useQuery<
		EventDefinitionData | EventAttributeDefinitionData,
		EventDefinitionVariables | EventAttributeDefinitionVariables
	>(query, {
		fetchPolicy: 'no-cache',
		variables: {id}
	});

	const dataMapper = get(
		showTypecast ? 'eventAttributeDefinition' : 'eventDefinition'
	);

	const validateDisplayName = debounce(250)(
		(value: string): Promise<string> => {
			let error = '';

			if (value !== dataMapper(result.data).displayName) {
				return client
					.query({
						query,
						variables: {displayName: value}
					})
					.then(({data}) => {
						if (dataMapper(data)) {
							error = (sub(
								Liferay.Language.get(
									'an-x-already-exists-with-that-display-name.-please-enter-a-different-display-name'
								),
								[
									showTypecast
										? Liferay.Language.get('attribute')
										: Liferay.Language.get('event')
								]
							) as string).toLowerCase();
						}

						return error;
					})
					.catch(() => {
						error = Liferay.Language.get(
							'there-was-an-error-processing-your-request.-please-try-again'
						);

						return error;
					});
			} else {
				return toPromise(error);
			}
		}
	);

	return (
		<Modal>
			<SafeResults {...result} page={false} pageDisplay={false}>
				{(item: {
					eventAttributeDefinition: Attribute;
					eventDefinition: Event;
				}) => {
					const {
						dataType,
						description,
						displayName,
						name
					} = dataMapper(item);

					return (
						<>
							<Modal.Header
								onClose={() => onCancel()}
								title={
									displayName
										? `${name} - ${displayName}`
										: name
								}
							/>

							<Form
								initialValues={
									showTypecast
										? {
												dataType,
												description: description || '',
												displayName: displayName || ''
										  }
										: {
												description: description || '',
												displayName: displayName || ''
										  }
								}
								onSubmit={(variables, {setSubmitting}) => {
									update({
										variables: {
											id,
											...variables
										}
									})
										.then(({data}) => {
											const {displayName, name} = get(
												showTypecast
													? 'updateEventAttributeDefinition'
													: 'updateEventDefinition'
											)(data);
											addAlert({
												alertType: Alert.Types.SUCCESS,
												message: sub(
													Liferay.Language.get(
														'x-has-been-updated'
													),
													[displayName || name]
												) as string
											});

											setSubmitting(false);

											onCancel();
										})
										.catch(() => {
											addAlert({
												alertType: Alert.Types.ERROR,
												message: Liferay.Language.get(
													'there-was-an-error-processing-your-request.-please-try-again'
												)
											});

											setSubmitting(false);
										});
								}}
							>
								{({handleSubmit, isSubmitting, isValid}) => (
									<Form.Form onSubmit={handleSubmit}>
										<Modal.Body>
											<Form.Group autoFit>
												<Form.GroupItem>
													<Form.Input
														label={Liferay.Language.get(
															'display-name'
														)}
														name='displayName'
														type='text'
														validate={sequence([
															validateMaxLength(
																50
															),
															validateDisplayName
														])}
													/>
												</Form.GroupItem>
											</Form.Group>

											<Form.Group autoFit>
												<Form.GroupItem>
													<Form.Input
														label={Liferay.Language.get(
															'description'
														)}
														name='description'
														type='textarea'
														validate={sequence([
															validateMaxLength(
																255
															)
														])}
													/>
												</Form.GroupItem>
											</Form.Group>

											{showTypecast && (
												<Form.Group>
													<Form.GroupItem>
														<Form.Select
															label={Liferay.Language.get(
																'default-data-typecast'
															)}
															name='dataType'
														>
															{DATA_TYPE_OPTIONS.map(
																value => (
																	<Form.Select.Item
																		key={
																			value
																		}
																		value={
																			value
																		}
																	>
																		{
																			DATA_TYPE_LABELS_MAP[
																				value
																			]
																		}
																	</Form.Select.Item>
																)
															)}
														</Form.Select>
													</Form.GroupItem>

													<Form.GroupItem className='text-secondary'>
														{Liferay.Language.get(
															'data-typecast-determines-how-attributes-can-be-analyzed'
														)}
													</Form.GroupItem>

													<Form.GroupItem className='text-secondary'>
														{Liferay.Language.get(
															'e.g.-typecasting-to-number-will-support-greater-than-or-less-than-conditions'
														)}
													</Form.GroupItem>
												</Form.Group>
											)}
										</Modal.Body>

										<Modal.Footer>
											<Button onClick={() => onCancel()}>
												{Liferay.Language.get('cancel')}
											</Button>

											<Button
												disabled={!isValid}
												display='primary'
												loading={isSubmitting}
												type='submit'
											>
												{Liferay.Language.get('save')}
											</Button>
										</Modal.Footer>
									</Form.Form>
								)}
							</Form>
						</>
					);
				}}
			</SafeResults>
		</Modal>
	);
};

export default connect(
	null,
	{addAlert}
)(EditAttributeEventModal);
