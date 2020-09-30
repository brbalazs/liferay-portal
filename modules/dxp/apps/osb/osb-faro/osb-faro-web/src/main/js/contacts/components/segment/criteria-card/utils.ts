import moment from 'moment';
import {formatDateToTimeZone} from 'shared/util/date';
import {
	GEOLOCATION_OPTIONS,
	INPUT_DATE_FORMAT,
	INPUT_DISPLAY_DATE_TIME_FORMAT,
	isKnown,
	isUnknown,
	RELATIONAL_OPERATORS,
	SUPPORTED_OPERATORS
} from 'contacts/components/segment-editor/dynamic/utils/constants';
import {PROPERTY_TYPES} from 'contacts/components/segment-editor/dynamic/utils/constants';

const {
	ACCOUNT_NUMBER,
	ACCOUNT_TEXT,
	BEHAVIOR,
	BOOLEAN,
	DATE,
	DATE_TIME,
	DURATION,
	INTEREST,
	NUMBER,
	ORGANIZATION_BOOLEAN,
	ORGANIZATION_DATE,
	ORGANIZATION_DATE_TIME,
	ORGANIZATION_NUMBER,
	ORGANIZATION_SELECT_TEXT,
	ORGANIZATION_TEXT,
	SESSION_DATE_TIME,
	SESSION_GEOLOCATION,
	SESSION_NUMBER,
	SESSION_TEXT,
	TEXT
} = PROPERTY_TYPES;

export function getOperatorLabel(operatorKey: string, type: string): string {
	let supportedOperators;

	switch (type) {
		case ACCOUNT_NUMBER:
		case ACCOUNT_TEXT:
			supportedOperators =
				SUPPORTED_OPERATORS[type.replace('account-', '')];
			break;
		case ORGANIZATION_BOOLEAN:
		case ORGANIZATION_DATE:
		case ORGANIZATION_DATE_TIME:
		case ORGANIZATION_NUMBER:
		case ORGANIZATION_TEXT:
			supportedOperators =
				SUPPORTED_OPERATORS[type.replace('organization-', '')];
			break;
		case SESSION_DATE_TIME:
		case SESSION_NUMBER:
		case SESSION_TEXT:
			supportedOperators =
				SUPPORTED_OPERATORS[type.replace('session-', '')];
			break;
		case SESSION_GEOLOCATION:
			supportedOperators = GEOLOCATION_OPTIONS;
			break;
		case BEHAVIOR:
		case BOOLEAN:
		case DATE:
		case DATE_TIME:
		case DURATION:
		case NUMBER:
		case ORGANIZATION_SELECT_TEXT:
		case TEXT:
		default:
			supportedOperators = SUPPORTED_OPERATORS[type];
	}

	const operator = supportedOperators.find(
		({key, value}) => (key || value) === operatorKey
	);

	return operator ? operator.label : null;
}

export function maybeFormatToKnownType(
	operatorName: string,
	value: any
): string {
	const valueNull = value === null;

	if (operatorName === RELATIONAL_OPERATORS.EQ && valueNull) {
		return isUnknown;
	} else if (operatorName === RELATIONAL_OPERATORS.NE && valueNull) {
		return isKnown;
	}

	return operatorName;
}

export function maybeFormatValue(
	value: any,
	type: string,
	timeZoneId?: string
): string | number {
	switch (type) {
		case ACCOUNT_TEXT:
		case BEHAVIOR:
		case INTEREST:
		case SESSION_GEOLOCATION:
		case SESSION_TEXT:
		case TEXT:
			return `"${value}"`;
		case BOOLEAN:
			return value.toUpperCase();
		case DATE:
			return moment(value).format(INPUT_DATE_FORMAT);
		case DATE_TIME:
		case SESSION_DATE_TIME:
			return formatDateToTimeZone(
				value,
				INPUT_DISPLAY_DATE_TIME_FORMAT,
				timeZoneId
			);
		case ACCOUNT_NUMBER:
		case DURATION:
		case NUMBER:
		case SESSION_NUMBER:
		default:
			return value;
	}
}
