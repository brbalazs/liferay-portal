import * as API from 'shared/api';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Nav from 'shared/components/Nav';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import React from 'react';
import SearchableTableWithStaged from 'shared/components/searchable-table-with-staged';
import Spinner from 'shared/components/Spinner';
import {
	ACTION_TYPES,
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {
	ACTIVITIES_COUNT,
	createOrderIOMap,
	getDefaultSortOrder,
	JOB_TITLE,
	LAST_ACTIVITY_DATE,
	NAME
} from 'shared/util/pagination';
import {addAlert} from 'shared/actions/alerts';
import {Alert} from 'shared/types';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withCurrentUser} from 'shared/hoc';
import {connect, ConnectedProps} from 'react-redux';
import {EntityTypes, SegmentTypes} from 'shared/util/constants';
import {individualsListColumns} from 'shared/util/table-columns';
import {isNil} from 'lodash';
import {List} from 'immutable';
import {OrderByDirections} from 'shared/util/constants';
import {RootState} from 'shared/store';
import {Routes, toRoute} from 'shared/util/router';
import {Segment, User} from 'shared/util/records';
import {sub} from 'shared/util/lang';
import {useQueryPagination, useRequest} from 'shared/hooks';

const getIndividualsDataSource = ({
	channelId,
	delta,
	groupId,
	orderIOMap,
	page,
	query
}) =>
	API.individuals.search({
		channelId,
		delta,
		groupId,
		orderIOMap,
		page,
		query
	});

const connector = connect(
	(store: RootState, {groupId}: {groupId: string}) => ({
		timeZoneId: store.getIn([
			'projects',
			groupId,
			'data',
			'timeZone',
			'timeZoneId'
		])
	}),
	{addAlert, close, open}
);

type PropsFromRedux = ConnectedProps<typeof connector>;

interface IKnownIndividualsProps
	extends React.HTMLAttributes<HTMLDivElement>,
		PropsFromRedux {
	channelId: string;
	currentUser: User;
	groupId: string;
}

const KnownIndividuals: React.FC<IKnownIndividualsProps> = ({
	addAlert,
	channelId,
	close,
	currentUser,
	groupId,
	open,
	timeZoneId
}) => {
	const {selectionDispatch} = useSelectionContext();

	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(NAME, getDefaultSortOrder(NAME))
	});

	const {data: dataSourceData, loading: dataSourceLoading} = useRequest({
		dataSourceFn: API.dataSource.search,
		variables: {
			delta: 1,
			groupId // TODO: Maybe add a default sort here.
		}
	});

	const addToSegment = (
		selectedSegmentsList: List<Segment>,
		idsArray: string[]
	) => {
		const selectedSegmentId = selectedSegmentsList[0].id;

		return API.individualSegment
			.addIndividuals({
				groupId,
				individualIds: idsArray,
				selectedSegmentId
			})
			.then(() => {
				addAlert({
					alertType: Alert.Types.Success,
					message: sub(
						Liferay.Language.get(
							'x-individuals-have-been-added-to-this-static-segment'
						),
						[idsArray.length]
					)
				});

				selectionDispatch({type: ACTION_TYPES.clearAll});
			})
			.catch((error: Error) => {
				addAlert({
					alertType: Alert.Types.Error,
					message: Liferay.Language.get(
						'an-unexpected-error-occurred'
					)
				});

				return error;
			});
	};

	const getStaticIndividualSegments = ({delta, orderIOMap, page, query}) =>
		API.individualSegment.search({
			channelId,
			delta,
			groupId,
			orderIOMap,
			page,
			query,
			segmentType: SegmentTypes.Static
		});

	const handleAddIndividualsToSegmentModal = (idsArray: string[]) => () =>
		open(modalTypes.SELECT_ITEMS_MODAL, {
			countLabel: Liferay.Language.get('x-segments'),
			dataSourceFn: getStaticIndividualSegments,
			entityType: EntityTypes.IndividualsSegment,
			groupId,
			initialOrderIOMap: createOrderIOMap(
				NAME,
				OrderByDirections.Ascending
			),
			noResultsIcon: 'ac-segment',
			noResultsName: Liferay.Language.get('static-segments'),
			onClose: close,
			onSubmit: (selectedSegmentsList: List<Segment>) =>
				addToSegment(selectedSegmentsList, idsArray),
			selectMultiple: false,
			submitMessage: Liferay.Language.get('add'),
			title: Liferay.Language.get('add-to-static-segment')
		});

	const renderNav = selectedItemsIOMap => {
		if (dataSourceData?.total > 0 && !selectedItemsIOMap.isEmpty()) {
			return (
				<Nav>
					<Nav.Item key='PRIMARY_ACTION'>
						<Button
							className='nav-btn'
							display='primary'
							onClick={handleAddIndividualsToSegmentModal(
								selectedItemsIOMap.keySeq().toArray()
							)}
						>
							{Liferay.Language.get('add-to-static-segment')}
						</Button>
					</Nav.Item>
				</Nav>
			);
		}
	};

	const renderNoResults = (query: string, activeFilters: boolean) => {
		const authorized = currentUser.isAdmin();

		const createDataSourceButton = (
			<Button
				display='primary'
				href={toRoute(Routes.SETTINGS_ADD_DATA_SOURCE, {
					groupId
				})}
			>
				{Liferay.Language.get('connect-data-source')}
			</Button>
		);

		if (dataSourceLoading || isNil(dataSourceData?.total)) {
			return (
				<NoResultsDisplay>
					<Spinner key='DATA_SOURCE_SPINNER' overlay />
				</NoResultsDisplay>
			);
		} else if (query || activeFilters) {
			return (
				<NoResultsDisplay
					title={getFormattedTitle(
						Liferay.Language.get('individuals')
					)}
				/>
			);
		} else if (dataSourceData?.total === 0) {
			return (
				<NoResultsDisplay
					description={
						authorized
							? Liferay.Language.get(
									'please-connect-people-data-sources-to-start-using-analytics-cloud'
							  )
							: Liferay.Language.get(
									'please-contact-your-site-administrator-to-add-people-data-sources'
							  )
					}
					primary
					title={Liferay.Language.get('no-data-sources-connected')}
				>
					{authorized && createDataSourceButton}
				</NoResultsDisplay>
			);
		} else {
			return (
				<NoResultsDisplay
					description={
						authorized
							? Liferay.Language.get(
									'please-connect-a-data-source-with-people-data.-if-this-problem-persists-visit-the-documentation-for-connecting-data-sources'
							  )
							: Liferay.Language.get(
									'please-contact-your-site-administrator-to-add-a-data-source-with-people-data'
							  )
					}
					primary
					title={Liferay.Language.get(
						'no-individuals-sycned-from-data-sources'
					)}
				>
					{authorized && createDataSourceButton}
				</NoResultsDisplay>
			);
		}
	};

	return (
		<div className='individuals-dashboard-known-individuals-root'>
			<div className='row'>
				<div className='col-xl-12'>
					<Card pageDisplay>
						<SearchableTableWithStaged
							columns={[
								individualsListColumns.getNameEmail({
									channelId,
									groupId
								}),
								individualsListColumns.jobTitle,
								individualsListColumns.activitiesCount,
								individualsListColumns.getLastActivityDate(
									timeZoneId
								)
							]}
							currentUser={currentUser}
							dataSourceFn={getIndividualsDataSource}
							dataSourceParams={{channelId, groupId}}
							delta={delta}
							entityLabel={Liferay.Language.get('individuals')}
							navRenderer={renderNav}
							noResultsRenderer={renderNoResults}
							orderByOptions={[
								{
									label: Liferay.Language.get('name'),
									value: NAME
								},
								{
									label: Liferay.Language.get('job-title'),
									value: JOB_TITLE
								},
								{
									label: Liferay.Language.get(
										'total-activities'
									),
									value: ACTIVITIES_COUNT
								},
								{
									label: Liferay.Language.get(
										'last-activity'
									),
									value: LAST_ACTIVITY_DATE
								}
							]}
							orderIOMap={orderIOMap}
							page={page}
							query={query}
							showCheckbox
						/>
					</Card>
				</div>
			</div>
		</div>
	);
};

export default compose<any>(
	withCurrentUser,
	connector,
	withSelectionProvider
)(KnownIndividuals);
