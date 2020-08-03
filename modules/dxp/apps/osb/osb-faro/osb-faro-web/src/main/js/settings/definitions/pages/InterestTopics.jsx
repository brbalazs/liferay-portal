import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import BasePage from 'settings/components/BasePage';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Icon from 'shared/components/Icon';
import Nav from 'shared/components/Nav';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import React from 'react';
import SearchableTableWithStaged from 'shared/components/searchable-table-with-staged';
import {
	ACTION_TYPES,
	SelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {addAlert, alertTypes} from 'shared/actions/alerts';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose, withCurrentUser} from 'shared/hoc';
import {connect} from 'react-redux';
import {
	CREATE_DATE,
	KEYWORD,
	paginationConfig,
	paginationDefaults
} from 'shared/util/pagination';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {getDefinitions} from 'shared/util/breadcrumbs';
import {partition} from 'lodash';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {UNAUTHORIZED_ACCESS} from 'shared/util/request';
import {User} from 'shared/util/records';

const INITIAL_PAGE = 1;

const dateFormatter = date => formatUTCDateFromUnix(date, 'll');

function fetchBlockedKeywords({
	delta,
	groupId,
	orderBy,
	orderByField,
	page,
	query
}) {
	return API.blockedKeywords.fetch({
		cur: page,
		delta,
		groupId,
		orderByFields: [
			{
				fieldName:
					orderByField === KEYWORD
						? `${orderByField}.raw`
						: orderByField,
				orderBy
			}
		],
		query
	});
}

export class InterestTopics extends React.Component {
	static contextType = SelectionContext;

	static defaultProps = {
		...paginationDefaults,
		orderByField: KEYWORD
	};

	static propTypes = {
		...paginationConfig,
		addAlert: PropTypes.func.isRequired,
		close: PropTypes.func.isRequired,
		currentUser: PropTypes.instanceOf(User).isRequired,
		maxLength: PropTypes.number,
		open: PropTypes.func.isRequired
	};

	constructor(props) {
		super(props);

		this._tableRef = React.createRef();
	}

	@autobind
	handleInsertModal() {
		const {close, open} = this.props;

		open(modalTypes.INSERT_BLOCKED_KEYWORDS, {
			onClose: close,
			onSubmit: this.handleAddKeywords
		});
	}

	@autobind
	handleAddKeywords(keywords) {
		const {addAlert, close, groupId} = this.props;

		API.blockedKeywords
			.insertMany({groupId, keywords})
			.then(response => {
				const [duplicate, nonDuplicate] = partition(
					response.items,
					({duplicate}) => duplicate
				);

				if (duplicate.length) {
					addAlert({
						alertType: alertTypes.DEFAULT,
						message: `${duplicate
							.map(({keyword}) => keyword)
							.join(', ')} ${Liferay.Language.get(
							'already-belong-to-the-blocklist'
						)}`
					});
				}

				if (nonDuplicate.length) {
					const nonDuplicatedMessage =
						nonDuplicate.length > 1
							? Liferay.Language.get(
									'x-keywords-added-to-the-blocklist'
							  )
							: Liferay.Language.get(
									'x-keyword-added-to-the-blocklist'
							  );

					addAlert({
						alertType: alertTypes.SUCCESS,
						message: sub(
							nonDuplicatedMessage,
							[
								<b key='nonDuplicateCount'>
									{nonDuplicate.length}
								</b>
							],
							false
						)
					});
				}

				this._tableRef.current.reload();

				close();
			})
			.catch(() => {
				addAlert({
					alertType: alertTypes.ERROR,
					message: Liferay.Language.get('error')
				});
			});
	}

	@autobind
	handleDeleteKeyword(ids) {
		return () => {
			const {
				context: {selectionDispatch},
				props: {addAlert, close, groupId, open}
			} = this;

			open(modalTypes.CONFIRMATION_MODAL, {
				message: sub(
					Liferay.Language.get(
						'are-you-sure-you-want-to-delete-x-keywords'
					),
					[<b key='confirmDeleteCount'>{ids.length}</b>],
					false
				),
				modalVariant: 'modal-warning',
				onClose: close,
				onSubmit: () =>
					API.blockedKeywords
						.delete({
							groupId,
							ids
						})
						.then(() => {
							const deletedMessage =
								ids.length > 1
									? Liferay.Language.get(
											'x-keywords-have-been-deleted'
									  )
									: Liferay.Language.get(
											'x-keyword-have-been-deleted'
									  );

							addAlert({
								alertType: alertTypes.SUCCESS,
								message: sub(
									deletedMessage,
									[<b key='deleteCount'>{ids.length}</b>],
									false
								)
							});

							selectionDispatch({type: ACTION_TYPES.clearAll});

							this._tableRef.current.reload();
						})
						.catch(err =>
							addAlert({
								alertType: alertTypes.ERROR,
								message:
									err.message === UNAUTHORIZED_ACCESS
										? Liferay.Language.get(
												'unauthorized-access'
										  )
										: Liferay.Language.get('error'),
								timeout: false
							})
						),
				title: Liferay.Language.get('delete-keyword'),
				titleIcon: 'warning-full'
			});
		};
	}

	@autobind
	renderNav(selectedItemsIOMap) {
		if (selectedItemsIOMap.isEmpty()) {
			return (
				<Nav>
					<Nav.Item>
						<Button
							className='nav-btn'
							display='primary'
							onClick={this.handleInsertModal}
						>
							{Liferay.Language.get('add-keyword')}
						</Button>
					</Nav.Item>
				</Nav>
			);
		} else {
			return (
				<Nav>
					<Button
						borderless
						className='nav-btn'
						display='secondary'
						onClick={this.handleDeleteKeyword(
							selectedItemsIOMap.keySeq().toArray()
						)}
						outline
					>
						<Icon symbol='trash' />
					</Button>
				</Nav>
			);
		}
	}

	@autobind
	renderNoResults(query) {
		const {currentUser, groupId, page} = this.props;

		const authorized = currentUser.isAdmin();

		const connectMessage = authorized
			? Liferay.Language.get(
					'click-the-button-below-to-add-the-first-keywords'
			  )
			: Liferay.Language.get(
					'please-contact-your-site-administrator-to-add-keywords'
			  );

		return query ? (
			<NoResultsDisplay
				icon={{symbol: 'star-o'}}
				title={getFormattedTitle(Liferay.Language.get('keywords'))}
			/>
		) : page > INITIAL_PAGE ? (
			<NoResultsDisplay title={Liferay.Language.get('page-not-found')}>
				<Button
					display='secondary'
					href={toRoute(Routes.SETTINGS_DEFINITIONS_INTEREST_TOPICS, {
						groupId
					})}
				>
					{Liferay.Language.get('back-to-interest-topics')}
				</Button>
			</NoResultsDisplay>
		) : (
			<NoResultsDisplay
				description={connectMessage}
				primary
				title={Liferay.Language.get(
					'you-have-not-added-keywords-to-the-blocklist-yet'
				)}
			>
				{authorized && (
					<Button
						display='secondary'
						onClick={this.handleInsertModal}
					>
						{Liferay.Language.get('add-keyword')}
					</Button>
				)}
			</NoResultsDisplay>
		);
	}

	@autobind
	renderInlineRowActions({data: {id}, itemsSelected}) {
		return (
			<Button
				borderless
				disabled={itemsSelected}
				onClick={this.handleDeleteKeyword([id])}
				size='sm'
			>
				<Icon symbol='trash' />
			</Button>
		);
	}

	renderPageDescription() {
		return (
			<>
				<p>
					{Liferay.Language.get(
						'approximates-what-topics-individuals-are-interested-in-based-on-their-interactions-with-registered-touchpoints'
					)}
					<br />
					{Liferay.Language.get(
						'liferay-analytics-cloud-automatically-associates-registered-touchpoints-with-keywords-based-on-their-meta-tags-titles-and-descriptions'
					)}
				</p>

				<h4>{Liferay.Language.get('keywords-blocklist')}</h4>
				<p>
					{Liferay.Language.get(
						'keywords-can-be-excluded-by-adding-them-to-a-blocklist-manage-the-keywords-that-you-dont-want-listed-in-liferay-analytics-cloud-and-dont-want-them-to-be-used-to-generate-content-recommendation-in-liferay-dxp'
					)}
				</p>
			</>
		);
	}
	render() {
		const {
			currentUser,
			delta,
			filterBy,
			groupId,
			orderBy,
			orderByField,
			page,
			query
		} = this.props;

		return (
			<BasePage
				breadcrumbItems={[
					getDefinitions({groupId}),
					{
						active: true,
						label: Liferay.Language.get('interest-topics')
					}
				]}
				groupId={groupId}
				key='interestTopicsPage'
				pageDescription={this.renderPageDescription()}
				pageTitle={Liferay.Language.get('interest-topics')}
			>
				<Card pageDisplay>
					<SearchableTableWithStaged
						columns={[
							{
								accessor: KEYWORD,
								className: 'table-cell-expand',
								label: Liferay.Language.get('keyword'),
								title: true
							},
							{
								accessor: CREATE_DATE,
								dataFormatter: dateFormatter,
								label: Liferay.Language.get('added')
							}
						]}
						dataSourceFn={fetchBlockedKeywords}
						dataSourceParams={{groupId}}
						delta={Number(delta)}
						entityLabel={Liferay.Language.get('keywords')}
						filterBy={filterBy}
						maxLength={900}
						navRenderer={
							currentUser.isAdmin() ? this.renderNav : null
						}
						noResultsName={Liferay.Language.get('keywords')}
						noResultsRenderer={this.renderNoResults}
						orderBy={orderBy}
						orderByField={orderByField}
						page={Number(page)}
						query={query}
						ref={this._tableRef}
						renderInlineRowActions={
							currentUser.isAdmin()
								? this.renderInlineRowActions
								: null
						}
						rowIdentifier='id'
						showCheckbox={currentUser.isAdmin()}
					/>
				</Card>
			</BasePage>
		);
	}
}

export default compose(
	connect(
		null,
		{addAlert, close, open}
	),
	withCurrentUser,
	withSelectionProvider
)(InterestTopics);
