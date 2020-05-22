import {Align} from 'metal-position';
import {
	ALIGNMENTS_MAP,
	LAST_30_DAYS,
	LAST_90_DAYS,
	POSITIONS
} from 'shared/util/constants';
import {flow, get, isFinite, isNil, isString, toLower, trim} from 'lodash';

export type RangeSelectors = {
	rangeEnd: string;
	rangeKey: string;
	rangeStart: string;
};

/**
 * Check if the value is blank.
 * @param {string|number} value
 * @returns {boolean}
 */
export const isBlank = (value: string | number): boolean =>
	isNil(value) || (isString(value) && !value.length);

export const getRangeSelectors = (
	{rangeEnd, rangeKey, rangeStart}: RangeSelectors,
	query: RangeSelectors
) => ({
	rangeEnd: get(query, 'rangeEnd', rangeEnd),
	rangeKey: get(query, 'rangeKey', rangeKey),
	rangeStart: get(query, 'rangeStart', rangeStart)
});

/**
 * Check if the value is blank and returns value.
 * @param {string|number} value
 * @param {string|number} defaultValue
 * @returns {string|number} Returns defaultValue if value is blank.
 */
export const getSafeDisplayValue = (
	value: string | number,
	defaultValue: string | number = '-'
): string | number => (isBlank(value) ? defaultValue : value);

/**
 * Create a Blob object from data string and temporarily attach
 * an anchor element to the DOM to click on and trigger download.
 */
export const downloadDataAsFile = ({
	data,
	name,
	type
}: {
	data: string;
	name: string;
	type: string;
}) => {
	const blob = new Blob([data], {type});

	const linkUrl = URL.createObjectURL(blob);
	const link = document.createElement('a');
	link.href = linkUrl;
	link.setAttribute('download', name);

	document.body.appendChild(link);

	link.click();

	link.parentNode.removeChild(link);
	URL.revokeObjectURL(linkUrl);
};

/**
 * Remove Protocol
 * @param {string} url
 */
export const removeProtocol = url =>
	decodeURIComponent(url).replace(/^http(s)?:\/\//i, '');

/**
 * Remove numbers using regex
 * @param {string} str
 */
export const removeNumbers = str => str.replace(/\d+/g, ' ');

/**
 * Remove spacing using regex
 * @param {string} str
 */
export const removeSpacing = str => str.replace(/\s+/g, '');

/**
 * Returns the percent number passing as
 * parameter the current number and total number.
 * @param {number} number1
 * @param {number} number2
 * @returns {number}
 */
export const getPercentage = (number1, number2) => {
	const result = (number1 / number2) * 100;

	return isFinite(result) ? result : 0;
};

/**
 * Return an array with the parsed data grouped with the max informed value
 * @param {array} data
 * @param {number} max
 */
export const groupData = (data, max) => {
	const others = Object.assign(data, []).filter((current, index) =>
		index > max - 1 ? current : null
	);
	const agroupedData = [];
	let agroupedDataValue;

	if (data.length <= max) {
		return data;
	}

	for (let i = 0; i <= max - 1; i++) {
		agroupedData.push(data[i]);
	}

	if (others.length > 1) {
		agroupedDataValue = others.reduce((actual, next) => {
			if (actual.data) {
				return actual.data[0] + next.data[0];
			} else {
				return actual + next.data[0];
			}
		});
	} else {
		agroupedDataValue = others[0].data[0];
	}

	agroupedData.push({
		data: [agroupedDataValue],
		group: others,
		id: Liferay.Language.get('others')
	});

	return agroupedData;
};

/**
 * Return the truncate text
 * @param {string} str
 * @param {number} length
 * @param {number} ending
 */
export const truncateText = (str, length, ending) => {
	if (length == null) length = 100;
	if (ending == null) ending = '...';

	return str.length > length
		? str.substring(0, length - ending.length) + ending
		: str;
};

/**
 * Is Ellipsis Active
 * @param {object} event
 */
export const isEllipisActive = ({target}) =>
	target.offsetWidth < target.scrollWidth;

/**
 * Get Align Position
 * @param {string} source
 * @param {string} target
 * @param {string} suggestedPosition
 */
export const getAlignPosition = (source, target, suggestedPosition) => {
	if (!suggestedPosition) {
		suggestedPosition = 'top';
	}

	const position = Align.align(
		source,
		target,
		ALIGNMENTS_MAP[suggestedPosition]
	);

	return POSITIONS[position];
};

/**
 * Get Range Key
 * @param {string} timeRange
 */
export const getRangeKeyFromTimeRange = timeRange => {
	if (!timeRange || timeRange.length === 0) {
		return {
			defaultValue: LAST_30_DAYS,
			lastValue: LAST_90_DAYS
		};
	}

	const timeRangeDefault = timeRange.filter(range => range.default);
	const {
		[0]: {rangeKey: defaultValue}
	} = timeRangeDefault;

	const timeRangeSorted = timeRange
		.map(timeRange => timeRange)
		.sort((a, b) => a.rangeKey - b.rangeKey);
	const {
		[timeRange.length - 1]: {rangeKey: lastValue = ''}
	} = timeRangeSorted;

	return {
		defaultValue: `${defaultValue}`,
		lastValue: `${lastValue}`
	};
};

/**
 * Get the RangeKey from the context object.
 * @param {object} context
 * @param {object} context.rangeKey
 * @param {object} context.router
 * @returns {string} rangeKey
 */
export const getRangeKeyFromContext = ({
	rangeKey: {defaultValue},
	router: {query}
}): string => {
	const {rangeKey = defaultValue} = query;

	return rangeKey;
};

/**
 * Trim and convert value to lowercase.
 * @param {string} value
 * @return {string} Lowercase & trimmed string.
 */
export const formatStringToLowercase: (value: string) => string = flow(
	toLower,
	trim
);
