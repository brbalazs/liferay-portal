import Card from 'shared/components/Card';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import React from 'react';
import {compose} from 'redux';
import {withRangeKey} from 'shared/hoc';

interface ICardWithRangeKeyProps extends React.HTMLAttributes<HTMLElement> {
	children: (val) => React.ReactNode;
	label: string;
	onChangeRangeKey: () => void;
	rangeKey: string;
}

const CardWithRangeKey = compose(withRangeKey)(
	({
		children,
		className,
		label,
		onChangeRangeKey,
		rangeKey,
		...otherProps
	}: ICardWithRangeKeyProps) => (
		<Card className={className}>
			<Card.Header className='align-items-center d-flex justify-content-between'>
				<Card.Title>{label}</Card.Title>

				<DropdownRangeKey
					onChange={onChangeRangeKey}
					rangeKey={rangeKey}
				/>
			</Card.Header>

			{children({...otherProps, rangeKey})}
		</Card>
	)
);

export default CardWithRangeKey;
