import Card from 'shared/components/Card';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import IntervalSelector from 'shared/components/IntervalSelector';
import React, {useCallback} from 'react';
import {Interval} from 'shared/types';
import {INTERVAL_KEY_MAP} from 'shared/util/time';
import {isHourlyRangeKey} from 'shared/util/time';

interface BaseCardHeaderDefaultIProps
	extends React.HTMLAttributes<HTMLElement> {
	interval: Interval;
	label: string;
	legacy: boolean;
	onChangeInterval: (val: Interval) => void;
	onChangeRangeKey: (val: any) => void;
	rangeKey: string;
	showInterval: boolean;
}

const BaseCardHeaderDefault: React.FC<BaseCardHeaderDefaultIProps> = ({
	interval,
	label,
	legacy,
	onChangeInterval,
	onChangeRangeKey,
	rangeKey,
	showInterval
}) => {
	const handleChangeRangeKey = useCallback(newVal => {
		onChangeRangeKey && onChangeRangeKey(newVal);

		if (isHourlyRangeKey(newVal)) {
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
				{showInterval && (
					<IntervalSelector
						activeInterval={interval}
						className='mr-3'
						disabled={isHourlyRangeKey(rangeKey)}
						onChange={handleChangeInterval}
					/>
				)}

				<DropdownRangeKey
					legacy={legacy}
					onChange={handleChangeRangeKey}
					rangeKey={rangeKey}
				/>
			</div>
		</Card.Header>
	);
};

export default BaseCardHeaderDefault;
