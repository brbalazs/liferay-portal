import BarChartHTML from 'cerebro-shared/components/BarChartHTML';
import Legend from 'cerebro-shared/components/Legend';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import PerDayChart from 'experiments/hocs/PerDayChart';
import React from 'react';
import VariantCard, {
	VariantCardIProps
} from 'experiments/components/variant-card';
import {useStateValue} from 'experiments/state';

const withChart: React.FC<VariantCardIProps> = ({
	legend,
	mediansData,
	metricUnit,
	...props
}) => {
	const [{variantChartTriggered}]: any = useStateValue();
	const {items} = mediansData;

	return (
		<VariantCard metricUnit={metricUnit} {...props}>
			{variantChartTriggered === 'medians' ? (
				items && !items.length ? (
					<NoResultsDisplay
						icon={{border: false, size: 'xl', symbol: 'ac-chart'}}
					>
						<div>
							{Liferay.Language.get(
								'we-are-currently-collecting-data'
							)}
						</div>
						<div>
							{Liferay.Language.get(
								'metrics-will-show-once-there-are-visitors-to-your-variants'
							)}
						</div>
					</NoResultsDisplay>
				) : (
					<>
						<BarChartHTML {...mediansData} />
						<Legend data={legend} />
					</>
				)
			) : (
				variantChartTriggered === 'per-day' && (
					<PerDayChart metricUnit={metricUnit} />
				)
			)}
		</VariantCard>
	);
};

export default withChart;
