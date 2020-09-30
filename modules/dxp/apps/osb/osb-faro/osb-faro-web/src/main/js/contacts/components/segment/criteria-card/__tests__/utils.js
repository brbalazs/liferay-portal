import * as data from 'test/data';
import * as utils from '../utils';
import {
	CUSTOM_FUNCTION_OPERATORS,
	isKnown,
	isUnknown,
	RELATIONAL_OPERATORS
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
	SESSION_DATE_TIME,
	SESSION_GEOLOCATION,
	SESSION_NUMBER,
	SESSION_TEXT,
	TEXT
} = PROPERTY_TYPES;

const {ACTIVITIES_FILTER_BY_COUNT} = CUSTOM_FUNCTION_OPERATORS;

const {EQ, LT, NE} = RELATIONAL_OPERATORS;

describe('utils', () => {
	describe('getOperatorLabel', () => {
		it.each`
			operatorKey                   | type                   | retVal
			${EQ}                         | ${ACCOUNT_TEXT}        | ${Liferay.Language.get('is-fragment')}
			${ACTIVITIES_FILTER_BY_COUNT} | ${BEHAVIOR}            | ${Liferay.Language.get('has-fragment')}
			${EQ}                         | ${SESSION_GEOLOCATION} | ${Liferay.Language.get('was-fragment')}
			${EQ}                         | ${SESSION_TEXT}        | ${Liferay.Language.get('is-fragment')}
			${EQ}                         | ${TEXT}                | ${Liferay.Language.get('is-fragment')}
			${EQ}                         | ${BOOLEAN}             | ${Liferay.Language.get('is-fragment')}
			${LT}                         | ${DATE}                | ${Liferay.Language.get('is-before-fragment')}
			${LT}                         | ${DATE_TIME}           | ${Liferay.Language.get('is-before-fragment')}
			${LT}                         | ${SESSION_DATE_TIME}   | ${Liferay.Language.get('is-before-fragment')}
			${LT}                         | ${ACCOUNT_NUMBER}      | ${Liferay.Language.get('less-than-fragment')}
			${LT}                         | ${DURATION}            | ${Liferay.Language.get('less-than-fragment')}
			${LT}                         | ${NUMBER}              | ${Liferay.Language.get('less-than-fragment')}
			${LT}                         | ${SESSION_NUMBER}      | ${Liferay.Language.get('less-than-fragment')}
		`(
			'get $retVal for $type from $operatorKey',
			({operatorKey, retVal, type}) => {
				expect(utils.getOperatorLabel(operatorKey, type)).toBe(retVal);
			}
		);
	});
	describe('maybeFormatToKnownType', () => {
		it.each`
			operatorName | value   | retVal
			${NE}        | ${null} | ${isKnown}
			${EQ}        | ${null} | ${isUnknown}
			${LT}        | ${123}  | ${LT}
		`('formats $value to $retVal', ({operatorName, retVal, value}) => {
			expect(utils.maybeFormatToKnownType(operatorName, value)).toBe(
				retVal
			);
		});
	});

	describe('maybeFormatValue', () => {
		it.each`
			value                  | type                   | retVal
			${'Test'}              | ${ACCOUNT_TEXT}        | ${'"Test"'}
			${'Test'}              | ${BEHAVIOR}            | ${'"Test"'}
			${'Test'}              | ${INTEREST}            | ${'"Test"'}
			${'Test'}              | ${SESSION_GEOLOCATION} | ${'"Test"'}
			${'Test'}              | ${SESSION_TEXT}        | ${'"Test"'}
			${'Test'}              | ${TEXT}                | ${'"Test"'}
			${'true'}              | ${BOOLEAN}             | ${'TRUE'}
			${data.getTimestamp()} | ${DATE}                | ${'2018-07-10'}
			${data.getTimestamp()} | ${DATE_TIME}           | ${'2018-07-10 23:01'}
			${data.getTimestamp()} | ${SESSION_DATE_TIME}   | ${'2018-07-10 23:01'}
			${123}                 | ${ACCOUNT_NUMBER}      | ${123}
			${123}                 | ${DURATION}            | ${123}
			${123}                 | ${NUMBER}              | ${123}
			${123}                 | ${SESSION_NUMBER}      | ${123}
		`('formats $value to $retVal if $type', ({retVal, type, value}) => {
			expect(utils.maybeFormatValue(value, type)).toBe(retVal);
		});
	});
});
