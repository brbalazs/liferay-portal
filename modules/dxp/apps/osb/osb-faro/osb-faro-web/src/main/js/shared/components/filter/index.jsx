import _ from 'lodash';
import AppliedFilters from 'shared/components/filter/AppliedFilters';
import Button from 'shared/components/Button';
import dom from 'metal-dom';
import DropdownMenu from 'cerebro-shared/components/DropdownMenu';
import PropTypes from 'prop-types';
import React, {useEffect, useRef, useState} from 'react';

const CLASSNAME = 'analytics-filter';

const Filter = ({items: initialItems = [], onChange}) => {
	const [selectedItems, setSelectedItems] = useState([]);
	const [showDropdown, setShowDropdown] = useState(false);

	const [items, setItems] = useState(initialItems);

	const elementRef = useRef(null);

	useEffect(() => {
		const documentClickHandler = dom.on(document, 'click', handleDocClick);

		return () => {
			if (documentClickHandler) {
				documentClickHandler.removeListener();
			}
		};
	}, []);

	useEffect(() => {
		setItems(getCheckedItems(initialItems));
	}, [initialItems]);

	const getCheckedItems = parentItems =>
		parentItems.map(item => {
			const categoryItems = selectedItems[item.category];

			let items = null;

			if (item.items) {
				items = getCheckedItems(item.items);
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

	const updateRadioItems = ({category, label}) => {
		handleUpdateFilters({...selectedItems, [category]: [label]});
	};

	const updateCheckboxItems = ({category, checked, label}) => {
		const categoryItems = selectedItems[category] || [];

		if (checked) {
			categoryItems.push(label);
		} else {
			_.remove(categoryItems, n => n === label);
		}

		selectedItems[category] = categoryItems;

		handleUpdateFilters({...selectedItems});
	};

	const handleChangeDropdownItem = ({dropdownItem}) => {
		if (dropdownItem.inputType == 'radio') {
			updateRadioItems(dropdownItem);
		} else if (dropdownItem.inputType == 'checkbox') {
			updateCheckboxItems(dropdownItem);
		}
	};

	const handleClickToggleDropdown = () => {
		setShowDropdown(!showDropdown);
	};

	const handleDocClick = ({target}) => {
		const dropdown = elementRef.current.querySelector(
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

		setShowDropdown(false);
	};

	const handleUpdateFilters = selectedItems => {
		onChange(selectedItems);

		setSelectedItems(selectedItems);
	};

	return (
		<div className={CLASSNAME} ref={elementRef}>
			<div className='analytics-dropdown dropdown btn-group border-0'>
				<Button
					aria-label='Dropdown Filter'
					className='dropdown-toggle btn-outline-borderless'
					display='secondary'
					icon='caret-bottom'
					iconAlignment='right'
					onClick={handleClickToggleDropdown}
					size='sm'
				>
					{Liferay.Language.get('filter')}
				</Button>
			</div>

			<DropdownMenu
				items={getCheckedItems(items)}
				onSelectItemsChange={handleChangeDropdownItem}
				show={showDropdown}
			/>

			<AppliedFilters
				filters={selectedItems}
				onChange={handleUpdateFilters}
			/>
		</div>
	);
};

Filter.defaultProps = {
	items: []
};

Filter.propTypes = {
	items: PropTypes.array,
	onChange: PropTypes.func.isRequired
};

export {Filter};
export default Filter;
