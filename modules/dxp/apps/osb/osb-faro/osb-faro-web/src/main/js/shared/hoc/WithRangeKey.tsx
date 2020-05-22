import React, {useCallback, useState} from 'react';
import {LAST_30_DAYS} from 'shared/util/constants';
import {RangeSelectors} from 'shared/types';

interface IWrappedComponentProps {
	onRangeSelectorsChange: (val) => void;
	rangeSelectors: RangeSelectors;
}

const withRangeKey = (
	WrappedComponent: React.ComponentType<IWrappedComponentProps>
) => {
	WrappedComponent.defaultProps = {
		rangeSelectors: {
			rangeEnd: null,
			rangeKey: LAST_30_DAYS,
			rangeStart: null
		}
	};

	return props => {
		const [rangeSelectors, setRangeSelectors] = useState(
			props.rangeSelectors
		);

		return (
			<WrappedComponent
				{...props}
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
