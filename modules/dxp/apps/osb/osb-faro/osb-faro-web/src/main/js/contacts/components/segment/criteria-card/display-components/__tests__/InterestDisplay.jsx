import * as data from 'test/data';
import InterestDisplay from '../InterestDisplay';
import React from 'react';
import {
	CUSTOM_FUNCTION_OPERATORS,
	PROPERTY_TYPES,
	RELATIONAL_OPERATORS
} from 'contacts/components/segment-editor/dynamic/utils/constants';
import {List, Map} from 'immutable';
import {Property} from 'shared/util/records';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('InterestDisplay', () => {
	const mockCriterion = {
		operatorName: CUSTOM_FUNCTION_OPERATORS.INTERESTS_FILTER,
		propertyName: 'name',
		value: Map({
			criterionGroup: Map({
				items: List([
					Map({
						operatorName: RELATIONAL_OPERATORS.EQ,
						propertyName: 'name',
						value: 'Tests'
					}),
					Map({
						operatorName: RELATIONAL_OPERATORS.EQ,
						propertyName: 'score',
						value: 'true'
					})
				])
			})
		})
	};

	const mockProperty = data.getImmutableMock(Property, data.mockProperty, 1, {
		entityName: 'Individual',
		label: 'name',
		name: 'name',
		propertykey: 'interest',
		type: PROPERTY_TYPES.INTEREST
	});

	it('renders', () => {
		const {container} = render(
			<InterestDisplay
				criterion={mockCriterion}
				property={mockProperty}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
