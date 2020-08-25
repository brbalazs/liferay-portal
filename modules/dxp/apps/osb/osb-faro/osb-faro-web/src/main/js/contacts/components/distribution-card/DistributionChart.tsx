import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import FaroConstants from 'shared/util/constants';
import Icon from 'shared/components/Icon';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import Promise from 'metal-promise';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {AXIS, BAR_COLORS, getTextWidth} from 'shared/util/clay-recharts';
import {
	Bar,
	CartesianGrid,
	Cell,
	ComposedChart,
	ResponsiveContainer,
	XAxis,
	YAxis
} from 'recharts';
import {connect} from 'react-redux';
import {DistributionTab} from 'shared/util/records';
import {hasChanges} from 'shared/util/react';
import {List, Map} from 'immutable';
import {noop, pickBy} from 'lodash';

const {
	fieldTypes: {number}
} = FaroConstants;

const BAR_WIDTH = 30;
const CHART_DATA_ID = 'count';
const CHART_PADDING = 60;
const MAX_BARS = 10;

interface IDistributionChartProps {
	channelId: string;
	distributionKey: string;
	error: boolean;
	fetchDistribution: (object) => Promise<any>;
	groupId: string;
	id: string;
	individualFieldDistributionIList: List<Map<string, any>>;
	loading: boolean;
	selectedTab: DistributionTab;
	viewAllLink: string;
}

@hasRequest
class DistributionChart extends React.Component<IDistributionChartProps> {
	state = {
		hoverIndex: -1
	};

	componentDidMount() {
		this.handleFetchChartData();
	}

	componentDidUpdate(prevProps) {
		if (hasChanges(prevProps, this.props, 'selectedTab')) {
			this.handleFetchChartData();
		}
	}

	formatChartData(fieldDistributions, histogram) {
		return fieldDistributions.map(({count, values}) => ({
			count,
			graphValue: histogram ? (values[0] + values[1]) / 2 : values[0],
			values
		}));
	}

	getBarColor(index) {
		const {hoverIndex} = this.state;

		if (index === hoverIndex) {
			return BAR_COLORS.hover;
		}

		return BAR_COLORS.default;
	}

	getYAxisTicks(fieldDistributions, histogram) {
		return [
			...fieldDistributions.map(item => item.values[0]),
			histogram &&
				fieldDistributions.length &&
				fieldDistributions[fieldDistributions.length - 1].values[1]
		].filter(Boolean);
	}

	@autoCancel
	@autobind
	handleFetchChartData() {
		const {
			channelId,
			fetchDistribution,
			groupId,
			id,
			selectedTab: {context, numberOfBins, propertyId}
		} = this.props;

		return fetchDistribution(
			pickBy({
				channelId,
				context,
				count: MAX_BARS,
				fieldMappingId: propertyId,
				groupId,
				id,
				individualSegmentId: id,
				numberOfBins
			})
		).catch(noop);
	}

	render() {
		const {
			error,
			individualFieldDistributionIList,
			loading,
			selectedTab: {propertyType},
			viewAllLink
		} = this.props;

		const individualFieldDistribution = individualFieldDistributionIList.toJS();

		const histogram = propertyType === number;

		const yAxisTicks = this.getYAxisTicks(
			individualFieldDistribution,
			histogram
		);

		const formattedChartData = this.formatChartData(
			individualFieldDistribution,
			histogram
		);

		const fieldDistributionsCount = individualFieldDistribution.length;

		const yAxisDomain = histogram
			? [yAxisTicks[0], yAxisTicks[yAxisTicks.length - 1]]
			: [0, 'auto'];

		const yAxisWidth = yAxisTicks.reduce((acc, item) => {
			const textWidth = getTextWidth(item.toString());

			return textWidth > acc ? textWidth : acc;
		}, 60);

		return (
			<>
				<Card.Body alignCenter>
					{error && (
						<ErrorDisplay
							onReload={this.handleFetchChartData}
							spacer
						/>
					)}

					{loading && <Spinner spacer />}

					{!error && !loading && (
						<>
							{!fieldDistributionsCount && (
								<NoResultsDisplay icon={{symbol: 'document'}} />
							)}

							{!!fieldDistributionsCount && (
								<ResponsiveContainer
									height={
										BAR_WIDTH * MAX_BARS + CHART_PADDING
									}
								>
									<ComposedChart
										data={formattedChartData}
										layout='vertical'
									>
										<CartesianGrid
											horizontal={false}
											stroke={AXIS.gridStroke}
											strokeDasharray='3 3'
										/>

										<YAxis
											axisLine={{
												stroke: AXIS.borderStroke
											}}
											dataKey='graphValue'
											domain={yAxisDomain}
											tickFormatter={val => val}
											ticks={yAxisTicks}
											type={
												histogram
													? 'number'
													: 'category'
											}
											width={yAxisWidth}
										/>

										<YAxis
											axisLine={{
												stroke: AXIS.borderStroke
											}}
											dataKey='graphValue'
											domain={yAxisDomain}
											orientation='right'
											tick={false}
											tickLine={false}
											yAxisId='right'
										/>

										<XAxis
											axisLine={{
												stroke: AXIS.borderStroke
											}}
											dataKey={CHART_DATA_ID}
											domain={[
												0,
												dataMax => dataMax * 1.1
											]}
											orientation='top'
											scale='linear'
											tickLine={false}
											type='number'
										/>

										<XAxis
											axisLine={{
												stroke: AXIS.borderStroke
											}}
											dataKey={CHART_DATA_ID}
											domain={[
												0,
												dataMax => dataMax * 1.1
											]}
											tick={false}
											tickLine={false}
											xAxisId='bottom'
										/>

										<Bar
											dataKey={CHART_DATA_ID}
											onMouseEnter={(e, index) =>
												this.setState({
													hoverIndex: index
												})
											}
											onMouseLeave={() =>
												this.setState({
													hoverIndex: -1
												})
											}
											radius={[0, 10, 10, 0]}
										>
											{formattedChartData.map(
												(item, index) => (
													<Cell
														fill={this.getBarColor(
															index
														)}
														key={`cell-${index}`}
													/>
												)
											)}
										</Bar>
									</ComposedChart>
								</ResponsiveContainer>
							)}
						</>
					)}
				</Card.Body>

				<Card.Footer>
					<Button display='link' href={viewAllLink} size='sm'>
						{Liferay.Language.get('explore-breakdown')}

						<Icon symbol='angle-right' />
					</Button>
				</Card.Footer>
			</>
		);
	}
}

export default connect((state, {distributionKey}) => {
	const distributionIMap = state.getIn(
		['distributions', distributionKey],
		Map()
	);

	return {
		error: distributionIMap.get('error', false),
		individualFieldDistributionIList: distributionIMap
			.getIn(['data', 'items'], List())
			.slice(0, 11),
		loading: distributionIMap.get('loading', true)
	};
})(DistributionChart);
