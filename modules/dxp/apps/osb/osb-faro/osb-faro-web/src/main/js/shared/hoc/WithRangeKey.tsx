import React, {useCallback, useState} from 'react';
import {LAST_30_DAYS} from 'shared/util/constants';
import {RangeSelectors} from 'shared/types';

export interface WithRangeKeyProps {
	onRangeSelectorsChange: (val) => void;
	rangeSelectors: RangeSelectors;
}

const withRangeKey = <P extends WithRangeKeyProps>(
	WrappedComponent: React.ComponentType<P>
): React.FC<Omit<P, keyof WithRangeKeyProps>> => {
	const defaultRangeSelectors = {
		rangeEnd: null,
		rangeKey: LAST_30_DAYS,
		rangeStart: null
	};

	return props => {
		const [rangeSelectors, setRangeSelectors] = useState(
			defaultRangeSelectors
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
