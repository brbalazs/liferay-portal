import ClayButton from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import getCN from 'classnames';
import React, {useState} from 'react';
import {
	LAST_24_HOURS,
	LAST_30_DAYS,
	LAST_7_DAYS,
	LAST_90_DAYS
} from 'shared/util/constants';

type Item = {
	description: string;
	label: string;
	value: string;
};

interface DropdownRangeKeyIProps extends React.HTMLAttributes<HTMLElement> {
	items: Array<Item>;
	legacy: boolean;
	onChange: (val: any) => void;
	rangeKey: string;
}

const getSelectedItem = (items: Array<Item>, currentValue: string) =>
	items.filter(({value}) => value === currentValue)[0];

const DropdownRangeKey: React.FC<DropdownRangeKeyIProps> = ({
	className,
	items,
	legacy = true, // legacy can be removed once we convert all uses of DropdownRangeKey to include the new values.
	onChange,
	rangeKey = LAST_30_DAYS
}) => {
	const [active, setActive] = useState(false);
	const [selectedItem, setSelectedItem] = useState(
		getSelectedItem(items, rangeKey)
	);
	const [seeMore, setSeeMore] = useState(false);

	const handleValueChange = (item: Item) => {
		setActive(false);
		setSelectedItem(item);

		onChange && onChange(item.value);
	};

	const filteredItems =
		seeMore || legacy
			? items
			: items.filter(
					({value}) =>
						value === selectedItem.value ||
						[
							LAST_24_HOURS,
							LAST_7_DAYS,
							LAST_30_DAYS,
							LAST_90_DAYS
						].includes(value)
			  );

	// TODO: LRAC-5926 Add logic for displaying CustomRange date picker
	const handleCustomRangeClick = () => {};

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
				{filteredItems.map((item: Item, index: number) => {
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

				{!legacy && (
					<>
						{!seeMore && (
							<ClayDropDown.Item
								className='c-pointer'
								key='SEE_MORE'
								onClick={() => setSeeMore(true)}
							>
								{Liferay.Language.get('more-preset-periods')}
							</ClayDropDown.Item>
						)}

						<ClayDropDown.Divider />

						<ClayDropDown.Item
							className={`c-pointer ${
								selectedItem.value === 'CUSTOM' ? 'active' : ''
							}`}
							key='CUSTOM'
							onClick={handleCustomRangeClick}
						>
							<b>{Liferay.Language.get('custom-range')}</b>
						</ClayDropDown.Item>
					</>
				)}
			</ClayDropDown.ItemList>
		</ClayDropDown>
	);
};

export default DropdownRangeKey;
