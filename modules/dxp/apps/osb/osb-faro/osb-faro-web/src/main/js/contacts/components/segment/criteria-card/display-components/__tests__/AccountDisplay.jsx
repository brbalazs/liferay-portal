import * as data from 'test/data';
import AccountDisplay from '../AccountDisplay';
import React from 'react';
import {
	CUSTOM_FUNCTION_OPERATORS,
	PROPERTY_TYPES,
	RELATIONAL_OPERATORS
} from 'contacts/components/segment-editor/dynamic/utils/constants';
import {List, Map} from 'immutable';
import {Property} from 'shared/util/records';
import {shallow} from 'enzyme';

describe('AccountDisplay', () => {
	const propertyName = 'organization/description/value';

	const mockCriterion = {
		operatorName: CUSTOM_FUNCTION_OPERATORS.ACCOUNTS_FILTER,
		propertyName,
		value: Map({
			criterionGroup: Map({
				items: List([
					Map({
						operatorName: RELATIONAL_OPERATORS.EQ,
						propertyName: 'organization/description/value',
						value: 'this is a description'
					})
				])
			})
		})
	};

	const mockProperty = data.getImmutableMock(Property, data.mockProperty, 1, {
		entityName: 'Account',
		label: 'description',
		name: propertyName,
		propertykey: 'account',
		type: PROPERTY_TYPES.ACCOUNT_TEXT
	});

	it('renders', () => {
		const component = shallow(
			<AccountDisplay criterion={mockCriterion} property={mockProperty} />
		);

		expect(component).toMatchSnapshot();
	});

	it('renders w/ a knownType', () => {
		const criterion = {...mockCriterion};

		criterion.value = criterion.value.setIn(
			['criterionGroup', 'items', 0, 'value'],
			null
		);

		const component = shallow(
			<AccountDisplay criterion={criterion} property={mockProperty} />
		);

		expect(component).toMatchSnapshot();
	});
});
