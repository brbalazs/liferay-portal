import ClaySticker from '@clayui/sticker';
import PropType from 'prop-types';
import React from 'react';

function Picture(props) {
	return (
		<div className="row">
			<div className="col-auto">
				<ClaySticker shape={props.value.shape || 'rounded'} size={props.value.size || "xl"}>
					<div className="sticker-overlay">
						<img
							alt={props.value.alt}
							className="sticker-img"
							src={props.value.url}
						/>
					</div>
				</ClaySticker>
			</div>
		</div>
	);
}

Picture.propTypes = {
	value: PropType.shape({
		alt: PropType.string.isRequired,
		shape: PropType.oneOf([
			'circle', 'rounded'
		]),
		size: PropType.oneOf([
			'lg','sm','xl'
		]),
		url: PropType.string.isRequired
	})
}

export default Picture;
