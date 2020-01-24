import PropTypes from 'prop-types';
import React from 'react';

export default function Arrows({onNext, onPrev}) {
	return (
		<>
			{onPrev && <div className="arrow prev" onClick={onPrev} />}
			{onNext && <div className="arrow next" onClick={onNext} />}
		</>
	);
}

Arrows.propTypes = {
	onNext: PropTypes.func,
	onPrev: PropTypes.func
};
