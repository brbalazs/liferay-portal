import {
	Attribute,
	AttributeTypes,
	Breakdown,
	DataTypes,
	DateGroupings,
	Filter,
	Operators
} from './types';
import {formatTime} from 'shared/util/time';
import {formatUTCDate} from 'shared/util/date';

const ATTRIBUTE_TYPE_LABEL_MAP = {
	[AttributeTypes.Account]: Liferay.Language.get('account'),
	[AttributeTypes.Event]: Liferay.Language.get('event'),
	[AttributeTypes.Individual]: Liferay.Language.get('individual'),
	[AttributeTypes.Session]: Liferay.Language.get('session')
};

const BOOLEAN_LABEL_MAP = {
	false: Liferay.Language.get('false'),
	true: Liferay.Language.get('true')
};

const DATE_GROUPING_LABELS_MAP = {
	[DateGroupings.Dates]: Liferay.Language.get('date'),
	[DateGroupings.Months]: Liferay.Language.get('month'),
	[DateGroupings.Years]: Liferay.Language.get('year')
};

const DATE_OPERATOR_LABELS_MAP = {
	[Operators.Between]: '-',
	[Operators.EQ]: '=',
	[Operators.GT]: Liferay.Language.get('after-fragment'),
	[Operators.LT]: Liferay.Language.get('before-fragment')
};

const DURATION_OPERATOR_LABELS_MAP = {
	[Operators.GT]: '>',
	[Operators.LT]: '<'
};

const NUMBER_OPERATOR_LABELS_MAP = {
	[Operators.Between]: '-',
	[Operators.GT]: '>',
	[Operators.LT]: '<'
};

const STRING_OPERATOR_LABELS_MAP = {
	[Operators.Contains]: Liferay.Language.get('contains-fragment'),
	[Operators.NotContains]: Liferay.Language.get('not-contains-fragment'),
	[Operators.EQ]: Liferay.Language.get('is-fragment'),
	[Operators.NE]: Liferay.Language.get('is-not-fragment')
};

export const getBreakdownDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	filter: Filter
): string[] => {
	let displayFn: (
		attribute: Attribute,
		breakdown: Breakdown,
		filter?: Filter
	) => string[] = getDefaultDisplay;

	if (filter) {
		switch (breakdown.dataType) {
			case DataTypes.Boolean:
				displayFn = getBooleanDisplay;
				break;
			case DataTypes.Date:
				displayFn = getDateDisplay;
				break;
			case DataTypes.Duration:
				displayFn = getDurationDisplay;
				break;
			case DataTypes.Number:
				displayFn = getNumberDisplay;
				break;
			case DataTypes.String:
				displayFn = getStringDisplay;
				break;
			default:
				displayFn = getDefaultDisplay;
				break;
		}
	}

	return displayFn(attribute, breakdown, filter);
};

const getBooleanDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	filter: Filter
): string[] => [
	getDefaultDisplay(attribute, breakdown).join(' | '),
	BOOLEAN_LABEL_MAP[String(filter.value[0])]
];

const getDateDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	{operator, value}: Filter
): string[] => {
	const {dateGrouping} = breakdown;

	const dateGroupingLabel = DATE_GROUPING_LABELS_MAP[dateGrouping];

	const operatorLabel = DATE_OPERATOR_LABELS_MAP[operator];

	const startDate = formatUTCDate(value[0] as string, 'll');

	const breakdownValue =
		operator === Operators.Between
			? `${startDate} ${operatorLabel} ${formatUTCDate(
					value[1] as string,
					'll'
			  )}`
			: `${operatorLabel} ${startDate}`;

	return [
		getDefaultDisplay(attribute, breakdown).join(' | '),
		`${dateGroupingLabel}, ${breakdownValue}`
	];
};

const getDefaultDisplay = (
	{displayName, name}: Attribute,
	{type}: Breakdown
): string[] => [ATTRIBUTE_TYPE_LABEL_MAP[type], displayName || name];

const getDurationDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	{operator, value: [value]}: Filter
): string[] => {
	const duration = formatTime(value as number);

	return [
		getDefaultDisplay(attribute, breakdown).join(' | '),
		`${DURATION_OPERATOR_LABELS_MAP[operator]} ${duration}`
	];
};

const getNumberDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	{operator, value}: Filter
): string[] => {
	const {bin} = breakdown;

	const operatorLabel = NUMBER_OPERATOR_LABELS_MAP[operator];

	const breakdownValue =
		operator === Operators.Between
			? `${value[0]} ${operatorLabel} ${value[1]}`
			: `${operatorLabel} ${value[0]}`;

	return [
		getDefaultDisplay(attribute, breakdown).join(' | '),
		`${bin}, ${breakdownValue}`
	];
};

const getStringDisplay = (
	attribute: Attribute,
	breakdown: Breakdown,
	{operator, value}: Filter
): string[] => [
	getDefaultDisplay(attribute, breakdown).join(' | '),
	`${STRING_OPERATOR_LABELS_MAP[operator]} "${value}"`
];
