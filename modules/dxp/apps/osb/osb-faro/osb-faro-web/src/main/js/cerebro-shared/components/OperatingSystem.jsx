import autobind from 'autobind-decorator';
import PropTypes from 'prop-types';
import React from 'react';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {AXIS, getAxisTickText} from 'shared/util/recharts';
import {
	Bar,
	CartesianGrid,
	ComposedChart,
	Label,
	ResponsiveContainer,
	Tooltip,
	XAxis,
	YAxis
} from 'recharts';
import {Colors} from 'shared/util/charts';
import {range} from 'lodash';
import {sub} from 'shared/util/lang';
import {toRounded, toThousands} from 'shared/util/numbers';

const CLASSNAME = 'analytics-operating-system-chart';
const EMPTY_DATA = [{data: [{views: 0}], label: ''}];
const MIN_VALUE = '< 0.1%';

/**
 * Operating System
 * @class
 */
class OperatingSystem extends React.Component {
	static defaultProps = {
		devices: [],
		height: 370,
		metricLabel: Liferay.Language.get('views')
	};

	static propTypes = {
		devices: PropTypes.array,
		empty: PropTypes.bool,
		height: PropTypes.number,
		metricLabel: PropTypes.string
	};

	getItemPercentage(percentage) {
		if (percentage < 0.1) {
			return `${MIN_VALUE}`;
		}

		return `${toRounded(percentage)}%`;
	}

	@autobind
	renderTooltip({active, payload}) {
		const {empty, metricLabel} = this.props;

		if (active) {
			let header = null;

			let rows = [
				{
					columns: [
						{
							className: 'pt-0',
							label: sub(
								Liferay.Language.get('empty-message-metric'),
								[metricLabel.toLowerCase()]
							)
						}
					]
				}
			];

			if (!empty) {
				const {
					payload: {label, percentageOfTotal, totalViews}
				} = payload[0];

				header = [
					{
						label
					},
					{
						align: 'right',
						label: `${toThousands(totalViews)} ${metricLabel}`
					},
					{
						align: 'right',
						label: `${toRounded(toRounded(percentageOfTotal))}%`
					}
				];

				rows = payload.map(({color, payload: {data}}, i) => {
					const {percentage, type, views} = data[i];

					return {
						columns: [
							{
								color,
								label: type,
								width: 100
							},
							{
								align: 'right',
								label: toThousands(views),
								width: 80
							},
							type !== 'Other' && {
								align: 'right',
								label: this.getItemPercentage(percentage),
								weight: 'semibold',
								width: 50
							}
						].filter(Boolean)
					};
				});
			}

			return (
				<div
					className='bb-tooltip-container'
					style={{position: 'static'}}
				>
					<TooltipChart header={header} rows={rows} />
				</div>
			);
		}
	}

	render() {
		const {devices, empty, height, metricLabel} = this.props;

		const barCount = devices.reduce((acc, {data}) => {
			const count = data.length;

			return count > acc ? count : acc;
		}, 0);

		return (
			<div className={CLASSNAME}>
				<ResponsiveContainer height={height}>
					<ComposedChart data={empty ? EMPTY_DATA : devices}>
						<CartesianGrid
							stroke={AXIS.gridStroke}
							strokeDasharray='3 3'
							vertical={false}
						>
							<Label
								position='center'
								value={sub(
									Liferay.Language.get(
										'empty-message-metric'
									),
									[metricLabel.toLowerCase()]
								)}
							/>
						</CartesianGrid>

						<XAxis
							axisLine={{stroke: AXIS.borderStroke}}
							dataKey='label'
							padding={{left: 20, right: 20}}
							tick={getAxisTickText('x')}
							tickLine={false}
							tickMargin={12}
						/>

						<XAxis
							axisLine={{stroke: AXIS.borderStroke}}
							dataKey='label'
							height={1}
							orientation='top'
							tick={false}
							tickLine={false}
							xAxisId='top'
						/>

						<YAxis
							allowDecimals={false}
							axisLine={{stroke: AXIS.borderStroke}}
							name={Liferay.Language.get('views')}
							tick={getAxisTickText('y', toThousands)}
							tickCount={6}
							tickLine={false}
							type='number'
							width={30}
						/>

						<YAxis
							axisLine={{stroke: AXIS.borderStroke}}
							orientation='right'
							tick={false}
							tickLine={false}
							type='number'
							width={1}
							yAxisId='right'
						/>

						<Tooltip
							content={this.renderTooltip}
							wrapperStyle={
								empty
									? {
											visibility: 'visible'
									  }
									: null
							}
						/>

						{range(0, barCount).map(i => (
							<Bar
								dataKey={`data[${i}].views`}
								fill={Colors.pallete[i]}
								isAnimationActive={false}
								key={i}
								stackId='devices'
							/>
						))}
					</ComposedChart>
				</ResponsiveContainer>
			</div>
		);
	}
}

export default OperatingSystem;
