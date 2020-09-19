import autobind from 'autobind-decorator';
import BarChartHTML from 'cerebro-shared/components/BarChartHTML';
import Chart, {DONUT_CHART} from 'shared/components/Chart';
import React from 'react';
import ReactDOMServer from 'react-dom/server';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {getUrl} from 'shared/util/urls';
import {Link} from 'react-router-dom';
import {PropTypes} from 'prop-types';
import {removeNumbers, removeSpacing} from 'shared/util/util';
import {toFixedPoint, toRounded} from 'shared/util/numbers';

const CLASSNAME = 'audience-report-chart';
const CLASSNAME_DONUT = `${CLASSNAME}-donut`;
const CLASSNAME_BAR_CHART = `${CLASSNAME}-bar`;

/**
 * Custom Donut Chart for AudienceReport component
 */
class Donut extends React.Component {
	static defaultProps = {
		data: [],
		empty: {
			show: false
		},
		total: 0,
		url: ''
	};

	static propTypes = {
		data: PropTypes.array,
		empty: PropTypes.shape({
			message: PropTypes.string,
			show: PropTypes.bool
		}),
		id: PropTypes.string,
		total: PropTypes.number,
		url: PropTypes.string
	};

	constructor(props) {
		super(props);

		this._legendElementRef = React.createRef();
	}

	/**
	 * Disable Legend Element
	 */
	disableLegendElement() {
		this._legendElementRef.current.classList.remove('enable-interaction');
	}

	/**
	 * Enable Legend Element
	 */
	enableLegendElement() {
		this._legendElementRef.current.classList.add('enable-interaction');
	}

	/**
	 * Handle Legend Move Over
	 */
	@autobind
	handleLegendMouseOver() {
		this.enableLegendElement();
	}

	/**
	 * Handle Legend Mouse Out
	 */
	@autobind
	handleLegendMouseOut() {
		this.disableLegendElement();
	}

	/**
	 * Handle Point Mouse Out
	 */
	@autobind
	handlePointMouseOut() {
		this.disableLegendElement();
	}

	isValidLink(title, url) {
		const labelKnownIndividuals = Liferay.Language.get(
			'segmented-visitors'
		);

		return url && title.indexOf(labelKnownIndividuals) !== -1;
	}

	/**
	 * Render Legend Item
	 * @param {string} title
	 * @param {string} color
	 */
	renderLegendItem(title, color, url) {
		const LegendTemplateColumn = () => (
			<div className='legend-template-column'>
				<span className='circle' style={{backgroundColor: color}} />
				<span className='text-truncate'>{removeNumbers(title)}</span>
			</div>
		);

		return ReactDOMServer.renderToString(
			<li data-title={removeSpacing(title)}>
				{this.isValidLink(title, url) ? (
					<a href={decodeURIComponent(url)}>
						<LegendTemplateColumn />
					</a>
				) : (
					<LegendTemplateColumn />
				)}
			</li>
		);
	}

	@autobind
	alignTooltip(values, width, height) {
		const arrowPopoverSize = 6;
		const tooltipDistance = 12;

		const {layerX, layerY} = window.event;

		return {
			left: layerX + (arrowPopoverSize + tooltipDistance),
			top: layerY - height / 2
		};
	}

	@autobind
	renderTooltip(data) {
		const {id, value} = data[0];

		return ReactDOMServer.renderToString(
			<TooltipChart
				rows={[
					{
						columns: [
							{
								className: 'pt-0',
								label: () => (
									<span style={{whiteSpace: 'nowrap'}}>
										<strong>
											{`${toFixedPoint(value)}`}
										</strong>

										{` ${removeNumbers(id)}`}
									</span>
								)
							}
						]
					}
				]}
			/>
		);
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

	render() {
		const {
			data,
			empty: {show: isEmpty},
			id,
			total,
			url
		} = this.props;

		if (isEmpty) {
			return this.renderEmptyState();
		}

		return (
			<div className={CLASSNAME_DONUT}>
				<div className='total'>{toFixedPoint(total)}</div>

				<Chart
					chartType={DONUT_CHART}
					data={data}
					dataId={id}
					donut={{
						label: {
							format: (total, value) =>
								`${toRounded(value * 100, 2)}%`,
							ratio: 1,
							threshold: 0
						},
						padAngle: 0.03,
						width: 55
					}}
					height={280}
					id={id}
					legend={{
						contents: {
							bindto: `#${id}`,
							template: (title, color) =>
								this.renderLegendItem(title, color, url)
						},
						item: {
							onclick: () => false
						},
						show: true
					}}
					tooltip={{
						contents: this.renderTooltip,
						position: this.alignTooltip
					}}
				/>

				<ul
					className={`${CLASSNAME_DONUT}-legend legend-template`}
					id={id}
					onBlur={this.handleLegendMouseOut}
					onFocus={this.handleLegendMouseOver}
					onMouseOut={this.handleLegendMouseOut}
					onMouseOver={this.handleLegendMouseOver}
					ref={this._legendElementRef}
				/>
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
const Title = ({children}) => (
	<h4 className='mb-3 text-center text-secondary title'>{children}</h4>
);

/**
 * Get Individuals URL
 * @description Get url to navigate in a dashboard
 */
const getIndividualsUrl = (path, router) => getUrl(path, router);

/**
 * Audience Report component
 * @param object} param0
 */
const AudienceReport = ({
	knownIndividuals,
	pathUrl,
	router,
	segments,
	uniqueVisitors
}) => (
	<div className={`${CLASSNAME} row w-100`}>
		<div className='col-sm-6'>
			<div className='row'>
				<div className='col-sm-6'>
					<Title>{Liferay.Language.get('visitors')}</Title>

					{renderDonutChart({
						...uniqueVisitors,
						url: getIndividualsUrl(pathUrl, router)
					})}
				</div>
				<div className='col-sm-6'>
					<Title>
						<Link to={getIndividualsUrl(pathUrl, router)}>
							{Liferay.Language.get('segmented-visitors')}
						</Link>
					</Title>

					{renderDonutChart(knownIndividuals)}
				</div>
			</div>
		</div>
		<div className='col-sm-6 pl-5'>
			<Title>{Liferay.Language.get('visitor-segments')}</Title>

			{renderBarChart(segments)}
		</div>
	</div>
);

export default AudienceReport;
