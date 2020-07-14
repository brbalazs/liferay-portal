import Card from 'shared/components/Card';

import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import IntervalSelector from 'shared/components/IntervalSelector';
import React, {useCallback} from 'react';
import {ENGAGEMENT} from 'shared/util/router';
import {Interval} from 'shared/types';
import {INTERVAL_KEY_MAP} from 'shared/util/time';
import {isHourlyRangeKey} from 'shared/util/time';

interface BaseCardHeaderIProps extends React.HTMLAttributes<HTMLElement> {
	interval: Interval;
	label: string;
	legacy: boolean;
	onChangeInterval: (val: Interval) => void;
	onRangeSelectorsChange: (val: any) => void;
	rangeSelectors: RangeSelectors;
	showInterval: boolean;
	tabId: string;
}
import {RangeSelectors} from 'shared/types';

const BaseCardHeader: React.FC<BaseCardHeaderIProps> = ({
	interval,
	label,
	legacy,
	onChangeInterval,
	onRangeSelectorsChange,
	rangeSelectors,
	tabId
}) => {
	const handleRangeSelectorsChange = useCallback(newVal => {
		onRangeSelectorsChange && onRangeSelectorsChange(newVal);

		if (isHourlyRangeKey(newVal.rangeKey)) {
			onChangeInterval(INTERVAL_KEY_MAP.day);
		}
	}, []);

	const handleChangeInterval = useCallback(
		newVal => onChangeInterval && onChangeInterval(newVal),
		[]
	);

	return (
		<Card.Header className='align-items-center d-flex justify-content-between'>
			<Card.Title>{label}</Card.Title>

			<div className='d-flex'>
				<IntervalSelector
					activeInterval={interval}
					className='mr-3'
					disabled={
						isHourlyRangeKey(rangeSelectors.rangeKey) ||
						tabId === ENGAGEMENT
					}
					onChange={handleChangeInterval}
				/>

				<DropdownRangeKey
					disabled={tabId === ENGAGEMENT}
					legacy={legacy}
					onChange={handleRangeSelectorsChange}
					rangeSelectors={rangeSelectors}
				/>
			</div>
		</Card.Header>
	);
};

export default BaseCardHeader;
