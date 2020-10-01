import _ from 'lodash';
import AppliedFilters from 'shared/components/filter/AppliedFilters';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import dom from 'metal-dom';
import DropdownMenu from 'cerebro-shared/components/DropdownMenu';
import React from 'react';
import {hasChanges} from 'shared/util/react';
import {PropTypes} from 'prop-types';

const CLASSNAME = 'analytics-filter';

/**
 * Filter
 * @class
 */
class Filter extends React.Component {
	static defaultProps = {
		items: []
	};

	static propTypes = {
		items: PropTypes.array,
		onChange: PropTypes.func.isRequired
	};

	state = {
		selectedItems: {},
		showDropdown: false
	};

	/**
	 * Lifecycle Constructor - ReactJS
	 */
	constructor(props) {
		super(props);

		this._elementRef = React.createRef();
	}

	/**
	 * Lifecycle Component Did Mount - ReactJS
	 */
	componentDidMount() {
		this._documentClickHandler = dom.on(
			document,
			'click',
			this.handleDocClick
		);
	}

	/**
	 * Lifecycle UNSAFE Component Will Receive Props - ReactJS
	 * @param {object} nextProps
	 */
	componentDidUpdate(prevProps) {
		if (
			hasChanges(prevProps, this.props, 'items') &&
			Object.keys(this.props).indexOf('items') > -1
		) {
			this.setState({
				items: this.getCheckedItems(this.props.items)
			});
		}
	}

	/**
	 * Lifecycle Component Will Unmount - ReactJS
	 */
	componentWillUnmount() {
		if (this._documentClickHandler) {
			this._documentClickHandler.removeListener();
		}
	}

	/**
	 * Get items with checked statuses
	 */
	getCheckedItems(parentItems) {
		const {selectedItems} = this.state;

		return parentItems.map(item => {
			const categoryItems = selectedItems[item.category];

			let items = null;

			if (item.items) {
				items = this.getCheckedItems(item.items);
			}

			if (categoryItems && categoryItems.indexOf(item.label) > -1) {
				return {
					...item,
					checked: true,
					items
				};
			}

			return {...item, items};
		});
	}

	/**
	 * Update Radio Items
	 * @param {object} param0
	 */
	updateRadioItems({category, label}) {
		const {selectedItems} = this.state;

		this.setState(
			{
				selectedItems: {...selectedItems, [category]: [label]}
			},
			this.handleClickApplyFilter
		);
	}

	/**
	 * Update Checkbox Items
	 * @param {object} param0
	 */
	updateCheckboxItems({category, checked, label}) {
		const {selectedItems} = this.state;

		const categoryItems = selectedItems[category] || [];

		if (checked) {
			categoryItems.push(label);
		} else {
			_.remove(categoryItems, n => n === label);
		}

		selectedItems[category] = categoryItems;

		this.setState(
			{
				selectedItems
			},
			this.handleClickApplyFilter
		);
	}

	/**
	 * Has Subitems
	 * @param {array} parentItems
	 */
	hasSubItems(parentItems) {
		return parentItems.some(
			({items, label}) =>
				(items && this.hasSubItems(items)) || (!items && label)
		);
	}

	/**
	 * Handle Change Dropdown Item
	 * @param {object} param0
	 */
	@autobind
	handleChangeDropdownItem({dropdownItem}) {
		if (dropdownItem.inputType == 'radio') {
			this.updateRadioItems(dropdownItem);
		} else if (dropdownItem.inputType == 'checkbox') {
			this.updateCheckboxItems(dropdownItem);
		}
	}

	/**
	 * Handle Click Toggle Dropdown
	 */
	@autobind
	handleClickToggleDropdown() {
		this.setState({
			showDropdown: !this.state.showDropdown
		});
	}

	/**
	 * Handle Click Apply Filter
	 */
	@autobind
	handleClickApplyFilter() {
		const {onChange} = this.props;

		onChange(this.state.selectedItems);
	}

	/**
	 * Handle Document Click
	 * @param {object} event
	 */
	@autobind
	handleDocClick({target}) {
		const dropdown = this._elementRef.current.querySelector(
			'.analytics-dropdown'
		);
		const dropdownMenu = Object.assign(
			[],
			document.querySelectorAll('.analytics-dropdown-menu')
		);

		if (
			dropdown.contains(target) ||
			dropdownMenu.find(menu => menu.contains(target))
		)
			return;

		this.setState({
			showDropdown: false
		});
	}

	/**
	 * Handle Update Filters
	 * @param {object} param0
	 */
	@autobind
	handleUpdateFilters(appliedFilters) {
		const {onChange} = this.props;

		onChange(appliedFilters);

		this.setState({
			selectedItems: appliedFilters
		});
	}

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {items} = this.props;

		const {selectedItems, showDropdown} = this.state;

		return (
			<div className={CLASSNAME} ref={this._elementRef}>
				<div className='analytics-dropdown dropdown btn-group border-0'>
					<Button
						aria-label='Dropdown Filter'
						className='dropdown-toggle btn-outline-borderless'
						display='secondary'
						icon='caret-bottom'
						iconAlignment='right'
						onClick={this.handleClickToggleDropdown}
						size='sm'
					>
						{Liferay.Language.get('filter')}
					</Button>
				</div>

				<DropdownMenu
					items={this.getCheckedItems(items)}
					onSelectItemsChange={this.handleChangeDropdownItem}
					show={showDropdown}
				/>

				<AppliedFilters
					filters={selectedItems}
					onChange={this.handleUpdateFilters}
				/>
			</div>
		);
	}
}

export {Filter};
export default Filter;
