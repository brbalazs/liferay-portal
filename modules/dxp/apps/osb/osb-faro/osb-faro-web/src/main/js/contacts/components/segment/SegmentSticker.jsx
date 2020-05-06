import FaroConstants from 'shared/util/constants';
import React from 'react';
import Sticker from 'shared/components/Sticker';

const {segmentStates, segmentTypes} = FaroConstants;

export default ({segmentType, state}) => {
	const disabled = state === segmentStates.disabled;

	const getSymbol = () => {
		if (disabled) {
			return 'warning';
		} else if (segmentType === segmentTypes.static) {
			return 'individual-static-segment';
		} else {
			return 'individual-dynamic-segment';
		}
	};

	return (
		<Sticker
			className='segment-sticker-root'
			display={disabled ? 'warning' : 'light'}
			symbol={getSymbol()}
		/>
	);
};
