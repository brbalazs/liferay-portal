import * as API from 'shared/api';
import BackButton from 'contacts/components/BackButton.tsx';
import Card from 'shared/components/Card';
import FaroConstants from 'shared/util/constants';
import InterestPagesList from 'contacts/components/InterestPagesList';
import Nav from 'shared/components/Nav';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {Account, Segment} from 'shared/util/records';
import {
	ACCOUNTS,
	INDIVIDUALS,
	PAGES,
	Routes,
	SEGMENTS,
	setUriQueryValue,
	toRoute
} from 'shared/util/router';
import {
	buildOrderByFields,
	NAME,
	paginationConfig,
	paginationDefaults
} from 'shared/util/pagination';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {individualsListColumns} from 'shared/util/table-columns';
import {omit} from 'lodash';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';

const {
	pagination: {orderAscending}
} = FaroConstants;

const tabIds = {
	INDIVIDUALS,
	PAGES
};

function fetchIndividuals({orderBy, orderByField, ...otherParams}) {
	return API.individuals.search({
		...otherParams,
		orderByFields: buildOrderByFields(
			{field: orderByField, sortOrder: orderBy},
			INDIVIDUALS
		)
	});
}

const IndividualsList = ({
	channelId,
	groupId,
	orderBy = orderAscending,
	orderByField = NAME,
	...otherProps
}) => (
	<SearchableEntityTable
		columns={[
			individualsListColumns.getName({channelId, groupId}),
			individualsListColumns.email,
			individualsListColumns.accountNames
		]}
		dataSourceFn={fetchIndividuals}
		entityLabel={Liferay.Language.get('individuals')}
		orderBy={orderBy}
		orderByField={orderByField}
		orderByOptions={[
			{
				label: Liferay.Language.get('name'),
				value: NAME
			}
		]}
		rowIdentifier='id'
		{...otherProps}
	/>
);

const InterestDetailsList = ({tabId, ...otherProps}) => {
	if (tabId == INDIVIDUALS) {
		return <IndividualsList {...otherProps} />;
	} else {
		return <InterestPagesList {...otherProps} />;
	}
};

export default class BaseInterestDetails extends React.Component {
	static defaultProps = {
		...omit(paginationDefaults, ['orderBy', 'orderByField']),
		active: 'true',
		tabId: tabIds.INDIVIDUALS
	};

	static propTypes = {
		...paginationConfig,
		active: PropTypes.string,
		channelId: PropTypes.string,
		entity: PropTypes.oneOfType([
			PropTypes.instanceOf(Account),
			PropTypes.instanceOf(Segment)
		]).isRequired,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		interestDetailsRoute: PropTypes.string.isRequired,
		interestId: PropTypes.string.isRequired,
		orderByField: PropTypes.string,
		type: PropTypes.oneOf([ACCOUNTS, SEGMENTS])
	};

	getNavigationItems() {
		const {
			channelId,
			groupId,
			id,
			interestDetailsRoute,
			interestId,
			tabId
		} = this.props;

		const active = this.props.active === 'true';

		return [
			{
				active: tabIds.INDIVIDUALS === tabId,
				href: toRoute(interestDetailsRoute, {
					channelId,
					groupId,
					id,
					interestId,
					tabId: tabIds.INDIVIDUALS
				}),
				label: Liferay.Language.get('individuals')
			},
			{
				active: tabIds.PAGES === tabId && active,
				href: setUriQueryValue(
					toRoute(interestDetailsRoute, {
						channelId,
						groupId,
						id,
						interestId,
						tabId: PAGES
					}),
					'active',
					true
				),
				label: Liferay.Language.get('active-pages')
			},
			{
				active: tabIds.PAGES === tabId && !active,
				href: setUriQueryValue(
					toRoute(interestDetailsRoute, {
						channelId,
						groupId,
						id,
						interestId,
						tabId: PAGES
					}),
					'active',
					false
				),
				label: Liferay.Language.get('inactive-pages')
			}
		];
	}

	render() {
		const {
			active,
			channelId,
			delta,
			entity,
			filterBy,
			groupId,
			id,
			interestId,
			orderBy,
			orderByField,
			page,
			query,
			tabId,
			type
		} = this.props;

		const interestName = decodeURIComponent(interestId);

		const individualsEntityKey =
			type === ACCOUNTS ? 'accountId' : 'individualSegmentId';

		let dataSourceParams = {
			contactsEntityId: id,
			contactsEntityType: entity.type,
			groupId,
			[individualsEntityKey]: id,
			interestName
		};

		if (tabId === tabIds.PAGES) {
			dataSourceParams = {
				...dataSourceParams,
				active: active === 'true'
			};
		}

		return (
			<div className='interest-details-root'>
				<BackButton
					href={toRoute(Routes.CONTACTS_INTERESTS, {
						channelId,
						groupId,
						id,
						type
					})}
					label={Liferay.Language.get('back-to-interests')}
				/>

				<Card pageDisplay>
					<Card.Header>
						<Card.Title>
							{sub(
								Liferay.Language.get('interest-x'),
								[
									<span
										className='interest-name'
										key='INTEREST_NAME'
									>
										{interestName}
									</span>
								],
								false
							)}
						</Card.Title>
					</Card.Header>

					<Card.Header>
						<Nav
							className='page-subnav'
							display='underline'
							key='subnav'
						>
							{this.getNavigationItems().map(
								({active, href, label}) => (
									<Nav.Item
										active={active}
										href={href}
										key={label}
									>
										<h4>{label}</h4>
									</Nav.Item>
								)
							)}
						</Nav>

						<h4 className='list-title'>
							{tabId === INDIVIDUALS
								? sub(
										Liferay.Language.get(
											'members-interested-in-x-as-of-x'
										),
										[
											interestName,
											formatUTCDateFromUnix(Date.now())
										]
								  )
								: sub(
										Liferay.Language.get(
											'pages-containing-x-as-a-keyword'
										),
										[interestName]
								  )}
						</h4>
					</Card.Header>

					<InterestDetailsList
						className='interest-history-table d-flex flex-column flex-grow-1'
						dataSourceParams={dataSourceParams}
						delta={Number(delta)}
						filterBy={filterBy}
						groupId={groupId}
						orderBy={orderBy}
						orderByField={orderByField}
						page={Number(page)}
						query={query}
						tabId={tabId}
					/>
				</Card>
			</div>
		);
	}
}
