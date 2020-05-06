import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Chart, {BAR_CHART} from 'shared/components/Chart';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import FaroConstants from 'shared/util/constants';
import HistogramChart from 'shared/components/HistogramChart';
import Icon from 'shared/components/Icon';
import Promise from 'metal-promise';
import React from 'react';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {connect} from 'react-redux';
import {DistributionTab} from 'shared/util/records';
import {getChartSizeConfig} from 'contacts/components/Distribution';
import {hasChanges} from 'shared/util/react';
import {List, Map} from 'immutable';
import {noop, pickBy, truncate} from 'lodash';

const {
	fieldTypes: {number}
} = FaroConstants;

const BAR_PADDING = 10;
const BB_CHART_HEIGHT = 320;

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
	componentDidMount() {
		this.handleFetchChartData();
	}

	componentDidUpdate(prevProps) {
		if (hasChanges(prevProps, this.props, 'selectedTab')) {
			this.handleFetchChartData();
		}
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
				count: 10,
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
			selectedTab: {numberOfBins, propertyType},
			viewAllLink
		} = this.props;

		const individualFieldDistribution = individualFieldDistributionIList.toJS();

		const histogram = propertyType === number;

		const ChartComponent = histogram ? HistogramChart : Chart;

		return (
			<>
				<Card.Body alignCenter>
					{error && (
						<ErrorDisplay
							onReload={this.handleFetchChartData}
							spacer
						/>
					)}

					{!error && (
						<ChartComponent
							{...getChartSizeConfig(
								individualFieldDistribution.length,
								histogram
							)}
							axisRotated
							axisX={{
								tick: {
									format: (_, name) =>
										truncate(name, {length: 25}),
									multiline: false
								},
								type: 'category'
							}}
							axisY={{show: false}}
							axisY2={{
								show: true,
								text: {show: true}
							}}
							bar={pickBy({
								padding: histogram ? BAR_PADDING : null,
								radius: {ratio: 0.1},
								width: histogram
									? BB_CHART_HEIGHT / numberOfBins -
									  BAR_PADDING
									: null
							})}
							chartType={BAR_CHART}
							data={
								individualFieldDistribution.length
									? [
											{
												axis: 'y2',
												data: individualFieldDistribution.map(
													({count}) => count
												),
												id: 'count'
											},
											{
												data: individualFieldDistribution.map(
													({values}) =>
														values.length > 1
															? values[1]
															: values[0]
												),
												id: 'value'
											}
									  ]
									: []
							}
							dataId='DISTRIBUTION'
							generateChartOnLoad
							height={352}
							histogram={histogram}
							id='DISTRIBUTION_CARD'
							loading={loading}
							noResultsProps={{spacer: true}}
							padding={{bottom: 10}}
							tooltip={{show: false}}
							x='value'
						/>
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
