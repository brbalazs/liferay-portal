import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import BasePage from 'shared/components/base-page';
import Card from 'shared/components/Card';
import Chart, {BAR_CHART} from 'shared/components/Chart';
import CollapsibleOverlay from 'shared/components/CollapsibleOverlay';
import FaroConstants from 'shared/util/constants';
import Form from 'shared/components/form';
import FormSelectFieldInput from 'contacts/components/form/SelectFieldInput';
import HistogramChart from 'shared/components/HistogramChart';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import Spinner from 'shared/components/Spinner';
import {
	ACCOUNT_NAME,
	FAMILY_NAME,
	GIVEN_NAME,
	paginationConfig,
	paginationDefaults
} from 'shared/util/pagination';
import {
	accountsListColumns,
	individualsListColumns
} from 'shared/util/table-columns';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {ClaySelectWithOption} from '@clayui/select';
import {compose, withSelectedPoint, withStatefulPagination} from 'shared/hoc';
import {
	CONJUNCTIONS,
	RELATIONAL_OPERATORS
} from 'contacts/components/segment-editor/dynamic/utils/constants';
import {connect} from 'react-redux';
import {createNumberMask} from 'text-mask-addons';
import {getFinitePercent} from 'shared/util/change';
import {hasChanges} from 'shared/util/react';
import {List, Map} from 'immutable';
import {noop, omit, pickBy, truncate} from 'lodash';
import {PropTypes} from 'prop-types';
import {setUriQueryValues} from 'shared/util/router';
import {sub} from 'shared/util/lang';

const {
	fieldContexts: {demographics, organization},
	fieldTypes: {number},
	pagination: {orderDefault}
} = FaroConstants;

const SearchableEntityTableHOC = withStatefulPagination(
	SearchableEntityTable,
	{
		defaultOrderByFields: [
			{
				fieldName: GIVEN_NAME,
				orderBy: orderDefault
			},
			{
				fieldName: FAMILY_NAME,
				orderBy: orderDefault
			}
		]
	},
	props => omit(props, 'onSearchValueChange')
);

export const CONTEXT_OPTIONS = [
	{
		label: Liferay.Language.get('individuals'),
		value: demographics
	},
	{
		label: Liferay.Language.get('accounts'),
		value: organization
	}
];

const MAX_ROWS = 100;

export function getContextLabel(context) {
	const contextOption = CONTEXT_OPTIONS.find(({value}) => value === context);

	return contextOption ? contextOption.label : null;
}

const CHART_ID = 'DISTRIBUTION_CHART';
const CHART_DATA_ID = 'count';
const CHART_PADDING = 60;
const DEFAULT_NUMBER_OF_BINS = 10;

const DEFAULT_SELECTED_POINT = 0;
const BAR_WIDTH = 60;

export const numberOfBinsMask = createNumberMask({
	includeThousandsSeparator: false,
	prefix: ''
});

function formatTickVal(name, percent, showPercentage) {
	return showPercentage
		? `${truncate(name, {length: 50})} - ${percent}%`
		: `${truncate(name, {length: 50})}`;
}

/**
 * Get the chart height and bar config by calculating
 * the length of the data multiplied by bar width.
 * @param {number} dataLength - The length of the data.
 * @param {boolean} histogram - Whether the chart is a histogram type.
 * @returns {Object} Chart height and bar width config.
 */
export function getChartSizeConfig(dataLength, histogram) {
	const height = BAR_WIDTH * dataLength + CHART_PADDING;

	return histogram ? {height} : {bar: {width: {ratio: 0.8}}, height};
}

@hasRequest
export class Distribution extends React.Component {
	static defaultProps = {
		...paginationDefaults,
		contextOptions: CONTEXT_OPTIONS,
		delta: 10,
		id: '',
		numberOfBins: DEFAULT_NUMBER_OF_BINS,
		selectedPoint: DEFAULT_SELECTED_POINT,
		title: Liferay.Language.get('breakdown-of-known-members')
	};

	static propTypes = {
		...paginationConfig,
		channelId: PropTypes.string,
		contextOptions: PropTypes.arrayOf(
			PropTypes.shape({label: PropTypes.string, value: PropTypes.string})
		),
		distributionsKey: PropTypes.string.isRequired,
		fetchDistribution: PropTypes.func.isRequired,
		fieldDistributionIList: PropTypes.instanceOf(List),
		fieldMappingId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		hasSelectedPoint: PropTypes.bool,
		history: PropTypes.object.isRequired,
		id: PropTypes.string,
		knownIndividualCount: PropTypes.number,
		loading: PropTypes.bool,
		numberOfBins: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
		onPointSelect: PropTypes.func.isRequired,
		selectedContext: PropTypes.string,
		selectedPoint: PropTypes.number,
		title: PropTypes.string
	};

	state = {
		fieldMappingSelected: null,
		histogram: false,
		selectedContext: demographics,
		showIndividualsPreview: false
	};

	_chartRef = React.createRef();
	_formRef = React.createRef();
	_formSelectFieldInputRef = React.createRef();

	componentDidMount() {
		this.fetchFieldMappings();
	}

	componentDidUpdate(prevProps, prevState) {
		const {fieldMappingSelected} = this.state;

		const fieldMappingSelectedChanged =
			prevState.fieldMappingSelected &&
			hasChanges(prevState, this.state, 'fieldMappingSelected');

		const histogramNumberOfBinsChanged =
			hasChanges(prevProps, this.props, 'numberOfBins') &&
			fieldMappingSelected.rawType === number;

		if (fieldMappingSelectedChanged || histogramNumberOfBinsChanged) {
			this.fetchDistributionData();
		}
	}

	@autobind
	buildNumberFilter([min, max]) {
		const {
			props: {fieldDistributionIList, selectedPoint},
			state: {
				fieldMappingSelected: {context, name}
			}
		} = this;

		const getFilter = (operator, value) =>
			`${context}/${name}/value ${operator} ${value}`;

		const filter = [getFilter(RELATIONAL_OPERATORS.GE, min)];

		if (fieldDistributionIList.size - 1 === selectedPoint) {
			filter.push(getFilter(RELATIONAL_OPERATORS.LE, max));
		} else {
			filter.push(getFilter(RELATIONAL_OPERATORS.LT, max));
		}

		return filter.join(` ${CONJUNCTIONS.AND} `);
	}

	@autobind
	buildStringFilter(distributionValues) {
		const {
			fieldMappingSelected: {context, name}
		} = this.state;

		const filter = `${context}/${name}/value ${RELATIONAL_OPERATORS.EQ} '${
			distributionValues[0]
		}'`;

		return filter;
	}

	@autobind
	fetchAccounts({
		delta,
		fieldMappingSelected: {name: propertyName},
		filter,
		orderBy,
		page,
		query
	}) {
		const {groupId, id} = this.props;

		return API.accounts.search(
			pickBy({
				delta,
				filter,
				groupId,
				includePropertyNames: [propertyName],
				individualSegmentId: id,
				orderByFields: [
					{
						fieldName: ACCOUNT_NAME,
						orderBy
					}
				],
				page,
				query
			})
		);
	}

	@autoCancel
	fetchDistributionData() {
		const {
			props: {
				channelId,
				fetchDistribution,
				fieldMappingId,
				groupId,
				id,
				numberOfBins
			},
			state: {selectedContext}
		} = this;

		return fetchDistribution(
			pickBy({
				channelId,
				context: selectedContext,
				count: MAX_ROWS,
				fieldMappingId,
				groupId,
				id,
				individualSegmentId: id,
				numberOfBins
			})
		).catch(noop);
	}

	@autoCancel
	fetchFieldMappings() {
		const {fieldMappingId, groupId, history} = this.props;

		const fieldMappingFn = fieldMappingId
			? () => API.fieldMappings.fetch({fieldMappingId, groupId})
			: () => API.fieldMappings.fetchDefault(groupId);

		return fieldMappingFn()
			.then(fieldMapping => {
				if (!fieldMappingId) {
					history.replace(
						setUriQueryValues({
							fieldMappingId: fieldMapping.id
						})
					);
				}

				this.setState(
					{
						fieldMappingSelected: fieldMapping,
						histogram: fieldMapping.rawType === number,
						selectedContext: fieldMapping.context
					},
					() => this.fetchDistributionData()
				);
			})
			.catch(noop);
	}

	@autobind
	fetchIndividuals({
		delta,
		fieldMappingSelected: {name: propertyName},
		filter,
		orderBy,
		page,
		query
	}) {
		const {channelId, groupId, id} = this.props;

		return API.individuals.search(
			pickBy({
				channelId,
				delta,
				filter,
				groupId,
				includePropertyNames: [propertyName],
				individualSegmentId: id,
				orderByFields: [
					{
						fieldName: GIVEN_NAME,
						orderBy
					},
					{
						fieldName: FAMILY_NAME,
						orderBy
					}
				],
				page,
				query
			})
		);
	}

	@autobind
	focusSelectFieldInput() {
		this._formSelectFieldInputRef.current.focus();
	}

	getAxisXConfig() {
		const {
			props: {fieldDistributionIList, knownIndividualCount},
			state: {histogram, selectedContext}
		} = this;

		return {
			tick: {
				format: (index, name) =>
					formatTickVal(
						name,
						getFinitePercent(
							fieldDistributionIList.getIn([index, 'count']),
							knownIndividualCount
						),
						!histogram && selectedContext === demographics
					),
				multiline: false
			},
			type: 'category'
		};
	}

	getNumberOfBins() {
		const {numberOfBins} = this.props;

		return Number(numberOfBins);
	}

	getChartData() {
		const {fieldDistributionIList, knownIndividualCount} = this.props;

		const fieldDistributionList = fieldDistributionIList.toJS();

		return fieldDistributionList.length && knownIndividualCount
			? [
					{
						axis: 'y2',
						data: fieldDistributionList.map(({count}) => count),
						id: 'count'
					},
					{
						data: fieldDistributionList.map(({values}) =>
							values.length > 1 ? values[1] : values[0]
						),
						id: 'value'
					}
			  ]
			: [];
	}

	getFilter() {
		const {
			props: {fieldDistributionIList, selectedPoint},
			state: {
				fieldMappingSelected: {rawType}
			}
		} = this;

		const buildFn =
			rawType === number
				? this.buildNumberFilter
				: this.buildStringFilter;

		const distributionValues = fieldDistributionIList
			.getIn([selectedPoint, 'values'], new List())
			.toJS();

		return buildFn(distributionValues);
	}

	@autobind
	handleNumberOfBinsChange(event) {
		const {name, value} = event.target;

		const {history} = this.props;

		const {errors} = this._formRef.current.getFormikBag();

		const numberOfBins = Number(value);

		const curNumberOfBins = this.getNumberOfBins();

		if (value && curNumberOfBins !== numberOfBins && !errors[name]) {
			history.replace(setUriQueryValues({numberOfBins}));
		}
	}

	@autobind
	handleBreakdownSelect(fieldMapping) {
		const {id, rawType} = fieldMapping;

		const {fieldMappingId, history} = this.props;

		const histogram = rawType === number;

		this.handleOverlayClose();

		this.setState({
			fieldMappingSelected: fieldMapping,
			histogram
		});

		if (fieldMappingId !== id) {
			history.replace(setUriQueryValues({fieldMappingId: id}));
		}

		if (histogram) {
			history.replace(
				setUriQueryValues({numberOfBins: DEFAULT_NUMBER_OF_BINS})
			);
		}
	}

	@autobind
	handleChartSelect({index}) {
		const {onPointSelect, selectedPoint} = this.props;

		const alreadySelected = selectedPoint === index;

		this.setState({
			showIndividualsPreview: alreadySelected ? false : true
		});

		onPointSelect({index: alreadySelected ? null : index});
	}

	@autobind
	handleContextSelect(event) {
		const {value} = event.target;

		this.setState({selectedContext: value}, () => {
			const {setTouched, validateField} = this._formRef.current;

			setTouched({breakdown: false});

			validateField('breakdown');
		});
	}

	@autobind
	handleOverlayClose() {
		const {onPointSelect} = this.props;
		const chartRef = this._chartRef.current._chartRef.current;

		this.setState({
			showIndividualsPreview: false
		});

		onPointSelect({index: null});

		chartRef && chartRef.chart.unselect();
	}

	@autobind
	validateFieldMapping() {
		const {
			fieldMappingSelected: {context},
			selectedContext
		} = this.state;

		if (context !== selectedContext) {
			this.focusSelectFieldInput();

			return sub(Liferay.Language.get('invalid-breakdown-for-x'), [
				getContextLabel(selectedContext)
			]);
		}

		return '';
	}

	render() {
		const {
			props: {
				channelId,
				contextOptions,
				fieldDistributionIList,
				groupId,
				hasSelectedPoint,
				loading,
				selectedPoint,
				title
			},
			state: {
				fieldMappingSelected,
				histogram,
				selectedContext,
				showIndividualsPreview
			}
		} = this;

		const numberOfBins = this.getNumberOfBins();

		const ChartComponent = histogram ? HistogramChart : Chart;

		return (
			<>
				<BasePage.Body>
					<Card>
						<Card.Header>
							<Card.Title>{title}</Card.Title>
						</Card.Header>

						<Card.Body>
							<Form
								enableReinitialize
								initialValues={{
									breakdown: fieldMappingSelected,
									numberOfBins
								}}
								ref={this._formRef}
							>
								<Form.Form className='chart-options'>
									<Form.Group autoFit>
										{contextOptions.length > 1 && (
											<>
												<Form.GroupItem shrink>
													{/* eslint-disable react/jsx-handler-names */}
													<ClaySelectWithOption
														className='context-select'
														onChange={
															this
																.handleContextSelect
														}
														options={contextOptions}
														value={selectedContext}
													/>
													{/* eslint-enable react/jsx-handler-names */}
												</Form.GroupItem>

												<Form.GroupItem label shrink>
													<Form.Label htmlFor='breakdown'>
														{Liferay.Language.get(
															'by'
														)}
													</Form.Label>
												</Form.GroupItem>
											</>
										)}

										<Form.GroupItem shrink>
											<FormSelectFieldInput
												context={selectedContext}
												groupId={groupId}
												name='breakdown'
												/* eslint-disable */
												onSelect={
													this.handleBreakdownSelect
												}
												/* eslint-enable */
												ref={
													this
														._formSelectFieldInputRef
												}
												validate={
													this.validateFieldMapping
												}
											/>
										</Form.GroupItem>

										{histogram && (
											<>
												<Form.GroupItem label shrink>
													<Form.Label htmlFor='numberOfBins'>
														{Liferay.Language.get(
															'number-of-bins'
														)}
													</Form.Label>
												</Form.GroupItem>

												<Form.GroupItem className='chart-options-bins-input'>
													{/* eslint-disable */}
													<Form.Input
														mask={numberOfBinsMask}
														name='numberOfBins'
														onChange={
															this
																.handleNumberOfBinsChange
														}
														showSuccess={false}
													/>
													{/* eslint-enable */}
												</Form.GroupItem>
											</>
										)}
									</Form.Group>
								</Form.Form>
							</Form>

							{loading ? (
								<Spinner spacer />
							) : (
								<div className='chart-container'>
									<ChartComponent
										{...getChartSizeConfig(
											fieldDistributionIList.size,
											histogram
										)}
										axisRotated
										axisX={this.getAxisXConfig()}
										axisY={{
											tick: {
												show: false,
												text: {
													show: false
												}
											}
										}}
										axisY2={{show: true}}
										bar={{radius: {ratio: 0.1}}}
										chartType={BAR_CHART}
										data={this.getChartData()}
										dataId={CHART_DATA_ID}
										id={CHART_ID}
										initialSelectedIndexes={[selectedPoint]}
										loading={loading}
										noResultsProps={{
											icon: {symbol: 'document'}
										}}
										onPointSelect={this.handleChartSelect}
										ref={this._chartRef}
										x='value'
									/>
								</div>
							)}
						</Card.Body>
					</Card>
				</BasePage.Body>

				{fieldMappingSelected && hasSelectedPoint && (
					<CollapsibleOverlay
						onClose={this.handleOverlayClose}
						title={sub(
							fieldMappingSelected.context === demographics
								? Liferay.Language.get('individuals-matching-x')
								: Liferay.Language.get('accounts-matching-x'),
							[
								<span className='distribution-name' key='NAME'>
									{`"${fieldMappingSelected.name}"`}
								</span>
							],
							false
						)}
						visible={showIndividualsPreview}
					>
						<SearchableEntityTableHOC
							columns={[
								fieldMappingSelected.context === demographics
									? individualsListColumns.getName({
											channelId,
											groupId
									  })
									: accountsListColumns.getName({
											channelId,
											groupId
									  }),
								{
									accessor: `properties.${fieldMappingSelected.name}`,
									label: fieldMappingSelected.name,
									sortable: false
								}
							]}
							dataSourceFn={
								fieldMappingSelected.context === demographics
									? this.fetchIndividuals
									: this.fetchAccounts
							}
							dataSourceParams={{
								fieldMappingSelected,
								filter: this.getFilter(),
								selectedPoint
							}}
							rowIdentifier='id'
						/>
					</CollapsibleOverlay>
				)}
			</>
		);
	}
}

export default compose(
	connect(
		(
			state,
			{
				distributionsKey,
				fieldMappingId,
				knownIndividualCount,
				selectedContext
			}
		) => {
			const distributionIMap = state.getIn(
				['distributions', distributionsKey],
				new Map()
			);

			return {
				fieldDistributionIList:
					distributionIMap.getIn(['data', 'items']) || new List(),
				fieldMappingId:
					fieldMappingId || distributionIMap.get('fieldMappingId'),
				loading:
					distributionIMap.get('loading') ||
					knownIndividualCount === null,
				selectedContext:
					selectedContext || distributionIMap.get('context')
			};
		}
	),
	withSelectedPoint
)(Distribution);
