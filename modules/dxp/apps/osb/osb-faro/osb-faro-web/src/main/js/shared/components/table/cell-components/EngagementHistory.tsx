import getCN from 'classnames';
import React from 'react';
import TrendLine from 'shared/components/TrendLine';

type EngagementAggregation = {
	scoreAvg: number;
};

interface IEngagementHistoryCellProps {
	className?: string;
	data: {
		engagementHistory: {
			engagementAggregations: EngagementAggregation[];
		};
	};
}

const EngagementHistoryCell: React.FC<IEngagementHistoryCellProps> = ({
	className,
	data: {
		engagementHistory: {engagementAggregations}
	}
}) => (
	<td className={getCN('trendline', className)}>
		<TrendLine
			data={engagementAggregations.map(({scoreAvg}) => scoreAvg)}
			point={{show: false}}
			size={{height: 40}}
		/>
	</td>
);

export default EngagementHistoryCell;
