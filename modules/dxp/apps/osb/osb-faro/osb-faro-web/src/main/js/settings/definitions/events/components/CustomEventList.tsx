import Button from 'shared/components/Button';
import Constants from 'shared/util/constants';
import EVENT_DEFINITIONS_QUERY, {
	EventDefinitionsData,
	EventDefinitionsVariables
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
	>(EVENT_DEFINITIONS_QUERY, {
		fetchPolicy: 'no-cache',
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
		eventListColumns.description
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

		const renderRowActions = ({data}: {data: Event}) => (
			<RowActions
				quickActions={[
					{
						iconSymbol: 'ac-block',
						label: Liferay.Language.get('block-event'),
						onClick: () => {
							handleBlockEvents([data]);
						}
					}
				]}
			/>
		);

		const authorized = currentUser.isAdmin();

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
