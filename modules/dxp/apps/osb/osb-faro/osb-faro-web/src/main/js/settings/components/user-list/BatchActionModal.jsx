import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Dropdown from 'shared/components/Dropdown';
import getCN from 'classnames';
import Modal from 'shared/components/modal';
import React from 'react';
import Table from 'shared/components/table';
import {
	ACTION_TYPES,
	SelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {fromJS, List, Set} from 'immutable';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';

class BatchActionModal extends React.Component {
	static contextType = SelectionContext;

	static defaultProps = {
		checkedItemsISet: new Set(),
		columns: [],
		editableAttr: '',
		fitContent: false,
		items: [],
		onClose: noop,
		onSave: noop,
		title: ''
	};

	static propTypes = {
		actionOptions: PropTypes.shape({
			actionCountString: PropTypes.string,
			options: PropTypes.array,
			optionsLabel: PropTypes.string
		}),
		checkedItemsISet: PropTypes.instanceOf(Set),
		columns: PropTypes.array,
		editableAttr: PropTypes.string,
		fitContent: PropTypes.bool,
		items: PropTypes.array,
		onClose: PropTypes.func,
		onSave: PropTypes.func,
		title: PropTypes.string
	};

	state = {
		itemsIList: new List(),
		selectedKey: ''
	};

	constructor(props) {
		super(props);

		const {
			actionOptions: {optionsLabel},
			items
		} = this.props;

		this.state = {
			...this.state,
			itemsIList: fromJS(items),
			selectedKey: optionsLabel
		};
	}

	componentDidMount() {
		const {
			context: {selectionDispatch},
			props: {items}
		} = this;

		items.length &&
			selectionDispatch({payload: {items}, type: ACTION_TYPES.add});
	}

	@autobind
	handleEdits(event) {
		const newVal = event.target.value;

		const {
			context: {selectedItems: selectedItemsIOMap},
			props: {editableAttr},
			state: {itemsIList}
		} = this;

		const updatedItemsIList = itemsIList.map(itemIMap =>
			selectedItemsIOMap.has(itemIMap.get('id'))
				? itemIMap.set(editableAttr, newVal)
				: itemIMap
		);

		this.setState({
			itemsIList: updatedItemsIList,
			selectedKey: newVal
		});
	}

	@autobind
	handleItemsChange(item) {
		const {selectionDispatch} = this.context;

		selectionDispatch({payload: {item}, type: ACTION_TYPES.toggle});
	}

	@autobind
	handleSave() {
		const {
			context: {selectedItems: selectedItemsIOMap},
			props: {editableAttr, onClose, onSave},
			state: {selectedKey}
		} = this;

		onSave({
			edits: {[editableAttr]: selectedKey},
			ids: selectedItemsIOMap.keySeq().toArray()
		});

		onClose();
	}

	render() {
		const {
			context: {selectedItems: selectedItemsIOMap},
			props: {
				actionOptions: {
					actionCountString = '',
					options = [],
					optionsLabel = ''
				},
				className,
				columns,
				fitContent,
				onClose,
				title
			},
			state: {itemsIList, selectedKey}
		} = this;

		const contentClasses = getCN(
			className,
			'scroll-container',
			'batch-action-modal-root',
			{
				'fit-content': fitContent
			}
		);

		const messageStr = sub(
			actionCountString,
			[<b key='selectedCount'>{selectedItemsIOMap.size}</b>],
			false
		);

		return (
			<Modal className={contentClasses} size='lg'>
				<Modal.Header onClose={onClose} title={title} />

				<Modal.Body>
					<div>
						<Dropdown label={selectedKey}>
							{options.map(option => (
								<Dropdown.Item
									hideOnClick
									key={option.value}
									onClick={this.handleEdits}
									value={option.value}
								>
									{option.label}
								</Dropdown.Item>
							))}
						</Dropdown>

						<p className='text-secondary'>{messageStr}</p>
					</div>

					<Table
						columns={columns}
						items={itemsIList.toJS()}
						onSelectItemsChange={this.handleItemsChange}
						rowIdentifier='id'
						selectedItemsIOMap={selectedItemsIOMap}
						showCheckbox
					/>
				</Modal.Body>

				<Modal.Footer>
					<Button onClick={onClose}>
						{Liferay.Language.get('cancel')}
					</Button>

					<Button
						disabled={
							selectedKey === optionsLabel ||
							selectedItemsIOMap.isEmpty()
						}
						display='primary'
						onClick={this.handleSave}
					>
						{Liferay.Language.get('save')}
					</Button>
				</Modal.Footer>
			</Modal>
		);
	}
}

export default withSelectionProvider(BatchActionModal);
