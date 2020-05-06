import DateFilterConjunctionDisplay from '../DateFilterConjunctionDisplay';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {
	FUNCTIONAL_OPERATORS,
	LAST_90_DAYS,
	RELATIONAL_OPERATORS
} from 'contacts/components/segment-editor/dynamic/utils/constants';

jest.unmock('react-dom');

describe('DateFilterConjunctionDisplay', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<DateFilterConjunctionDisplay
				conjunctionCriterion={{
					operatorName: RELATIONAL_OPERATORS.EQ,
					propertyName: 'date',
					touched: false,
					valid: false,
					value: '2020-12-12'
				}}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render w/ between', () => {
		const {getByText} = render(
			<DateFilterConjunctionDisplay
				conjunctionCriterion={{
					operatorName: FUNCTIONAL_OPERATORS.BETWEEN,
					propertyName: 'date',
					touched: false,
					valid: false,
					value: {end: '2020-12-12', start: '2020-12-01'}
				}}
			/>
		);

		expect(getByText('between')).toBeTruthy();
	});

	it('should render w/ ever', () => {
		const {getByText} = render(
			<DateFilterConjunctionDisplay
				conjunctionCriterion={{
					propertyName: 'date'
				}}
			/>
		);

		expect(getByText('ever')).toBeTruthy();
	});

	it('should render w/ since', () => {
		const {getByText} = render(
			<DateFilterConjunctionDisplay
				conjunctionCriterion={{
					operatorName: RELATIONAL_OPERATORS.GT,
					propertyName: 'date',
					touched: false,
					valid: false,
					value: LAST_90_DAYS
				}}
			/>
		);

		expect(getByText('since')).toBeTruthy();
	});
});
