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
import {RangeSelectors} from 'shared/util/util';

type Item = {
	description?: string;
	label: string;
	value: string;
};

interface DropdownRangeKeyIProps {
	className: string;
	items: Array<Item>;
	legacy: boolean;
	onChange: (rangeSelectors: RangeSelectors) => void;
	rangeSelectors: RangeSelectors;
}

const DropdownRangeKey: React.FC<DropdownRangeKeyIProps> = ({
	className,
	items,
	legacy = true, // legacy can be removed once we convert all uses of DropdownRangeKey to include the new values.
	onChange,
	rangeSelectors: {rangeEnd, rangeKey, rangeStart} = {
		rangeEnd: '',
		rangeKey: LAST_30_DAYS,
		rangeStart: ''
	}
}) => {
	// add a useEffect for when rangeDates change

	const [active, setActive] = useState(false);
	const [customDateRange, setCustomDateRange] = useState<MomentDateRange>({
		end: rangeEnd ? moment(rangeEnd, FORMAT) : null, // prob should set this to whatever is passed in
		start: rangeStart ? moment(rangeStart, FORMAT) : null
	});
	const [seeMore, setSeeMore] = useState(false);
	const [showDatePicker, setShowDatePicker] = useState(false);

	useEffect(() => {
		if (customDateRange && customDateRange.end && customDateRange.start) {
			const {end, start} = customDateRange;

			const dateRangeItem = {
				label: `${start.format('ll')} - ${end.format('ll')}`,
				value: 'CUSTOM'
			};

			onChange({
				rangeEnd: customDateRange.end.format(FORMAT),
				rangeKey: dateRangeItem.value,
				rangeStart: customDateRange.start.format(FORMAT)
			});

			setActive(false);
			setShowDatePicker(false);
		}
	}, [customDateRange]);

	const handleValueChange = (item: Item) => {
		setActive(false);

		onChange &&
			onChange({
				rangeEnd: '',
				rangeKey: item.value,
				rangeStart: ''
			});

		setCustomDateRange({end: null, start: null});
	};

	const getSelectedItem = () => {
		if (rangeKey === 'CUSTOM') {
			const {end, start} = customDateRange;

			return {
				label: `${start.format('ll')} - ${end.format('ll')}`,
				value: 'CUSTOM'
			};
		}

		return items.find(({value}) => value === rangeKey) || items[0];
	};

	const filteredItems =
		seeMore || legacy
			? items // filter these items to contain the original values only.
			: items.filter(
					({value}) =>
						value === rangeKey ||
						[
							LAST_24_HOURS,
							LAST_7_DAYS,
							LAST_30_DAYS,
							LAST_90_DAYS
						].includes(value as string)
			  );

	const selectedItem = getSelectedItem();

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
					onSelect={({end, start}: MomentDateRange) => {
						setCustomDateRange({
							end,
							start
						});
					}}
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
									active: selectedItem.value === 'CUSTOM'
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
