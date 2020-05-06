import getCN from 'classnames';
import getSVG from '../util/svg';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {PropTypes} from 'prop-types';
const SIZES = ['sm', 'md', 'lg', 'xl', 'xxl', 'xxxl'];

class Icon extends React.Component {
	static propTypes = {
		className: PropTypes.string,
		color: PropTypes.string,
		monospaced: PropTypes.bool,
		size: PropTypes.oneOf(SIZES),
		symbol: PropTypes.string.isRequired
	};

	render() {
		const {
			className,
			color,
			monospaced,
			size,
			symbol,
			...otherProps
		} = this.props;

		const classes = getCN(
			'icon-root',
			'lexicon-icon',
			`lexicon-icon-${symbol}`,
			className,
			{
				[`${color}-color`]: color,
				['icon-monospaced']: monospaced,
				[`icon-size-${size}`]: size
			}
		);

		const svg = getSVG(symbol);

		return (
			<svg
				{...omitDefinedProps(otherProps, Icon.propTypes)}
				className={classes}
				viewBox={svg.viewBox}
			>
				<use xlinkHref={`#${svg.id}`} />
			</svg>
		);
	}
}

Icon.SIZES = SIZES;
export default Icon;
