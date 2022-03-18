import MetricBar from 'shared/components/MetricBar';
import PropTypes from 'prop-types';
import React from 'react';
import TextTruncate from 'shared/components/TextTruncate';
import {ceil, round} from 'lodash';

export default class RelativeMetricBar extends React.Component {
	static defaultProps = {
		showName: false
	};

	static propTypes = {
		data: PropTypes.shape({
			count: PropTypes.number,
			name: PropTypes.string
		}).isRequired,
		maxCount: PropTypes.number,
		showName: PropTypes.bool,
		total: PropTypes.number,
		totalCount: PropTypes.number
	};

	render() {
		const {
			data: {count, name},
			maxCount,
			showName,
			totalCount
		} = this.props;

		const denominator = ceil(maxCount / totalCount, 1) * totalCount;

		const percent = round(count / denominator, 2);

		const displayName = showName ? name : '';

		return (
			<td className='table-cell-expand relative-metric-bar-root'>
				<MetricBar percent={percent} size='lg'>
					<TextTruncate className='title' title={displayName} />

					<span className='count'>{count}</span>
				</MetricBar>
			</td>
		);
	}
}
