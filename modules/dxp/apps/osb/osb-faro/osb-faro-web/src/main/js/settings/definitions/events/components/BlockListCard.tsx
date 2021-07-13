import BLOCKED_CUSTOM_EVENT_DEFINITIONS_QUERY, {
	BlockedCustomEventDefinitionsData,
	BlockedCustomEventDefinitionsVariables,
	HideBlockedCustomEventDefinitions,
	HideBlockedCustomEventDefinitionsData,
	HideBlockedCustomEventDefinitionsVariables,
	UnhideBlockedCustomEventDefinitions,
	UnhideBlockedCustomEventDefinitionsData
} from '../queries/BlockedCustomEventDefinitionsQuery';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Constants from 'shared/util/constants';
import Nav from 'shared/components/Nav';
import React from 'react';
import RowActions from 'shared/components/RowActions';
import URLConstants from 'shared/util/url-constants';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {
	BlockCustomEventDefinitionsData,
	BlockCustomEventDefinitionsVariables,
	UnblockCustomEventDefinitions
} from 'event-analysis/queries/CustomEventDefinitions';
import {BlockedCustomEvent} from 'event-analysis/utils/types';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose} from 'redux';
import {connect} from 'react-redux';
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

const EVENT_LIMIT_REACHED = /Processing request will exceed custom event definition limit/;

const withData = () => WrapperComponent => ({
	delta = defaultDelta,
	orderBy,
	orderByField,
	page = defaultPage,
	query,
	...otherProps
}) => {
	const {data, error, loading, refetch} = useQuery<
		BlockedCustomEventDefinitionsData,
		BlockedCustomEventDefinitionsVariables
	>(BLOCKED_CUSTOM_EVENT_DEFINITIONS_QUERY, {
		fetchPolicy: 'network-only',
		variables: {
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
			items={get(
				data,
				[
					'blockedCustomEventDefinitions',
					'blockedCustomEventDefinitions'
				],
				[]
			)}
			loading={loading}
			noResultsProps={{
				icon: {border: false, size: 'xxxl', symbol: 'ac-satellite'}
			}}
			orderBy={orderBy}
			orderByField={orderByField}
			page={page}
			query={query}
			refetch={refetch}
			total={get(data, ['blockedCustomEventDefinitions', 'total'], 0)}
		/>
	);
};

const BlockListCard = withCrossPageSelect(withData, {
	defaultOrderBy: orderDefault,
	defaultOrderByField: NAME,
	emptyTitle: Liferay.Language.get('no-blocked-events-to-report'),
	getColumns: ({timeZoneId}) => [
		eventListColumns.name,
		eventListColumns.lastSeenURL,
		eventListColumns.getLastSeenDate(timeZoneId),
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

		const [unblockCustomEventDefinitions] = useMutation<
			BlockCustomEventDefinitionsData,
			BlockCustomEventDefinitionsVariables
		>(UnblockCustomEventDefinitions);

		const [hideEventDefinitions] = useMutation<
			HideBlockedCustomEventDefinitionsData,
			HideBlockedCustomEventDefinitionsVariables
		>(HideBlockedCustomEventDefinitions, {
			onCompleted: ({
				hideBlockedEventDefinitions
			}: {
				hideBlockedEventDefinitions: BlockedCustomEvent[];
			}) => {
				if (!selectedItems.isEmpty()) {
					selectionDispatch({
						payload: {
							items: hideBlockedEventDefinitions
						},
						type: 'add'
					});
				}
			}
		});

		const [unhideEventDefinitions] = useMutation<
			UnhideBlockedCustomEventDefinitionsData,
			HideBlockedCustomEventDefinitionsVariables
		>(UnhideBlockedCustomEventDefinitions, {
			onCompleted: ({
				unhideBlockedEventDefinitions
			}: {
				unhideBlockedEventDefinitions: BlockedCustomEvent[];
			}) => {
				if (!selectedItems.isEmpty()) {
					selectionDispatch({
						payload: {
							items: unhideBlockedEventDefinitions
						},
						type: 'add'
					});
				}
			}
		});

		const handleHideEvents = (events: BlockedCustomEvent[] = []) => {
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
													'x-has-been-set-to-hide'
												),
												[visibleEvents[0].name]
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
								visibleEvents[0].name
						  ]),
				titleIcon: 'warning'
			});
		};

		const handleUnhideEvents = (events: BlockedCustomEvent[] = []) => {
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
										[hiddenEvents[0].name]
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

		const handleUnblockEvents = (events: BlockedCustomEvent[] = []) => {
			const eventsCount = events.length;

			unblockCustomEventDefinitions({
				variables: {
					eventDefinitionIds: events.map(({id}) => id)
				}
			})
				.then(() => {
					selectionDispatch({
						type: 'clear-all'
					});

					const updatedPage = eventsCount > 1 ? 1 : Number(page);

					if (updatedPage !== Number(page)) {
						history.push(
							setUriQueryValues(
								{
									orderBy,
									orderByField,
									page: updatedPage
								},
								toRoute(
									Routes.SETTINGS_DEFINITIONS_EVENTS_BLOCK_LIST,
									{
										groupId
									}
								)
							)
						);
					} else {
						refetch({
							fetchPolicy: 'network-only',
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
											'x-events-have-been-returned-to-the-custom-event-list'
										),
										[eventsCount]
								  )
								: sub(
										Liferay.Language.get(
											'x-has-been-returned-to-the-custom-event-list'
										),
										[events[0].name]
								  )
					});
				})
				.catch(err => {
					let message = Liferay.Language.get(
						'there-was-an-error-processing-your-request.-please-try-again'
					);

					if (EVENT_LIMIT_REACHED.test(err.message)) {
						message = sub(
							Liferay.Language.get(
								'your-workspace-is-over-the-event-limit.-please-remove-some-events-from-the-allow-list-to-continue.-visit-our-x-to-learn-more'
							),
							[
								<a
									href={URLConstants.DocumentationLink}
									key='DOCUMENTATION_LINK'
									target='_blank'
								>
									{Liferay.Language.get(
										'documentation-fragment'
									)}
								</a>
							],
							false
						);
					}

					addAlert({
						alertType: Alert.Types.Error,
						message,
						timeout: false
					});
				});
		};

		const renderRowActions = ({data}: {data: BlockedCustomEvent}) => {
			const {hidden} = data;

			return (
				<RowActions
					quickActions={[
						{
							iconSymbol: 'undo',
							label: Liferay.Language.get('unblock-event'),
							onClick: () => {
								handleUnblockEvents([data]);
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

		const hasUnhiddenEvent = (
			events: OrderedMap<string, BlockedCustomEvent>
		) => events.some(({hidden}) => !hidden);

		return (
			<Card pageDisplay>
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
												icon='undo'
												iconAlignment='left'
												onClick={() => {
													handleUnblockEvents(
														selectedItems.toArray()
													);
												}}
											>
												{Liferay.Language.get(
													'unblock-events'
												)}
											</Button>

											<Button
												borderless
												className='nav-btn'
												display='outline-secondary'
												icon={
													hasUnhiddenEvent(
														selectedItems
													)
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
													? Liferay.Language.get(
															'hide'
													  )
													: Liferay.Language.get(
															'show'
													  )}
											</Button>
										</Nav.Item>
									</Nav>
							  )
							: null
					}
					renderRowActions={
						authorized && !selectedItems.size
							? renderRowActions
							: null
					}
					showCheckbox={authorized}
				/>
			</Card>
		);
	}
});

export default compose<any>(
	withSelectionProvider,
	withCurrentUser,
	connect(null, {addAlert, close, open})
)(BlockListCard);
