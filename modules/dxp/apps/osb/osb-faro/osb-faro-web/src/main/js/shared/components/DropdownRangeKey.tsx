import ClayButton from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import getCN from 'classnames';
import React, {useState} from 'react';
import {LAST_30_DAYS} from 'shared/util/constants';

type Item = {
	description: string;
	label: string;
	value: string;
};

interface DropdownRangeKeyIProps extends React.HTMLAttributes<HTMLElement> {
	items: Array<Item>;
	onChange: (val: any) => void;
	rangeKey: string;
}

const getSelectedItem = (items: Array<Item>, currentValue: string) =>
	items.filter(({value}) => value === currentValue)[0];

const DropdownRangeKey: React.FC<DropdownRangeKeyIProps> = ({
	className,
	items,
	onChange,
	rangeKey = LAST_30_DAYS
}) => {
	const [active, setActive] = useState(false);
	const [selectedItem, setSelectedItem] = useState(
		getSelectedItem(items, rangeKey)
	);

	const handleValueChange = (item: Item) => {
		setActive(false);
		setSelectedItem(item);

		onChange && onChange(item.value);
	};

	return (
		<ClayDropDown
			active={active}
			alignmentPosition={3}
			className={getCN(className, 'dropdown-range-key-root')}
			onActiveChange={setActive}
			trigger={
				<ClayButton borderless displayType='secondary' small>
					{selectedItem.label}

					<ClayIcon className='ml-2' symbol='caret-bottom' />
				</ClayButton>
			}
		>
			<ClayDropDown.ItemList>
				{items.map((item: Item, index: number) => {
					const {description, label, value} = item;
					const activeClass =
						selectedItem.value === value ? 'active' : '';

					return (
						<ClayDropDown.Item
							className={`c-pointer ${activeClass}`}
							key={index}
							onClick={() => handleValueChange(item)}
						>
							{label}

							<div className='font-size-sm-2x'>{description}</div>
						</ClayDropDown.Item>
					);
				})}
			</ClayDropDown.ItemList>
		</ClayDropDown>
	);
};

export default DropdownRangeKey;
