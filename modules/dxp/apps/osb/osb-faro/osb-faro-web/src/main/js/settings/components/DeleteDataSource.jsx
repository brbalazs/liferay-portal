import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import FaroConstants from 'shared/util/constants';
import Form, {validateInputMessage} from 'shared/components/form';
import getCN from 'classnames';
import React from 'react';
import Sheet from 'shared/components/Sheet';
import TextTruncate from 'shared/components/TextTruncate';
import {
	accountsListColumns,
	assetsListColumns,
	individualsListColumns,
	pagesListColumns,
	segmentsListColumns
} from 'shared/util/table-columns';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {DataSource} from 'shared/util/records';
import {getRouteName} from 'shared/util/router';
import {getTypeLangKey, sub} from 'shared/util/lang';
import {noop} from 'lodash/fp';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

const {
	assetTypes,
	entityTypes: {account, asset, individual, individualsSegment, page}
} = FaroConstants;

/**
 * Get the API for the specific entityType.
 * @param {number} entityType
 * @returns {function} - API function.
 */
function getEntityApi(entityType) {
	switch (entityType) {
		case account:
			return API.accounts.search;
		case asset:
			return params =>
				API.assets.search({assetType: assetTypes.asset, ...params});
		case individual:
			return API.individuals.search;
		case page:
			return params =>
				API.assets.search({assetType: assetTypes.webPage, ...params});
		case individualsSegment:
		default:
			return API.individualSegment.search;
	}
}

/**
 * Get the data source function for entity's modal.
 * @param {number} entityType
 * @returns {function} - Data source function.
 */
function getDataSourceFn(entityType) {
	const entityApi = getEntityApi(entityType);

	return ({dataSourceId, delta, groupId, orderByFields, page, query}) =>
		entityApi({
			dataSourceId,
			delta,
			groupId,
			orderByFields,
			page,
			query
		});
}

/**
 * Get the table columns for entity's modal.
 * @param {number} entityType
 * @param {string} timeZoneId
 * @returns {array}
 */
function getEntityColumns(entityType, timeZoneId) {
	switch (entityType) {
		case account:
			return [
				accountsListColumns.name,
				accountsListColumns.type,
				accountsListColumns.individualCount,
				accountsListColumns.activitiesCount,
				accountsListColumns.engagementScore
			];
		case asset:
			return [
				assetsListColumns.name,
				assetsListColumns.canonicalUrl,
				assetsListColumns.type
			];
		case individual:
			return [
				individualsListColumns.name,
				individualsListColumns.jobTitle,
				individualsListColumns.activitiesCount,
				individualsListColumns.engagementScore,
				individualsListColumns.getLastActivityDate(timeZoneId),
				individualsListColumns.willBeRemoved
			];
		case page:
			return [pagesListColumns.name, pagesListColumns.canonicalUrl];
		case individualsSegment:
		default:
			return [
				segmentsListColumns.name,
				segmentsListColumns.individualCount,
				segmentsListColumns.activitiesCount,
				segmentsListColumns.engagementScore,
				segmentsListColumns.getOwnerName(timeZoneId)
			];
	}
}

/**
 * Get the title for the entity's modal.
 * @param {number} entityType
 * @param {string} dataSourceName
 * @returns {string}
 */
function getEntityTitle(entityType, dataSourceName) {
	const TruncatedName = () => (
		<TextTruncate inline maxCharLength={50} title={dataSourceName} />
	);

	switch (entityType) {
		case account:
			return sub(
				Liferay.Language.get('x-s-accounts'),
				[<TruncatedName key='NAME' />],
				false
			);
		case asset:
			return sub(
				Liferay.Language.get('x-s-assets'),
				[<TruncatedName key='NAME' />],
				false
			);
		case individual:
			return sub(
				Liferay.Language.get('x-s-individuals'),
				[<TruncatedName key='NAME' />],
				false
			);
		case page:
			return sub(
				Liferay.Language.get('x-s-pages'),
				[<TruncatedName key='NAME' />],
				false
			);
		case individualsSegment:
		default:
			return Liferay.Language.get('related-segments');
	}
}

class DataSourceItem extends React.Component {
	static defaultProps = {
		onClick: noop
	};

	static propTypes = {
		entityType: PropTypes.number.isRequired,
		onClick: PropTypes.func,
		secondaryInfo: PropTypes.string.isRequired,
		title: PropTypes.string.isRequired
	};

	@autobind
	handleClick() {
		const {entityType, onClick} = this.props;

		onClick(entityType);
	}

	render() {
		const {secondaryInfo, title} = this.props;

		return (
			<div
				className={`entity-item${
					this.props.className ? ` ${this.props.className}` : ''
				}`}
			>
				<Button
					className='title'
					display='unstyled'
					onClick={this.handleClick}
				>
					<h5>{title}</h5>
				</Button>

				<div className='secondary-info'>{secondaryInfo}</div>
			</div>
		);
	}
}

export class DeleteDataSource extends React.Component {
	static propTypes = {
		actionRequestFn: PropTypes.func.isRequired,
		close: PropTypes.func.isRequired,
		dataSource: PropTypes.instanceOf(DataSource).isRequired,
		deleteMessage: PropTypes.string.isRequired,
		deletePhrase: PropTypes.string.isRequired,
		entitiesCount: PropTypes.object,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		open: PropTypes.func.isRequired,
		pageActionText: PropTypes.string.isRequired,
		timeZoneId: PropTypes.string
	};

	state = {
		valid: false
	};

	@autobind
	handleDeleteDataSource(_, {setSubmitting}) {
		const {actionRequestFn, close, open, pageActionText} = this.props;

		open(modalTypes.CONFIRMATION_MODAL, {
			message: (
				<div>
					<h4 className='text-secondary'>
						{sub(
							Liferay.Language.get('are-you-sure-you-want-to-x'),
							[pageActionText.toLowerCase()]
						)}
					</h4>

					<b>
						{Liferay.Language.get(
							'you-will-permanently-lose-all-contacts-and-analytics-data-collected-from-this-data-source.-you-will-not-be-able-to-undo-this-action'
						)}
					</b>
				</div>
			),
			modalVariant: 'modal-warning',
			onClose: close,
			onSubmit: actionRequestFn,
			submitButtonDisplay: 'warning',
			submitMessage: pageActionText,
			title: pageActionText,
			titleIcon: 'warning'
		});

		setSubmitting(false);
	}

	@autobind
	handleEntityModal(entityType) {
		const {
			close,
			dataSource: {name},
			groupId,
			id,
			open,
			timeZoneId
		} = this.props;

		open(modalTypes.SEARCHABLE_ENTITIES_TABLE_MODAL, {
			columns: getEntityColumns(entityType, timeZoneId),
			dataSourceFn: getDataSourceFn(entityType),
			dataSourceParams: {dataSourceId: id, groupId},
			entityLabel: getTypeLangKey(entityType),
			entityType: getRouteName(entityType),
			onClose: close,
			rowIdentifier: 'id',
			title: getEntityTitle(entityType, name)
		});
	}

	renderDataSourceItems() {
		const {entitiesCount} = this.props;

		const items = [
			{
				entityType: individualsSegment,
				secondaryInfo: Liferay.Language.get(
					'segments-with-criteria-related-to-this-data-source-will-be-disabled-until-the-criteria-is-updated'
				),
				title: sub(Liferay.Language.get('x-segments'), [
					entitiesCount[individualsSegment].toLocaleString()
				])
			},
			{
				entityType: account,
				secondaryInfo: sub(
					Liferay.Language.get(
						'all-attributes-related-to-an-x-from-this-data-source-will-be-removed,-which-may-result-in-the-removal-of-the-x'
					),
					[Liferay.Language.get('account')]
				),
				title: sub(Liferay.Language.get('x-accounts'), [
					entitiesCount[account].toLocaleString()
				])
			},
			{
				entityType: individual,
				secondaryInfo: sub(
					Liferay.Language.get(
						'all-attributes-related-to-an-x-from-this-data-source-will-be-removed,-which-may-result-in-the-removal-of-the-x'
					),
					[Liferay.Language.get('individual')]
				),
				title: sub(Liferay.Language.get('x-individuals'), [
					entitiesCount[individual].toLocaleString()
				])
			},
			{
				entityType: page,
				secondaryInfo: sub(
					Liferay.Language.get(
						'all-x-and-related-behaviors-for-both-known-and-anonymous-individuals-will-be-deleted'
					),
					[Liferay.Language.get('pages')]
				),
				title: sub(Liferay.Language.get('x-pages'), [
					entitiesCount[page].toLocaleString()
				])
			},
			{
				entityType: asset,
				secondaryInfo: sub(
					Liferay.Language.get(
						'all-x-and-related-behaviors-for-both-known-and-anonymous-individuals-will-be-deleted'
					),
					[Liferay.Language.get('assets')]
				),
				title: sub(Liferay.Language.get('x-assets'), [
					entitiesCount[asset].toLocaleString()
				])
			}
		];

		return items.map((params, i) => (
			<DataSourceItem
				{...params}
				key={i}
				onClick={this.handleEntityModal}
			/>
		));
	}

	render() {
		const {
			props: {
				className,
				dataSource: {name},
				deleteMessage,
				deletePhrase,
				groupId,
				id,
				pageActionText
			}
		} = this;

		return (
			<div className={getCN('delete-data-source-root', className)}>
				<div>{this.renderDataSourceItems()}</div>

				<Form
					initialValues={{delete: ''}}
					onSubmit={this.handleDeleteDataSource}
				>
					{({handleSubmit, isSubmitting, isValid}) => (
						<Form.Form data-testid='form' onSubmit={handleSubmit}>
							<Sheet.Body>
								<div>{deleteMessage}</div>

								<div className='copy-container'>
									<h4>
										{sub(
											Liferay.Language.get(
												'copy-the-following-x'
											),
											[
												<span
													className='copy-text'
													key='COPY_TEXT'
												>
													{sub(deletePhrase, [name])}
												</span>
											],
											false
										)}
									</h4>
								</div>

								<Form.Input
									data-testid='confirmation-input'
									name='delete'
									validate={validateInputMessage(
										sub(deletePhrase, [name])
									)}
								/>
							</Sheet.Body>

							<Sheet.Footer divider={false}>
								<Button
									className='delete-button'
									disabled={!isValid || isSubmitting}
									display='warning'
									type='submit'
								>
									{pageActionText}
								</Button>

								<Button
									href={toRoute(Routes.SETTINGS_DATA_SOURCE, {
										groupId,
										id
									})}
								>
									{Liferay.Language.get('cancel')}
								</Button>
							</Sheet.Footer>
						</Form.Form>
					)}
				</Form>
			</div>
		);
	}
}

export default connect(
	(store, {groupId}) => ({
		timeZoneId: store.getIn([
			'projects',
			groupId,
			'data',
			'timeZone',
			'timeZoneId'
		])
	}),
	{
		close,
		open
	}
)(DeleteDataSource);
