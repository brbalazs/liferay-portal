import {
	Attribute,
	AttributeTypes,
	Breakdown,
	BreakdownData,
	BreakdownDataItem,
	DataTypes,
	DateGroupings,
	Filter,
	Operators,
	ParsedBreakdownData,
	ParsedBreakdownItem
} from './types';
import {formatTime} from 'shared/util/time';
import {formatUTCDate} from 'shared/util/date';

const DEFAULT_DATE_GROUPING = DateGroupings.Months;
const DEFAULT_DURATION_BIN = 60000;
const DEFAULT_NUMBER_BIN = 10;

const ATTRIBUTE_TYPE_LABEL_MAP = {
	[AttributeTypes.Account]: Liferay.Language.get('account'),
	[AttributeTypes.Event]: Liferay.Language.get('event'),
	[AttributeTypes.Individual]: Liferay.Language.get('individual'),
	[AttributeTypes.Session]: Liferay.Language.get('session')
};

export const BOOLEAN_OPTIONS = ['true', 'false'];

export const DATE_GROUPING_OPTIONS = [
	DateGroupings.Dates,
	DateGroupings.Months,
	DateGroupings.Years
];

export const DATE_OPTIONS = [
	Operators.EQ,
	Operators.LT,
	Operators.GT,
	Operators.Between
];

export const DURATION_OPTIONS = [Operators.GT, Operators.LT];

export const NUMBER_OPTIONS = [Operators.GT, Operators.LT, Operators.Between];

export const STRING_OPTIONS = [
	Operators.Contains,
	Operators.NotContains,
	Operators.EQ,
	Operators.NE
];

export const BOOLEAN_LABELS_MAP = {
	false: Liferay.Language.get('false'),
	true: Liferay.Language.get('true')
};

export const DATA_TYPE_LABELS_MAP = {
	[DataTypes.Boolean]: Liferay.Language.get('boolean'),
	[DataTypes.Date]: Liferay.Language.get('date'),
	[DataTypes.Duration]: Liferay.Language.get('duration'),
	[DataTypes.Number]: Liferay.Language.get('number'),
	[DataTypes.String]: Liferay.Language.get('string')
};

export const DATE_GROUPING_LABELS_MAP = {
	[DateGroupings.Dates]: Liferay.Language.get('date'),
	[DateGroupings.Months]: Liferay.Language.get('month'),
	[DateGroupings.Years]: Liferay.Language.get('year')
};

export const DATE_OPERATOR_LABELS_MAP = {
	[Operators.Between]: '-',
	[Operators.EQ]: '=',
	[Operators.GT]: Liferay.Language.get('after-fragment'),
	[Operators.LT]: Liferay.Language.get('before-fragment')
};

export const DATE_OPERATOR_LONGHAND_LABELS_MAP = {
	[Operators.Between]: Liferay.Language.get('is-between-fragment'),
	[Operators.EQ]: Liferay.Language.get('is-fragment'),
	[Operators.GT]: Liferay.Language.get('after-fragment'),
	[Operators.LT]: Liferay.Language.get('before-fragment')
};

export const DURATION_OPERATOR_LABELS_MAP = {
	[Operators.GT]: '>',
	[Operators.LT]: '<'
};

export const DURATION_OPERATOR_LONGHAND_LABELS_MAP = {
	[Operators.GT]: Liferay.Language.get('is-greater-than-fragment'),
	[Operators.LT]: Liferay.Language.get('is-less-than-fragment')
};

export const NUMBER_OPERATOR_LABELS_MAP = {
	[Operators.Between]: '-',
	[Operators.GT]: '>',
	[Operators.LT]: '<'
};

export const NUMBER_OPERATOR_LONGHAND_LABELS_MAP = {
	[Operators.Between]: Liferay.Language.get('between-fragment'),
	[Operators.GT]: Liferay.Language.get('is-greater-than-fragment'),
	[Operators.LT]: Liferay.Language.get('is-less-than-fragment')
};

export const STRING_OPERATOR_LABELS_MAP = {
	[Operators.Contains]: Liferay.Language.get('contains-fragment'),
	[Operators.NotContains]: Liferay.Language.get('not-contains-fragment'),
	[Operators.EQ]: Liferay.Language.get('is-fragment'),
	[Operators.NE]: Liferay.Language.get('is-not-fragment')
};

const getBooleanDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	filter: Filter
): [string, string] => [
	getDefaultDisplay(attribute, breakdown).join(' | '),
	BOOLEAN_LABELS_MAP[String(filter.value[0])]
];

const getDateDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	{operator, value: [startDate, endDate]}: Filter
): [string, string] => {
	const {dateGrouping} = breakdown;

	const dateGroupingLabel = DATE_GROUPING_LABELS_MAP[dateGrouping];

	const operatorLabel = DATE_OPERATOR_LABELS_MAP[operator];

	const formattedStartDate = formatUTCDate(startDate as string, 'll');

	const breakdownValue =
		operator === Operators.Between
			? `${formattedStartDate} ${operatorLabel} ${formatUTCDate(
					endDate as string,
					'll'
			  )}`
			: `${operatorLabel} ${formattedStartDate}`;

	return [
		getDefaultDisplay(attribute, breakdown).join(' | '),
		`${dateGroupingLabel}, ${breakdownValue}`
	];
};

const getDefaultDisplay = (
	{displayName, name}: Attribute,
	{type}: Breakdown
): [string, string] => [ATTRIBUTE_TYPE_LABEL_MAP[type], displayName || name];

const getDurationDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	{operator, value: [value]}: Filter
): [string, string] => {
	const bin = formatTime(breakdown.bin as number);
	const duration = formatTime(value as number);

	return [
		getDefaultDisplay(attribute, breakdown).join(' | '),
		`${bin}, ${DURATION_OPERATOR_LABELS_MAP[operator]} ${duration}`
	];
};

const getNumberDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	{operator, value: [start, end]}: Filter
): [string, string] => {
	const {bin} = breakdown;

	const operatorLabel = NUMBER_OPERATOR_LABELS_MAP[operator];

	const breakdownValue =
		operator === Operators.Between
			? `${start} ${operatorLabel} ${end}`
			: `${operatorLabel} ${start}`;

	return [
		getDefaultDisplay(attribute, breakdown).join(' | '),
		`${bin}, ${breakdownValue}`
	];
};

const getStringDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	{operator, value}: Filter
): [string, string] => [
	getDefaultDisplay(attribute, breakdown).join(' | '),
	`${STRING_OPERATOR_LABELS_MAP[operator]} "${value}"`
];

const BREAKDOWN_DISPLAY_MAP = {
	[DataTypes.Boolean]: getBooleanDisplay,
	[DataTypes.Date]: getDateDisplay,
	[DataTypes.Duration]: getDurationDisplay,
	[DataTypes.Number]: getNumberDisplay,
	[DataTypes.String]: getStringDisplay
};

export const getBreakdownDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	filter: Filter
): [string, string] => {
	let displayFn: (
		attribute: Attribute,
		breakdown: Breakdown,
		filter?: Filter
	) => [string, string] = getDefaultDisplay;

	if (filter) {
		displayFn = BREAKDOWN_DISPLAY_MAP[breakdown.dataType];
	}

	return displayFn(attribute, breakdown, filter);
};

export const isAttribute = (item: Attribute | Event): item is Attribute =>
	(item as Attribute).dataType !== undefined;

export const createBooleanBreakdown = ({attributeId, type}): Breakdown => ({
	attributeId,
	dataType: DataTypes.Boolean,
	type
});

export const createDateBreakdown = ({
	attributeId,
	dateGrouping = DEFAULT_DATE_GROUPING,
	type
}): Breakdown => ({
	attributeId,
	dataType: DataTypes.Date,
	dateGrouping,
	type
});

export const createDurationBreakdown = ({
	attributeId,
	bin = DEFAULT_DURATION_BIN,
	type
}): Breakdown => ({
	attributeId,
	bin,
	dataType: DataTypes.Duration,
	type
});

export const createNumberBreakdown = ({
	attributeId,
	bin = DEFAULT_NUMBER_BIN,
	type
}): Breakdown => ({
	attributeId,
	bin,
	dataType: DataTypes.Number,
	type
});

export const createStringBreakdown = ({attributeId, type}): Breakdown => ({
	attributeId,
	dataType: DataTypes.String,
	type
});

export const BREAKDOWN_FNS_MAP = {
	[DataTypes.Boolean]: createBooleanBreakdown,
	[DataTypes.Date]: createDateBreakdown,
	[DataTypes.Duration]: createDurationBreakdown,
	[DataTypes.Number]: createNumberBreakdown,
	[DataTypes.String]: createStringBreakdown
};

export const parserBreakdownData = (
	{breakdownItems}: BreakdownData | BreakdownDataItem,
	rows: ParsedBreakdownData = [{} as ParsedBreakdownItem],
	level: number = 1
): ParsedBreakdownData => {
	breakdownItems.forEach(data => {
		const {
			breakdownItems: nextBreakdownItems,
			isLeafNode: isLeafCurrentNode,
			...node
		} = data;

		const isLeafNextNode =
			nextBreakdownItems.length > 0 && nextBreakdownItems[0].isLeafNode;
		const currentRowIndex = rows.length - 1;

		Object.assign(rows[currentRowIndex], {
			[`breakdown${level}`]: {
				...node,
				rowSpan:
					!isLeafCurrentNode && !isLeafNextNode
						? nextBreakdownItems.length
						: 1
			},
			index: currentRowIndex
		});

		if (!isLeafNextNode) {
			parserBreakdownData(data, rows, level + 1);
		} else {
			Object.assign(rows[currentRowIndex], {
				events: nextBreakdownItems
			});

			rows.push({} as ParsedBreakdownItem);
		}
	});

	return rows.filter(obj => Object.keys(obj).length !== 0);
};

export const getMaxEventValue = (parsedData, compareToPrevious: boolean) =>
	parsedData.reduce(
		(prev, {events = []}) =>
			events.reduce(
				(
					prev2,
					{
						breakdownItems: segments = [],
						previousValue = 0,
						value = 0
					}
				) =>
					segments.length <= 1
						? Math.max(
								value,
								prev2,
								compareToPrevious && previousValue
						  )
						: segments.reduce(
								(prev3, {previousValue, value}) =>
									Math.max(
										value,
										prev3,
										compareToPrevious && previousValue
									),
								prev2
						  ),
				prev
			),
		0
	);
