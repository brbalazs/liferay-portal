import React from 'react';
import {PropTypes} from 'prop-types';

export default class ChartTooltip extends React.Component {
	static defaultProps = {
		items: []
	};

	static propTypes = {
		items: PropTypes.arrayOf(
			PropTypes.shape({
				label: PropTypes.string,
				value: PropTypes.oneOfType([PropTypes.number, PropTypes.string])
			})
		),
		subtitle: PropTypes.oneOfType([PropTypes.array, PropTypes.string]),
		title: PropTypes.oneOfType([PropTypes.array, PropTypes.string])
	};

	render() {
		const {items, subtitle, title} = this.props;

		return (
			<div
				className={`chart-tooltip-root${
					this.props.className ? ` ${this.props.className}` : ''
				}`}
			>
				<div className='header'>
					<div className='title'>{title}</div>

					{subtitle}
				</div>

				<div className='divider' />

				<div className='contents'>
					{items.map(({label, value}, i) => (
						<div key={i}>
							{label}

							<b>{value}</b>
						</div>
					))}
				</div>
			</div>
		);
	}
}
