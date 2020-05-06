import React, {useCallback, useState} from 'react';
import {LAST_30_DAYS} from 'shared/util/constants';

interface IWrappedComponentProps {
	onChangeRangeKey: (val) => void;
	rangeKey: string;
}

const withRangeKey = (
	WrappedComponent: React.ComponentType<IWrappedComponentProps>
) => {
	WrappedComponent.defaultProps = {
		rangeKey: LAST_30_DAYS
	};

	return props => {
		const [rangeKey, setRangeKey] = useState(props.rangeKey);
		const handleChangeRangeKey = useCallback(
			newVal => setRangeKey(newVal),
			[]
		);

		return (
			<WrappedComponent
				{...props}
				onChangeRangeKey={handleChangeRangeKey}
				rangeKey={rangeKey}
			/>
		);
	};
};

export default withRangeKey;
