import FaroConstants from '../util/constants';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import Sticker, {getDisplayForId, getSymbol} from './Sticker';
import {get} from 'lodash';
import {PropTypes} from 'prop-types';
const {individual} = FaroConstants.entityTypes;

function getInitials(first, last) {
	let retVal = first ? first.substring(0, 1) : '';

	if (last) {
		retVal += last.substring(0, 1);
	}

	return retVal.toUpperCase();
}

class Avatar extends React.Component {
	static propTypes = {
		entity: PropTypes.object.isRequired
	};

	render() {
		const {
			className,
			entity: {id, properties, type},
			...otherProps
		} = this.props;

		const image = get(properties, 'image');

		const styles = !image
			? undefined
			: {
					backgroundImage: `url(${image})`
			  };

		return (
			<Sticker
				{...omitDefinedProps(otherProps, Avatar.propTypes)}
				className={`avatar-root${className ? ` ${className}` : ''}`}
				display={getDisplayForId(id)}
				style={styles}
				symbol={type !== individual ? getSymbol(type) : null}
			>
				{type === individual &&
					!image &&
					getInitials(properties.givenName, properties.familyName)}
			</Sticker>
		);
	}
}

export default Avatar;
