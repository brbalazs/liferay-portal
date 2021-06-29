import Button from 'shared/components/Button';
import Constants from 'shared/util/constants';
import EventDefinitionsQuery, {
	EventDefinitionsData,
	EventDefinitionsVariables,
	HideEventDefinitions,
	HideEventDefinitionsData,
	HideEventDefinitionsVariables,
	UnhideEventDefinitions,
	UnhideEventDefinitionsData
} from 'event-analysis/queries/EventDefinitionsQuery';
import Nav from 'shared/components/Nav';
import React from 'react';
import RowActions from 'shared/components/RowActions';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {
	BlockCustomEventDefinitions,
	BlockCustomEventDefinitionsData,
	BlockCustomEventDefinitionsVariables
} from 'event-analysis/queries/CustomEventDefinitions';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {Event, EventTypes} from 'event-analysis/utils/types';
import {eventListColumns} from 'shared/util/table-columns';
import {get} from 'lodash';
import {NAME} from 'shared/util/pagination';
import {OrderedMap} from 'immutable';
import {Routes, setUriQueryValues, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useMutation, useQuery} from '@apollo/react-hooks';
import {
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {withCrossPageSelect, withCurrentUser} from 'shared/hoc';

const {
	pagination: {cur: defaultPage, delta: defaultDelta, orderDefault}
} = Constants;

const withData = () => WrapperComponent => ({
	delta = defaultDelta,
	orderBy,
	orderByField,
	page = defaultPage,
	query,
	...otherProps
}) => {
	const {data, error, loading, refetch} = useQuery<
		EventDefinitionsData,
		EventDefinitionsVariables
	>(EventDefinitionsQuery, {
		fetchPolicy: 'network-only',
		variables: {
			eventType: EventTypes.Custom,
			keyword: query,
			page: Number(page) - 1,
			size: delta,
			sort: {
				column: orderByField,
				type: orderBy.toUpperCase()
			}
		}
	});

	return (
		<WrapperComponent
			{...otherProps}
			delta={delta}
			error={error}
			items={get(data, ['eventDefinitions', 'eventDefinitions'], [])}
			loading={loading}
			noResultsProps={{
				icon: {border: false, size: 'xxxl', symbol: 'ac-satellite'}
			}}
			orderBy={orderBy}
			orderByField={orderByField}
			page={page}
			query={query}
			refetch={refetch}
			total={get(data, ['eventDefinitions', 'total'], 0)}
		/>
	);
};

const CustomEventList = withCrossPageSelect(withData, {
	defaultOrderBy: orderDefault,
	defaultOrderByField: NAME,
	emptyDescription: Liferay.Language.get(
		'visit-our-documentation-to-learn-how-to-add-custom-events-on-your-site'
	),
	emptyTitle: Liferay.Language.get('create-some-custom-events'),
	getColumns: ({groupId}) => [
		eventListColumns.getName({groupId}),
		eventListColumns.displayName,
		eventListColumns.description,
		eventListColumns.hidden
	],
	rowIdentifier: 'id',
	showDropdownRangeKey: false,
	withQueryOptions: Component => ({
		addAlert,
		close,
		currentUser,
		open,
		...otherProps
	}) => {
		const {
			delta,
			groupId,
			history,
			orderBy,
			orderByField,
			page,
			query = '',
			refetch
		} = otherProps;

		const {selectedItems, selectionDispatch} = useSelectionContext();

		const [blockCustomEventDefinitions] = useMutation<
			BlockCustomEventDefinitionsData,
			BlockCustomEventDefinitionsVariables
		>(BlockCustomEventDefinitions);

		const [hideEventDefinitions] = useMutation<
			HideEventDefinitionsData,
			HideEventDefinitionsVariables
		>(HideEventDefinitions, {
			onCompleted: ({
				hideEventDefinitions
			}: {
				hideEventDefinitions: Event[];
			}) => {
				if (!selectedItems.isEmpty()) {
					selectionDispatch({
						payload: {
							items: hideEventDefinitions
						},
						type: 'add'
					});
				}
			}
		});

		const [unhideEventDefinitions] = useMutation<
			UnhideEventDefinitionsData,
			HideEventDefinitionsVariables
		>(UnhideEventDefinitions, {
			onCompleted: ({
				unhideEventDefinitions
			}: {
				unhideEventDefinitions: Event[];
			}) => {
				if (!selectedItems.isEmpty()) {
					selectionDispatch({
						payload: {
							items: unhideEventDefinitions
						},
						type: 'add'
					});
				}
			}
		});

		const handleBlockEvents = (events: Event[] = []) => {
			const eventsCount = events.length;

			open(modalTypes.CONFIRMATION_MODAL, {
				message: (
					<p className='text-secondary'>
						{eventsCount > 1
							? Liferay.Language.get(
									'blocking-events-will-result-in-the-deletion-of-their-display-names-and-descriptions.-you-must-reassign-these-values-if-you-wish-to-unblock-these-events-in-the-future'
							  )
							: sub(
									Liferay.Language.get(
										'blocking-x-will-result-in-the-deletion-of-its-display-name-and-description.-you-must-reassign-these-values-if-you-wish-to-unblock-the-event-in-the-future'
									),
									[events[0].displayName]
							  )}
					</p>
				),
				modalVariant: 'modal-warning',
				onClose: close,
				onSubmit: () => {
					blockCustomEventDefinitions({
						variables: {
							eventDefinitionIds: events.map(({id}) => id)
						}
					})
						.then(() => {
							selectionDispatch({
								type: 'clear-all'
							});

							const updatedPage =
								eventsCount > 1 ? 1 : Number(page);

							if (updatedPage !== Number(page)) {
								history.push(
									setUriQueryValues(
										{
											orderBy,
											orderByField,
											page: updatedPage
										},
										toRoute(
											Routes.SETTINGS_DEFINITIONS_EVENTS_CUSTOM,
											{
												groupId
											}
										)
									)
								);
							} else {
								refetch({
									fetchPolicy: 'no-cache',
									variables: {
										keywords: query,
										size: delta,
										sort: {
											column: orderByField,
											type: orderBy.toUpperCase()
										},
										start: updatedPage - 1
									}
								});
							}

							addAlert({
								alertType: Alert.Types.Success,
								message:
									eventsCount > 1
										? sub(
												Liferay.Language.get(
													'x-events-have-been-added-to-the-block-list'
												),
												[eventsCount]
										  )
										: sub(
												Liferay.Language.get(
													'x-has-been-added-to-the-block-list'
												),
												[events[0].displayName]
										  )
							});
						})
						.catch(() =>
							addAlert({
								alertType: Alert.Types.Error,
								message: Liferay.Language.get(
									'there-was-an-error-processing-your-request.-please-try-again'
								),
								timeout: false
							})
						);
				},
				submitButtonDisplay: 'warning',
				submitMessage: Liferay.Language.get('block'),
				title:
					eventsCount > 1
						? Liferay.Language.get('block-events')
						: Liferay.Language.get('block-event'),
				titleIcon: 'warning'
			});
		};

		const handleHideEvents = (events: Event[] = []) => {
			const visibleEvents = events.filter(({hidden}) => !hidden);

			const visibleEventsCount = visibleEvents.length;

			open(modalTypes.CONFIRMATION_MODAL, {
				message: (
					<p className='text-secondary'>
						{Liferay.Language.get(
							'hiding-events-in-the-interface-may-require-reconfiguration-of-segments-and-other-analysis-using-this-event.-hidden-events-will-be-available-for-calculating-metrics'
						)}
					</p>
				),
				modalVariant: 'modal-warning',
				onClose: close,
				onSubmit: () => {
					hideEventDefinitions({
						variables: {
							eventDefinitionIds: events.map(({id}) => id)
						}
					})
						.then(() => {
							addAlert({
								alertType: Alert.Types.Success,
								message:
									visibleEventsCount > 1
										? sub(
												Liferay.Language.get(
													'x-events-have-been-set-to-hide'
												),
												[visibleEventsCount]
										  )
										: sub(
												Liferay.Language.get(
													'x-have-been-set-to-hide'
												),
												[visibleEvents[0].displayName]
										  )
							});
						})
						.catch(() =>
							addAlert({
								alertType: Alert.Types.Error,
								message: Liferay.Language.get(
									'there-was-an-error-processing-your-request.-please-try-again'
								),
								timeout: false
							})
						);
				},
				submitButtonDisplay: 'warning',
				submitMessage: Liferay.Language.get('hide'),
				title:
					visibleEventsCount > 1
						? Liferay.Language.get('hide-events')
						: sub(Liferay.Language.get('hide-x'), [
								visibleEvents[0].displayName
						  ]),
				titleIcon: 'warning'
			});
		};

		const handleUnhideEvents = (events: Event[] = []) => {
			const hiddenEvents = events.filter(({hidden}) => hidden);

			const hiddenEventsCount = hiddenEvents.length;

			unhideEventDefinitions({
				variables: {
					eventDefinitionIds: events.map(({id}) => id)
				}
			})
				.then(() => {
					addAlert({
						alertType: Alert.Types.Success,
						message:
							hiddenEventsCount > 1
								? sub(
										Liferay.Language.get(
											'x-events-have-been-set-to-show'
										),
										[hiddenEventsCount]
								  )
								: sub(
										Liferay.Language.get(
											'x-has-been-set-to-show'
										),
										[hiddenEvents[0].displayName]
								  )
					});
				})
				.catch(() =>
					addAlert({
						alertType: Alert.Types.Error,
						message: Liferay.Language.get(
							'there-was-an-error-processing-your-request.-please-try-again'
						),
						timeout: false
					})
				);
		};

		const renderRowActions = ({data}: {data: Event}) => {
			const {hidden} = data;

			return (
				<RowActions
					quickActions={[
						{
							iconSymbol: 'ac-block',
							label: Liferay.Language.get('block-event'),
							onClick: () => {
								handleBlockEvents([data]);
							}
						},
						{
							iconSymbol: hidden ? 'view' : 'ac-hidden',
							label: hidden
								? Liferay.Language.get('set-to-show')
								: Liferay.Language.get('set-to-hide'),
							onClick: () => {
								const hideEventFn = hidden
									? handleUnhideEvents
									: handleHideEvents;

								hideEventFn([data]);
							}
						}
					]}
				/>
			);
		};

		const authorized = currentUser.isAdmin();

		const hasUnhiddenEvent = (events: OrderedMap<string, Event>) =>
			events.some(({hidden}) => !hidden);

		return (
			<Component
				{...otherProps}
				renderNav={
					authorized && selectedItems.size
						? () => (
								<Nav>
									<Nav.Item>
										<Button
											borderless
											className='nav-btn'
											display='outline-secondary'
											icon='ac-block'
											iconAlignment='left'
											onClick={() => {
												handleBlockEvents(
													selectedItems.toArray()
												);
											}}
										>
											{Liferay.Language.get(
												'block-events'
											)}
										</Button>

										<Button
											borderless
											className='nav-btn'
											display='outline-secondary'
											icon={
												hasUnhiddenEvent(selectedItems)
													? 'ac-hidden'
													: 'view'
											}
											iconAlignment='left'
											onClick={() => {
												const hideEventFn = hasUnhiddenEvent(
													selectedItems
												)
													? handleHideEvents
													: handleUnhideEvents;

												hideEventFn(
													selectedItems.toArray()
												);
											}}
										>
											{hasUnhiddenEvent(selectedItems)
												? Liferay.Language.get('hide')
												: Liferay.Language.get('show')}
										</Button>
									</Nav.Item>
								</Nav>
						  )
						: null
				}
				renderRowActions={
					authorized && !selectedItems.size ? renderRowActions : null
				}
				showCheckbox={authorized}
			/>
		);
	}
});

export default compose<any>(
	withSelectionProvider,
	withCurrentUser,
	connect(null, {addAlert, close, open})
)(CustomEventList);
