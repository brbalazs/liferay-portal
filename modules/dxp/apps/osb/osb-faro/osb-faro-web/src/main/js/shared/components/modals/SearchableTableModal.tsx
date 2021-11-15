import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Modal from 'shared/components/modal';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import SearchableTableWithStaged from 'shared/components/searchable-table-with-staged';
import {
	ACTION_TYPES,
	SelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {noop, omit, pickBy} from 'lodash';
import {PropTypes} from 'prop-types';
import {withStatefulPagination} from 'shared/hoc';

const SearchableTable = withStatefulPagination(
	SearchableTableWithStaged,
	({
		delta: defaultDelta,
		orderBy: defaultOrderBy,
		orderByField: defaultOrderByField
	}) =>
		pickBy({
			defaultDelta,
			defaultOrderBy,
			defaultOrderByField
		}),
	props => omit(props, 'onSearchValueChange')
);

class SearchableTableModal extends React.Component {
	static contextType = SelectionContext;

	static defaultProps = {
		checkDisabled: noop,
		columns: [],
		delta: 10,
		instruction: '',
		noResultsIcon: 'ac-individual',
		onClose: noop,
		onSubmit: noop,
		orderByField: '',
		orderByOptions: [],
		requireSelection: true,
		selectedItems: [],
		submitMessage: Liferay.Language.get('submit'),
		title: Liferay.Language.get('select-items')
	};

	static propTypes = {
		checkDisabled: PropTypes.func,
		columns: PropTypes.array,
		dataSourceFn: PropTypes.func.isRequired,
		delta: PropTypes.number,
		entityLabel: PropTypes.string,
		instruction: PropTypes.string,
		noResultsIcon: PropTypes.string,
		onClose: PropTypes.func,
		onSubmit: PropTypes.func,
		orderByField: PropTypes.string,
		orderByOptions: PropTypes.array,
		requireSelection: PropTypes.bool,
		selectedItems: PropTypes.array,
		submitMessage: PropTypes.string,
		title: PropTypes.string
	};

	constructor(props) {
		super(props);

		this._tableRef = React.createRef();
	}

	componentDidMount() {
		const {
			context: {selectionDispatch},
			props: {selectedItems}
		} = this;

		if (selectedItems.length) {
			selectionDispatch({
				payload: {items: selectedItems},
				type: ACTION_TYPES.add
			});
		}
	}

	@autobind
	handleSubmit() {
		const {
			context: {selectedItems: selectedItemsIOMap},
			props: {onSubmit}
		} = this;

		onSubmit(selectedItemsIOMap);
	}

	reload() {
		this._tableRef.current.reload();
	}

	render() {
		const {
			context: {selectedItems: selectedItemsIOMap},
			props: {
				checkDisabled,
				className,
				columns,
				dataSourceFn,
				delta,
				entityLabel,
				instruction,
				noResultsIcon,
				onClose,
				orderByField,
				orderByOptions,
				requireSelection,
				selectedItems,
				submitMessage,
				title,
				...otherProps
			}
		} = this;

		return (
			<Modal className={className} size='lg'>
				<Modal.Header onClose={onClose} title={title} />

				<Modal.Body>
					<div className='text-secondary'>{instruction}</div>
				</Modal.Body>

				<SearchableTable
					{...omitDefinedProps(
						otherProps,
						SearchableTableModal.propTypes
					)}
					autoFocus
					checkDisabled={checkDisabled}
					columns={columns}
					dataSourceFn={dataSourceFn}
					delta={delta}
					entityLabel={entityLabel}
					noResultsIcon={noResultsIcon}
					orderByField={orderByField}
					orderByOptions={orderByOptions}
					ref={this._tableRef}
					rowIdentifier='id'
					selectedItems={selectedItems.map(({id}) => id)}
					selectedItemsIOMap={selectedItemsIOMap}
					showCheckbox
				/>

				<Modal.Footer>
					<Button onClick={onClose}>
						{Liferay.Language.get('cancel')}
					</Button>

					<Button
						disabled={requireSelection && !selectedItemsIOMap.size}
						display='primary'
						onClick={this.handleSubmit}
					>
						{submitMessage}
					</Button>
				</Modal.Footer>
			</Modal>
		);
	}
}

export default withSelectionProvider(SearchableTableModal);
