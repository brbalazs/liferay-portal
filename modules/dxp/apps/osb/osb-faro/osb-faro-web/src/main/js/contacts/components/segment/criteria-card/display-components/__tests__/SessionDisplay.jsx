import * as data from 'test/data';
import React from 'react';
import SessionDisplay from '../SessionDisplay';
import {cleanup, render} from '@testing-library/react';
import {
	CUSTOM_FUNCTION_OPERATORS,
	LAST_7_DAYS,
	PROPERTY_TYPES,
	RELATIONAL_OPERATORS
} from 'contacts/components/segment-editor/dynamic/utils/constants';
import {List, Map} from 'immutable';
import {Property} from 'shared/util/records';

jest.unmock('react-dom');

describe('SessionDisplay', () => {
	const mockCriterion = {
		operatorName: CUSTOM_FUNCTION_OPERATORS.SESSIONS_FILTER,
		propertyName: 'context/browserName',
		value: Map({
			criterionGroup: Map({
				items: List([
					Map({
						operatorName: RELATIONAL_OPERATORS.EQ,
						propertyName: 'context/browserName',
						value: 'Chrome'
					}),
					Map({
						operatorName: RELATIONAL_OPERATORS.GT,
						propertyName: 'completeDate',
						value: LAST_7_DAYS
					})
				])
			})
		})
	};

	afterEach(cleanup);

	const mockProperty = data.getImmutableMock(Property, data.mockProperty, 1, {
		entityName: 'Individual',
		label: 'name',
		name: 'name',
		propertykey: 'session',
		type: PROPERTY_TYPES.SESSION_TEXT
	});

	it('renders', () => {
		const {container} = render(
			<SessionDisplay criterion={mockCriterion} property={mockProperty} />
		);

		expect(container).toMatchSnapshot();
	});

	it('renders w/ a knownType', () => {
		const criterion = {...mockCriterion};

		criterion.value = criterion.value.setIn(
			['criterionGroup', 'items', 0, 'value'],
			null
		);

		const {getByText} = render(
			<SessionDisplay criterion={criterion} property={mockProperty} />
		);

		expect(getByText('is unknown')).toBeTruthy();
	});
});
