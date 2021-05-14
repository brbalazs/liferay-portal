import ChartTooltip from 'shared/components/chart-tooltip';
import HTMLBarChart, {Grid, Item} from 'shared/components/HTMLBarChart';
import InfoPopover from 'shared/components/InfoPopover';
import React, {useState} from 'react';
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

type Datapoint = {
	color: string;
	count: number;
	label: string;
};

type Dataset = {
	data: Datapoint[];
	empty: Empty;
	total: number;
};

type Empty = {
	message: string;
	show: boolean;
};

interface IDonutProps extends Dataset {
	height?: number;
}

const Donut: React.FC<IDonutProps> = ({
	data = [],
	empty: {message: emptyMessage, show: isEmpty = false},
	height = 360,
	total = 0
}) => {
	const [hoverIndex, setHoverIndex] = useState<number>(-1);

	const renderActiveShape = ({
		cx,
		cy,
		endAngle,
		fill,
		innerRadius,
		outerRadius,
		startAngle
	}) => (
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

	const renderBarLabel = ({
		cx,
		cy,
		innerRadius,
		midAngle,
		outerRadius,
		percent
	}) => {
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
	};

	const renderTooltip = ({active, payload}) => {
		if (active && !!payload.length) {
			const {count, label} = get(payload, [0, 'payload'], {});

			return (
				<div
					className='bb-tooltip-container'
					style={{position: 'static'}}
				>
					<ChartTooltip
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
	};

	const handleSetHoverIndex = (e, index) => setHoverIndex(index);

	const handleResetHoverIndex = () => setHoverIndex(-1);

	if (isEmpty) {
		return (
			<div className={`${CLASSNAME_DONUT}-empty`}>
				<div className='total'>{total}</div>

				<div className='mt-5 text-center pl-4 pr-4'>{emptyMessage}</div>
			</div>
		);
	}

	return (
		<div className={CLASSNAME_DONUT}>
			<ResponsiveContainer height={height}>
				<PieChart>
					<Tooltip content={renderTooltip} />

					{/* eslint-disable jsx-a11y/mouse-events-have-key-events
					 */}
					<Legend
						formatter={(value, {payload: {label}}) => (
							<span className='legend-item'>{label}</span>
						)}
						layout='vertical'
						onMouseMove={handleSetHoverIndex}
						onMouseOut={handleResetHoverIndex}
						verticalAlign='bottom'
					/>

					<Pie
						activeIndex={hoverIndex}
						activeShape={renderActiveShape}
						blendStroke
						cy={142}
						data={data}
						dataKey='count'
						endAngle={-270}
						innerRadius='50%'
						isAnimationActive={false}
						label={renderBarLabel}
						labelLine={false}
						legendType='circle'
						onMouseMove={handleSetHoverIndex}
						onMouseOut={handleResetHoverIndex}
						outerRadius='90%'
						startAngle={90}
					>
						<Label position='center' value={toFixedPoint(total)} />

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
};

const Title: React.FC<React.ComponentProps<typeof InfoPopover>> = ({
	content,
	title
}) => (
	<div className='d-inline-flex gap'>
		<h4 className='mb-3 text-center text-secondary title'>{title}</h4>

		{content && <InfoPopover content={content} title={title} />}
	</div>
);

interface IAudienceReportProps {
	knownIndividuals: Dataset;
	knownIndividualsTitle: string;
	segments: {
		disableScroll: boolean;
		formatSpacement: boolean;
		grid: Grid;
		items: Item[];
	};
	segmentsTitle: string;
	uniqueVisitors: Dataset;
	uniqueVisitorsTitle: string;
	metricAction: React.ReactText;
}

const AudienceReport: React.FC<IAudienceReportProps> = ({
	knownIndividuals,
	knownIndividualsTitle,
	segments,
	segmentsTitle = Liferay.Language.get('viewer-segments'),
	uniqueVisitors,
	uniqueVisitorsTitle = Liferay.Language.get('visitors'),
	metricAction = Liferay.Language.get('view')
}) => (
	<div className={`${CLASSNAME} row w-100`}>
		<div className='col-sm-6'>
			<div className='row'>
				<div className='col-sm-6 text-center'>
					<Title title={uniqueVisitorsTitle} />

					<Donut {...uniqueVisitors} />
				</div>

				<div className='col-sm-6 text-center'>
					<Title
						content={
							sub(
								Liferay.Language.get(
									'a-snapshot-of-the-audience-captured-at-the-time-of-x.-this-does-not-reflect-the-current-state-of-the-visitors-segments'
								),
								[metricAction]
							) as string
						}
						title={knownIndividualsTitle}
					/>

					<Donut {...knownIndividuals} />
				</div>
			</div>
		</div>

		<div className='col-sm-6 pl-5'>
			<Title
				content={
					sub(
						Liferay.Language.get(
							'a-snapshot-of-segments-captured-at-the-time-of-x.-this-does-not-relect-the-current-state-of-the-visitors-segments'
						),
						[metricAction]
					) as string
				}
				title={segmentsTitle}
			/>

			<div className={CLASSNAME_BAR_CHART}>
				<HTMLBarChart {...segments} />
			</div>
		</div>
	</div>
);

export default AudienceReport;
