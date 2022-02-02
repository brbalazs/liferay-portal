import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import EmbeddedAlertList, {
	IEmbeddedAlertListProps
} from 'shared/components/EmbeddedAlertList';
import Nav from 'shared/components/Nav';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import StatesRenderer from 'shared/components/states-renderer/StatesRenderer';
import URLConstants from 'shared/util/url-constants';
import {FetchSegmentsParams} from 'segment/pages/List';
import {FilterOptionType} from 'shared/types';
import {get} from 'lodash';
import {NAME} from 'shared/util/pagination';
import {Routes, toRoute} from 'shared/util/router';
import {useChannelContext} from 'shared/context/channel';
import {useDataSource} from 'shared/hooks/useDataSource';
import {useParams} from 'react-router-dom';
import {User} from 'shared/util/records';

interface IBaseListPageProps {
	alerts?: IEmbeddedAlertListProps[];
	className?: string;
	columns: {
		accessor: string;
		label: any;
	}[];
	currentUser: User;
	dataSourceFn: (params: FetchSegmentsParams) => any;
	delta?: number;
	emptyStateTitle?: string;
	entityLabel?: string;
	filterBy?: object;
	filterByOptions?: FilterOptionType[];
	hideNav?: boolean;
	icon?: string;
	noResultsConfig?: {
		content?: any;
		description?: string;
		title?: string;
	};
	orderByOptions?: {label: string; value: string}[];
	orderIOMap?: object;
	page?: number;
	pageActions?: any[];
	pageActionsLabel?: string;
	query?: string;
	ref?: React.RefObject<SearchableEntityTable>;
	renderRowActions?: any;
	renderSelectedAction?: (checkedItemsISet) => any;
	rowIdentifier?: string;
	showCheckbox?: boolean;
}

const BaseListPage: React.FC<IBaseListPageProps> = ({
	alerts = [],
	className,
	columns,
	currentUser,
	dataSourceFn,
	delta,
	emptyStateTitle = Liferay.Language.get('no-data-sources-connected'),
	entityLabel,
	filterBy,
	filterByOptions,
	hideNav = false,
	icon,
	noResultsConfig,
	orderByOptions = [
		{
			label: Liferay.Language.get('name'),
			value: NAME
		}
	],
	orderIOMap,
	page,
	pageActions,
	pageActionsLabel,
	query,
	renderSelectedAction,
	rowIdentifier = 'id',
	showCheckbox = false,
	...otherProps
}) => {
	const {selectedChannel} = useChannelContext();
	const {channelId, groupId} = useParams();
	const _tableRef = React.createRef<SearchableEntityTable>();
	const authorized = currentUser.isAdmin();

	const dataSourceStates = useDataSource();
	const {empty, error, loading} = dataSourceStates;

	const renderNav = checkedItemsISet => {
		if (!hideNav && renderSelectedAction && !checkedItemsISet.isEmpty()) {
			return (
				<Nav>
					<Nav.Item key='PRIMARY_ACTION'>
						{renderSelectedAction(checkedItemsISet)}
					</Nav.Item>
				</Nav>
			);
		}
	};

	const renderNoResults = (query, activeFilters) => {
		const createDataSourceButton = (
			<Button
				display='primary'
				href={toRoute(Routes.SETTINGS_ADD_DATA_SOURCE, {
					channelId,
					groupId
				})}
			>
				{Liferay.Language.get('connect-data-source')}
			</Button>
		);

		if (query || activeFilters) {
			return (
				<NoResultsDisplay
					icon={icon && {symbol: icon}}
					title={getFormattedTitle(entityLabel)}
				/>
			);
		} else {
			return (
				<NoResultsDisplay
					description={get(noResultsConfig, 'description')}
					primary
					title={get(noResultsConfig, 'title')}
				>
					{get(noResultsConfig, 'content') ||
						(authorized && createDataSourceButton)}
				</NoResultsDisplay>
			);
		}
	};

	return (
		<BasePage className={className} documentTitle={entityLabel}>
			<BasePage.Header
				breadcrumbs={[
					breadcrumbs.getHome({
						channelId,
						groupId,
						label: selectedChannel && selectedChannel.name
					})
				]}
				groupId={groupId}
			>
				<BasePage.Row>
					<BasePage.Header.TitleSection title={entityLabel} />

					<BasePage.Header.Section>
						<BasePage.Header.PageActions
							actions={pageActions}
							disabled={empty || error || loading}
							label={pageActionsLabel}
						/>
					</BasePage.Header.Section>
				</BasePage.Row>
			</BasePage.Header>

			<BasePage.Body>
				<EmbeddedAlertList alerts={alerts} />

				<Card pageDisplay>
					<Card.Body noPadding>
						<StatesRenderer {...dataSourceStates}>
							<StatesRenderer.Empty
								className='sites-dashboard bg-white mt-4 py-5'
								description={
									authorized ? (
										<>
											{Liferay.Language.get(
												'connect-a-data-source-to-get-started'
											)}

											<a
												className='pl-1'
												href={
													URLConstants.DataSourceConnection
												}
												key='DOCUMENTATION'
												target='_blank'
											>
												{Liferay.Language.get(
													'access-our-documentation-to-learn-more'
												)}
											</a>
										</>
									) : (
										Liferay.Language.get(
											'please-contact-your-site-administrator-to-add-people-data-sources'
										)
									)
								}
								title={emptyStateTitle}
							/>

							<StatesRenderer.Success>
								<SearchableEntityTable
									{...otherProps}
									columns={columns}
									dataSourceFn={dataSourceFn}
									dataSourceParams={{channelId, groupId}}
									delta={delta}
									entityLabel={entityLabel}
									filterBy={filterBy}
									filterByOptions={filterByOptions}
									navRenderer={renderNav}
									noResultsRenderer={renderNoResults}
									orderByOptions={orderByOptions}
									orderIOMap={orderIOMap}
									page={page}
									query={query}
									ref={_tableRef}
									rowIdentifier={rowIdentifier}
									showCheckbox={showCheckbox}
								/>
							</StatesRenderer.Success>
						</StatesRenderer>
					</Card.Body>
				</Card>
			</BasePage.Body>
		</BasePage>
	);
};

export default BaseListPage;
