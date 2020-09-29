import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import EntityRowActions from './EntityRowActions';
import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import Promise from 'metal-promise';
import React from 'react';
import SearchableTableWithAdded from './SearchableTableWithAdded';
import ToolbarActionsRenderer from './ToolbarActionsRenderer';
import {buildOrderByFields, NAME} from 'shared/util/pagination';
import {Changeset} from 'shared/util/records';
import {close, modalTypes, open} from 'shared/actions/modals';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {INDIVIDUALS} from 'shared/util/router';
import {individualsListColumns} from 'shared/util/table-columns';
import {Map} from 'immutable';
import {PropTypes} from 'prop-types';
import {
	SelectionContext,
	SelectionProvider,
	withSelectionProvider
} from 'shared/context/selection';
import {sub} from 'shared/util/lang';

const ORDER_BY_OPTIONS = [
	{
		label: Liferay.Language.get('name'),
		value: 'name'
	},
	{
		label: Liferay.Language.get('first-seen'),
		value: 'dateCreated'
	}
];

export class SegmentEditStatic extends React.Component {
	static contextType = SelectionContext;

	static propTypes = {
		changeset: PropTypes.instanceOf(Changeset),
		channelId: PropTypes.string,
		close: PropTypes.func.isRequired,
		entityLabel: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string,
		membershipCount: PropTypes.number,
		open: PropTypes.func.isRequired,
		timeZoneId: PropTypes.string
	};

	state = {
		showAdded: false,
		total: 0
	};

	constructor(props) {
		super(props);

		this._tableRef = React.createRef();
	}

	componentDidUpdate() {
		const {
			props: {changeset},
			state: {showAdded}
		} = this;

		if (changeset.added.isEmpty() && showAdded) {
			this.setState({showAdded: false});
		}
	}

	fetchAccounts() {
		return Promise.resolve({items: [], total: 0});
	}

	getColumns() {
		const {timeZoneId} = this.props;

		return [
			individualsListColumns.name,
			individualsListColumns.getDateCreated(timeZoneId)
		];
	}

	getIndividuals({delta, orderBy, orderByField, page, query}) {
		const {channelId, groupId, id} = this.props;

		const params = {
			channelId,
			cur: page,
			delta,
			groupId,
			orderByFields: buildOrderByFields(
				{field: orderByField, sortOrder: orderBy},
				INDIVIDUALS
			),
			query
		};

		return API.individuals.search(
			id ? {notIndividualSegmentId: id, ...params} : params
		);
	}

	getMembership(data) {
		const {delta, groupId, id, orderBy, orderByField, page, query} = data;

		return id
			? API.individuals.fetchMembership({
					cur: page,
					delta,
					groupId,
					individualSegmentId: id,
					orderByFields: this.getOrderByFields(orderBy, orderByField),
					query
			  })
			: Promise.resolve({items: [], total: 0});
	}

	@autobind
	fetchModalData(data) {
		return this.getIndividuals(data);
	}

	@autobind
	fetchResults(data) {
		return this.getMembership(data);
	}

	@autobind
	getOrderByFields(orderBy, orderByField) {
		const system = orderByField === 'dateCreated';

		if (orderByField && orderByField !== 'name') {
			return [
				{
					fieldName: orderByField,
					orderBy,
					system
				}
			];
		} else {
			return [
				{
					fieldName: 'givenName',
					orderBy,
					system
				},
				{
					fieldName: 'familyName',
					orderBy,
					system
				}
			];
		}
	}

	@autobind
	handleAddEntitiesModal() {
		const {close, entityLabel, groupId, open} = this.props;

		open(modalTypes.SEARCHABLE_TABLE_MODAL, {
			checkDisabled: this.isCurrentMember,
			columns: this.getColumns(),
			dataSourceFn: this.fetchModalData,
			entityLabel,
			groupId,
			instruction: sub(
				Liferay.Language.get('select-x-to-add-to-your-static-segment'),
				[entityLabel]
			),
			onClose: close,
			onSubmit: itemsIOMap => {
				this.handleStageEntityAdditions(itemsIOMap);

				close();
			},
			orderByField: NAME,
			orderByOptions: ORDER_BY_OPTIONS,
			submitMessage: Liferay.Language.get('add'),
			title: Liferay.Language.get('add-members')
		});
	}

	@autobind
	handleStageEntityAdditions(itemsIOMap) {
		const {changeset, onChange} = this.props;

		onChange(changeset.update('added', added => added.merge(itemsIOMap)));

		this._tableRef.current.reload();
	}

	@autobind
	handleStageEntityRemoval(itemsIOMap) {
		const {changeset, onChange} = this.props;

		onChange(
			changeset.update('removed', removed => removed.merge(itemsIOMap))
		);
	}

	@autobind
	handleToggleShowAdded() {
		this.setState({
			showAdded: !this.state.showAdded
		});
	}

	@autobind
	handleUndoAllAdded() {
		this.props.onChange(
			this.props.changeset.update('added', () => new Map())
		);

		this._tableRef.current.reload();
	}

	@autobind
	handleUndoChanges(itemsIOMap) {
		const {changeset, onChange} = this.props;

		onChange(
			new Changeset({
				added: changeset.added.filter(item => !itemsIOMap.has(item.id)),
				removed: changeset.removed.filter(
					item => !itemsIOMap.has(item.id)
				)
			})
		);

		this._tableRef.current.reload();
	}

	@autobind
	isCurrentMember({id}) {
		const {changeset} = this.props;

		return changeset.added.has(id);
	}

	@autobind
	renderNav(selectedItemsIOMap, items) {
		const {
			props: {id},
			state: {showAdded}
		} = this;

		return (
			<ToolbarActionsRenderer
				buttonDisplay={id ? 'secondary' : 'primary'}
				items={items}
				itemsSelected={!selectedItemsIOMap.isEmpty()}
				onClick={this.handleAddEntitiesModal}
				onSelectedClick={this.handleStageEntityRemoval}
				onUndoChanges={this.handleUndoChanges}
				selectedItemsIOMap={selectedItemsIOMap}
				showAdded={showAdded}
			/>
		);
	}

	@autobind
	renderInlineRowActions({data, items, itemsSelected}) {
		const {
			props: {changeset, onChange},
			state: {showAdded}
		} = this;

		return (
			/* eslint-disable react/jsx-handler-names */
			<EntityRowActions
				addIdsISet={changeset.added.keySeq().toSet()}
				data={data}
				itemsIMap={new Map(items.map(item => [item.id, item]))}
				itemsSelected={itemsSelected}
				onRowDelete={() => {
					onChange(
						changeset.update('removed', removed =>
							removed.set(data.id, data)
						)
					);
				}}
				onUndoChanges={([id]) => {
					const undoAdd = changeset.added.has(id);

					onChange(
						new Changeset({
							added: changeset.added.delete(id),
							removed: undoAdd
								? changeset.removed
								: changeset.removed.delete(id)
						})
					);

					undoAdd && this._tableRef.current.reload();
				}}
				removeIdsISet={changeset.removed.keySeq().toSet()}
				showAdded={showAdded}
			/>
			/* eslint-enable react/jsx-handler-names */
		);
	}

	render() {
		const {
			props: {
				changeset,
				className,
				entityLabel,
				groupId,
				id,
				membershipCount,
				...otherProps
			},
			state: {showAdded}
		} = this;

		return (
			<div
				className={getCN(
					'segment-edit-static-root d-flex flex-column flex-grow-1',
					className
				)}
			>
				<div className='select-items-header'>
					<h3>
						{!!membershipCount &&
							sub(Liferay.Language.get('manage-x-members'), [
								membershipCount
							])}
					</h3>
				</div>

				<SelectionProvider>
					<SearchableTableWithAdded
						{...omitDefinedProps(
							otherProps,
							SegmentEditStatic.propTypes
						)}
						addedItemsIOMap={changeset.added.toOrderedMap()}
						className='d-flex flex-column flex-grow-1'
						columns={this.getColumns()}
						dataSourceFn={this.fetchResults}
						dataSourceParams={{
							groupId,
							id,
							showAdded
						}}
						defaultOrderByField={NAME}
						entityLabel={entityLabel}
						navRenderer={this.renderNav}
						noResultsIcon='ac-individual'
						onShowStagedToggle={this.handleToggleShowAdded}
						onUndoChanges={this.handleUndoAllAdded}
						orderByOptions={ORDER_BY_OPTIONS}
						ref={this._tableRef}
						renderInlineRowActions={this.renderInlineRowActions}
						rowIdentifier='id'
						showCheckbox
						showStaged={showAdded}
					/>
				</SelectionProvider>
			</div>
		);
	}
}

export default compose(
	connect(
		(store, {groupId}) => ({
			timeZoneId: store.getIn([
				'projects',
				groupId,
				'data',
				'timeZone',
				'timeZoneId'
			])
		}),
		{close, open}
	),
	withSelectionProvider
)(SegmentEditStatic);
