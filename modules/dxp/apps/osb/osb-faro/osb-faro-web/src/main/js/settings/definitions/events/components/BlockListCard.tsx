import BLOCKED_CUSTOM_EVENT_DEFINITIONS_QUERY, {
	BlockedCustomEventDefinitionsData,
	BlockedCustomEventDefinitionsVariables
} from '../queries/BlockedCustomEventDefinitionsQuery';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Constants from 'shared/util/constants';
import Nav from 'shared/components/Nav';
import React from 'react';
import RowActions from 'shared/components/RowActions';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {
	BlockCustomEventDefinitionsData,
	BlockCustomEventDefinitionsVariables,
	UnblockCustomEventDefinitions
} from 'event-analysis/queries/CustomEventDefinitions';
import {BlockedCustomEvent} from 'event-analysis/utils/types';
import {compose} from 'redux';
import {connect} from 'react-redux';
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
		BlockedCustomEventDefinitionsData,
		BlockedCustomEventDefinitionsVariables
	>(BLOCKED_CUSTOM_EVENT_DEFINITIONS_QUERY, {
		fetchPolicy: 'no-cache',
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
		eventListColumns.getLastSeenDate(timeZoneId)
	],
	rowIdentifier: 'id',
	showDropdownRangeKey: false,
	withQueryOptions: Component => ({addAlert, currentUser, ...otherProps}) => {
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
				.catch(() =>
					// TODO: LRAC-7606 Add custom mesage for when returning an event to custom event list will go over 100 events
					addAlert({
						alertType: Alert.Types.Error,
						message: Liferay.Language.get(
							'there-was-an-error-processing-your-request.-please-try-again'
						),
						timeout: false
					})
				);
		};

		const renderRowActions = ({data}: {data: BlockedCustomEvent}) => (
			<RowActions
				quickActions={[
					{
						iconSymbol: 'undo',
						label: Liferay.Language.get('unblock-event'),
						onClick: () => {
							handleUnblockEvents([data]);
						}
					}
				]}
			/>
		);

		const authorized = currentUser.isAdmin();

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
	connect(null, {addAlert})
)(BlockListCard);
