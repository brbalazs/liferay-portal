import ClayButton from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import DatePicker from './date-picker';
import getCN from 'classnames';
import moment from 'moment';
import React, {useEffect, useState} from 'react';
import {DateRange, MomentDateRange} from 'shared/components/DateRangeInput';
import {FORMAT} from 'shared/util/date';
import {isRange} from 'shared/components/date-picker/util';
import {
	LAST_24_HOURS,
	LAST_30_DAYS,
	LAST_7_DAYS,
	LAST_90_DAYS
} from 'shared/util/constants';

type Item = {
	description?: string;
	label: string;
	value: string | DateRange;
};

interface DropdownRangeKeyIProps extends React.HTMLAttributes<HTMLElement> {
	items: Array<Item>;
	legacy: boolean;
	onChange: (val: any) => void;
	rangeKey: string;
}

const isDateRange = (dateRange: DateRange | string): dateRange is DateRange =>
	Boolean((dateRange as DateRange).end && (dateRange as DateRange).start);

const getSelectedItem = (items: Array<Item>, currentValue: string) =>
	items.filter(({value}) => value === currentValue)[0];

const DropdownRangeKey: React.FC<DropdownRangeKeyIProps> = ({
	className,
	items,
	legacy = true, // legacy can be removed once we convert all uses of DropdownRangeKey to include the new values.
	onChange,
	rangeKey = LAST_30_DAYS
}) => {
	// add method to check if rangeKey is a range
	const [active, setActive] = useState(false);
	const [customDateRange, setCustomDateRange] = useState<MomentDateRange>({
		end: null,
		start: null
	});
	const [selectedItem, setSelectedItem] = useState(
		getSelectedItem(items, rangeKey)
	);
	const [seeMore, setSeeMore] = useState(false);
	const [showDatePicker, setShowDatePicker] = useState(false);

	useEffect(() => {
		if (customDateRange && customDateRange.end && customDateRange.start) {
			const {end, start} = customDateRange;

			const dateRangeItem = {
				label: `${start.format('ll')} - ${end.format('ll')}`,
				value: {
					end: end.format(FORMAT),
					start: start.format(FORMAT)
				}
			};

			setSelectedItem(dateRangeItem);

			onChange(dateRangeItem.value);

			setActive(false);
			setShowDatePicker(false);
		}
	}, [customDateRange]);

	const handleDateRangeSelect = ({end, start}: MomentDateRange) => {
		setCustomDateRange({
			end,
			start
		});
	};

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
						].includes(value as string)
			  );

	return (
		<ClayDropDown
			active={active}
			alignmentPosition={3}
			className={getCN(className, 'dropdown-range-key-root')}
			menuElementAttrs={{
				className: getCN('dropdown-range-key-menu-root', {
					'show-date-picker': showDatePicker
				})
			}}
			onActiveChange={active => {
				setActive(active);

				setShowDatePicker(false);
			}}
			trigger={
				<ClayButton borderless displayType='secondary' small>
					{selectedItem.label}

					<ClayIcon className='ml-2' symbol='caret-bottom' />
				</ClayButton>
			}
		>
			{showDatePicker ? (
				<DatePicker
					date={customDateRange}
					maxDate={moment().endOf('day')}
					maxRange={365}
					minDate={moment().subtract(100, 'years')}
					onSelect={handleDateRangeSelect}
				/>
			) : (
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

								<div className='font-size-sm-2x'>
									{description}
								</div>
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
									{Liferay.Language.get(
										'more-preset-periods'
									)}
								</ClayDropDown.Item>
							)}

							<ClayDropDown.Divider />

							<ClayDropDown.Item
								className={getCN('c-pointer', {
									active: isDateRange(selectedItem.value)
								})}
								key='CUSTOM'
								onClick={() => setShowDatePicker(true)}
							>
								<b>{Liferay.Language.get('custom-range')}</b>
							</ClayDropDown.Item>
						</>
					)}
				</ClayDropDown.ItemList>
			)}
		</ClayDropDown>
	);
};

export default DropdownRangeKey;
