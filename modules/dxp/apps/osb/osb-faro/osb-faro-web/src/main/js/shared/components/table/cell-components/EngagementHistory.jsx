import getCN from 'classnames';
import React from 'react';
import TrendLine from 'shared/components/TrendLine';
import {PropTypes} from 'prop-types';

export default class EngagementHistoryCell extends React.Component {
	static propTypes = {
		data: PropTypes.shape({
			engagementHistory: PropTypes.shape({
				engagementAggregations: PropTypes.array
			})
		}).isRequired
	};

	render() {
		const {
			className,
			data: {
				engagementHistory: {engagementAggregations}
			}
		} = this.props;

		return (
			<td className={getCN('trendline', className)}>
				<TrendLine
					data={engagementAggregations.map(({scoreAvg}) => scoreAvg)}
					point={{show: false}}
					size={{height: 40}}
				/>
			</td>
		);
	}
}
