import * as utils from '../utils';
import {
	AttributeOwnerTypes,
	DataTypes,
	DateGroupings,
	Operators
} from '../types';

describe('utils', () => {
	describe('getBreakdownDisplay', () => {
		it.each`
			dataType              | type                              | operator                 | value                           | result                                                    | dateGrouping            | bin
			${DataTypes.Boolean}  | ${AttributeOwnerTypes.Account}    | ${Operators.EQ}          | ${[true]}                       | ${['Account | Test', 'True']}                             | ${null}                 | ${null}
			${DataTypes.Boolean}  | ${AttributeOwnerTypes.Account}    | ${Operators.EQ}          | ${[false]}                      | ${['Account | Test', 'False']}                            | ${null}                 | ${null}
			${DataTypes.Date}     | ${AttributeOwnerTypes.Event}      | ${Operators.Between}     | ${['2021-01-20', '2021-01-24']} | ${['Event | Test', 'Month, Jan 20, 2021 - Jan 24, 2021']} | ${DateGroupings.Months} | ${null}
			${DataTypes.Date}     | ${AttributeOwnerTypes.Event}      | ${Operators.Between}     | ${['2021-01-20', '2021-01-24']} | ${['Event | Test', 'Year, Jan 20, 2021 - Jan 24, 2021']}  | ${DateGroupings.Years}  | ${null}
			${DataTypes.Date}     | ${AttributeOwnerTypes.Event}      | ${Operators.EQ}          | ${['2021-01-20']}               | ${['Event | Test', 'Date, = Jan 20, 2021']}               | ${DateGroupings.Dates}  | ${null}
			${DataTypes.Date}     | ${AttributeOwnerTypes.Event}      | ${Operators.GT}          | ${['2021-01-20']}               | ${['Event | Test', 'Date, after Jan 20, 2021']}           | ${DateGroupings.Dates}  | ${null}
			${DataTypes.Date}     | ${AttributeOwnerTypes.Event}      | ${Operators.LT}          | ${['2021-01-20']}               | ${['Event | Test', 'Date, before Jan 20, 2021']}          | ${DateGroupings.Dates}  | ${null}
			${DataTypes.Duration} | ${AttributeOwnerTypes.Session}    | ${Operators.GT}          | ${[123123]}                     | ${['Session | Test', '00:01:00, > 00:02:03']}             | ${null}                 | ${60000}
			${DataTypes.Duration} | ${AttributeOwnerTypes.Session}    | ${Operators.LT}          | ${[123123123]}                  | ${['Session | Test', '00:01:00, < 34:12:03']}             | ${null}                 | ${60000}
			${DataTypes.Number}   | ${AttributeOwnerTypes.Individual} | ${Operators.Between}     | ${[120, 200]}                   | ${['Individual | Test', '10, 120 - 200']}                 | ${null}                 | ${10}
			${DataTypes.Number}   | ${AttributeOwnerTypes.Individual} | ${Operators.GT}          | ${[120]}                        | ${['Individual | Test', '10, > 120']}                     | ${null}                 | ${10}
			${DataTypes.Number}   | ${AttributeOwnerTypes.Individual} | ${Operators.LT}          | ${[120]}                        | ${['Individual | Test', '10, < 120']}                     | ${null}                 | ${10}
			${DataTypes.String}   | ${AttributeOwnerTypes.Event}      | ${Operators.Contains}    | ${['Hello World']}              | ${['Event | Test', 'contains "Hello World"']}             | ${null}                 | ${null}
			${DataTypes.String}   | ${AttributeOwnerTypes.Event}      | ${Operators.NotContains} | ${['Hello World']}              | ${['Event | Test', 'not contains "Hello World"']}         | ${null}                 | ${null}
			${DataTypes.String}   | ${AttributeOwnerTypes.Event}      | ${Operators.EQ}          | ${['Hello World']}              | ${['Event | Test', 'is "Hello World"']}                   | ${null}                 | ${null}
			${DataTypes.String}   | ${AttributeOwnerTypes.Event}      | ${Operators.NE}          | ${['Hello World']}              | ${['Event | Test', 'is not "Hello World"']}               | ${null}                 | ${null}
		`(
			'returns $result for $dataType, $type, $operator, $value, $dateGrouping, $bin',
			({bin, dataType, dateGrouping, operator, result, type, value}) => {
				expect(
					utils.getBreakdownDisplay(
						{
							displayName: 'Test'
						},
						{bin, dataType, dateGrouping, type},
						{operator, value}
					)
				).toEqual(result);
			}
		);
	});
});
