import ActiveIndividualsChart from '../components/ActiveIndividualsChart';
import Card from 'shared/components/Card';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import IntervalSelector from 'shared/components/IntervalSelector';
import React, {useCallback} from 'react';
import SiteMetricsQuery from '../queries/SiteMetricsQuery';
import {compose} from 'redux';
import {graphql} from '@apollo/react-hoc';
import {Interval} from 'shared/types';
import {INTERVAL_KEY_MAP, isHourlyRangeKey} from 'shared/util/time';
import {
	mapPropsToOptions,
	mapResultToProps
} from '../hocs/mappers/site-metrics-query';
import {RangeSelectors} from 'shared/types';
import {withError} from 'shared/hoc';
import {withInterval, withRangeKey} from 'shared/hoc';

const ChartWithData = compose<any>(
	graphql(SiteMetricsQuery, {
		options: mapPropsToOptions,
		props: mapResultToProps
	}),
	withError({page: false})
)(ActiveIndividualsChart);

interface IActiveIndividualsCardProps {
	channelId: string;
	interval: Interval;
	loading: Boolean;
	onChangeInterval: (val: any) => void;
	onRangeSelectorsChange: (val: string) => void;
	rangeSelectors: RangeSelectors;
}

const ActiveIndividualsCard = compose<any>(
	withInterval,
	withRangeKey
)(
	({
		channelId,
		interval,
		loading,
		onChangeInterval,
		onRangeSelectorsChange,
		rangeSelectors
	}: IActiveIndividualsCardProps) => (
		<Card minHeight={536}>
			<Card.Header className='align-items-center d-flex justify-content-between'>
				<Card.Title>
					{Liferay.Language.get('active-individuals')}
				</Card.Title>

				<div className='d-flex'>
					{interval && (
						<IntervalSelector
							activeInterval={interval}
							className='mr-3'
							disabled={isHourlyRangeKey(rangeSelectors.rangeKey)}
							onChange={onChangeInterval}
						/>
					)}

					<DropdownRangeKey
						legacy={false}
						onChange={useCallback(newVal => {
							onRangeSelectorsChange &&
								onRangeSelectorsChange(newVal);

							if (isHourlyRangeKey(newVal.rangeKey)) {
								onChangeInterval(INTERVAL_KEY_MAP.day);
							}
						}, [])}
						rangeSelectors={rangeSelectors}
					/>
				</div>
			</Card.Header>

			<Card.Body className='justify-content-center'>
				<ChartWithData
					active
					channelId={channelId}
					interval={interval}
					loading={loading}
					rangeSelectors={rangeSelectors}
				/>
			</Card.Body>
		</Card>
	)
);

export default ActiveIndividualsCard;
