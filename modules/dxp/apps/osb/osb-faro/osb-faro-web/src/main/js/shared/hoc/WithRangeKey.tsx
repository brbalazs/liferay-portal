import React, {useCallback, useState} from 'react';
import {RangeKeyTimeRanges} from 'shared/util/constants';
import {RangeSelectors} from 'shared/types';

export interface WithRangeKeyProps {
	onRangeSelectorsChange?: (val) => void;
	rangeSelectors?: RangeSelectors;
}

const withRangeKey = <P extends WithRangeKeyProps>(
	WrappedComponent: React.ComponentType<P>
): React.FC<P> => {
	const defaultRangeSelectors = {
		rangeEnd: null,
		rangeKey: RangeKeyTimeRanges.Last30Days,
		rangeStart: null
	};

	return props => {
		const [rangeSelectors, setRangeSelectors] = useState(
			props?.rangeSelectors || defaultRangeSelectors
		);

		return (
			<WrappedComponent
				{...(props as P)}
				onRangeSelectorsChange={useCallback(
					newVal => setRangeSelectors(newVal),
					[]
				)}
				rangeSelectors={rangeSelectors}
			/>
		);
	};
};

export default withRangeKey;
