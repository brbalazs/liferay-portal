import autobind from 'autobind-decorator';
import BarChartHTML from 'cerebro-shared/components/BarChartHTML';
import InfoPopover from 'shared/components/InfoPopover';
import PropTypes from 'prop-types';
import React from 'react';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {AUDIENCE_VIEWER_MODE} from 'shared/util/constants';
import {AXIS} from 'shared/util/recharts';
import {
	Cell,
	Label,
	Legend,
	Pie,
	PieChart,
	ResponsiveContainer,
	Sector,
	Text,
	Tooltip
} from 'recharts';
import {get} from 'lodash';
import {sub} from 'shared/util/lang';
import {toFixedPoint, toRounded} from 'shared/util/numbers';

const CLASSNAME = 'audience-report-chart';
const CLASSNAME_DONUT = `${CLASSNAME}-donut`;
const CLASSNAME_BAR_CHART = `${CLASSNAME}-bar`;

class Donut extends React.Component {
	static defaultProps = {
		data: [],
		empty: {
			show: false
		},
		height: 360,
		total: 0
	};

	static propTypes = {
		data: PropTypes.array,
		empty: PropTypes.shape({
			message: PropTypes.string,
			show: PropTypes.bool
		}),
		total: PropTypes.number
	};

	state = {
		hoverIndex: -1
	};

	@autobind
	renderActiveShape({
		cx,
		cy,
		endAngle,
		fill,
		innerRadius,
		outerRadius,
		startAngle
	}) {
		return (
			<g>
				<Sector
					cx={cx}
					cy={cy}
					endAngle={endAngle}
					fill={fill}
					innerRadius={innerRadius}
					outerRadius={outerRadius + 4}
					startAngle={startAngle}
				/>
			</g>
		);
	}

	@autobind
	renderBarLabel({cx, cy, innerRadius, midAngle, outerRadius, percent}) {
		const RADIAN = Math.PI / 180;

		const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
		const x = cx + radius * Math.cos(-midAngle * RADIAN);
		const y = cy + radius * Math.sin(-midAngle * RADIAN);

		if (percent) {
			return (
				<Text
					style={{
						fill: 'black',
						font: AXIS.font,
						fontSize: '1rem',
						fontWeight: 600
					}}
					textAnchor='middle'
					x={x}
					y={y}
				>
					{`${toRounded(percent * 100, 2)}%`}
				</Text>
			);
		}
	}

	renderEmptyState() {
		const {
			empty: {message},
			total
		} = this.props;

		return (
			<div className={`${CLASSNAME_DONUT}-empty`}>
				<div className='total'>{total}</div>
				<div className='mt-5 text-center pl-4 pr-4'>{message}</div>
			</div>
		);
	}

	@autobind
	renderTooltip({active, payload}) {
		if (active && !!payload.length) {
			const {count, label} = get(payload, [0, 'payload'], {});

			return (
				<div
					className='bb-tooltip-container'
					style={{position: 'static'}}
				>
					<TooltipChart
						rows={[
							{
								columns: [
									{
										className: 'pt-0',
										label: () => (
											<span
												style={{whiteSpace: 'nowrap'}}
											>
												<strong>
													{`${toFixedPoint(count)}`}
												</strong>

												{` ${label}`}
											</span>
										)
									}
								]
							}
						]}
					/>
				</div>
			);
		}
	}

	render() {
		const {
			props: {
				data,
				empty: {show: isEmpty},
				height,
				total
			},
			state: {hoverIndex}
		} = this;

		if (isEmpty) {
			return this.renderEmptyState();
		}

		return (
			<div className={CLASSNAME_DONUT}>
				<ResponsiveContainer height={height}>
					<PieChart>
						<Tooltip content={this.renderTooltip} />

						{/* eslint-disable jsx-a11y/mouse-events-have-key-events
						 */}
						<Legend
							formatter={(value, {payload: {label}}) => (
								<span className='legend-item'>{label}</span>
							)}
							layout='vertical'
							onMouseMove={(e, index) =>
								this.setState({hoverIndex: index})
							}
							onMouseOut={() => this.setState({hoverIndex: -1})}
							verticalAlign='bottom'
						/>

						<Pie
							activeIndex={hoverIndex}
							activeShape={this.renderActiveShape}
							blendStroke
							cy={142}
							data={data}
							dataKey='count'
							endAngle={-270}
							innerRadius='50%'
							isAnimationActive={false}
							label={this.renderBarLabel}
							labelLine={false}
							legendType='circle'
							onMouseMove={(e, index) =>
								this.setState({hoverIndex: index})
							}
							onMouseOut={() => this.setState({hoverIndex: -1})}
							outerRadius='90%'
							startAngle={90}
						>
							<Label
								position='center'
								value={toFixedPoint(total)}
							/>

							{data.map(({color}, index) => (
								<Cell
									fill={color}
									fillOpacity={
										hoverIndex >= 0 && hoverIndex !== index
											? 0.2
											: 1
									}
									key={`cell-${index}`}
									strokeOpacity={
										hoverIndex >= 0 && hoverIndex !== index
											? 0
											: 1
									}
								/>
							))}
						</Pie>
						{/* eslint-enable jsx-a11y/mouse-events-have-key-events */}
					</PieChart>
				</ResponsiveContainer>
			</div>
		);
	}
}

/**
 * Render BarChartHTML Component
 * @param {array} data
 */
const renderBarChart = props => (
	<div className={CLASSNAME_BAR_CHART}>
		<BarChartHTML {...props} />
	</div>
);

/**
 * Render Donut Chart component
 * @param {object} param
 */
const renderDonutChart = props => <Donut {...props} />;

/**
 * Render Title for Audience Report
 * @param {object} param
 */
const Title = ({helperText, title}) => (
	<div className='d-inline-flex gap'>
		<h4 className='mb-3 text-center text-secondary title'>{title}</h4>

		{helperText && <InfoPopover content={helperText} title={title} />}
	</div>
);

/**
 * Audience Report component
 * @param object} param0
 */
const AudienceReport = ({
	knownIndividuals,
	knownIndividualsTitle,
	segments,
	segmentsTitle,
	uniqueVisitors,
	uniqueVisitorsTitle,
	viewerMode
}) => (
	<div className={`${CLASSNAME} row w-100`}>
		<div className='col-sm-6'>
			<div className='row'>
				<div className='col-sm-6 text-center'>
					<Title
						title={
							uniqueVisitorsTitle ||
							Liferay.Language.get('visitors')
						}
					/>

					{renderDonutChart(uniqueVisitors)}
				</div>
				<div className='col-sm-6 text-center'>
					<Title
						helperText={
							viewerMode &&
							sub(
								Liferay.Language.get(
									'a-snapshot-of-the-audience-captured-at-the-time-of-x.-this-does-not-reflect-the-current-state-of-the-visitors-segments'
								),
								[
									viewerMode === AUDIENCE_VIEWER_MODE.VIEW
										? Liferay.Language.get('view')
										: Liferay.Language.get('preview')
								]
							)
						}
						title={
							knownIndividualsTitle ||
							Liferay.Language.get('segmented-visitors')
						}
					/>

					{renderDonutChart(knownIndividuals)}
				</div>
			</div>
		</div>
		<div className='col-sm-6 pl-5'>
			<Title
				helperText={
					viewerMode &&
					sub(
						Liferay.Language.get(
							'a-snapshot-of-segments-captured-at-the-time-of-x.-this-does-not-relect-the-current-state-of-the-visitors-segments'
						),
						[
							viewerMode === AUDIENCE_VIEWER_MODE.VIEW
								? Liferay.Language.get('view')
								: Liferay.Language.get('preview')
						]
					)
				}
				title={segmentsTitle || Liferay.Language.get('viewer-segments')}
			/>

			{renderBarChart(segments)}
		</div>
	</div>
);

export default AudienceReport;
