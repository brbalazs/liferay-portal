import AccountInput from '../AccountInput';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {fromJS} from 'immutable';
import {Property} from 'shared/util/records';
import {PROPERTY_TYPES, RELATIONAL_OPERATORS} from '../../utils/constants';

jest.unmock('react-dom');

const {ACCOUNT_NUMBER} = PROPERTY_TYPES;
const {EQ} = RELATIONAL_OPERATORS;

describe('AccountInput', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<AccountInput
				property={new Property()}
				value={fromJS({criterionGroup: {items: [{operatorName: EQ}]}})}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render a CustomNumberInput', () => {
		const {queryByText} = render(
			<AccountInput
				property={new Property({type: ACCOUNT_NUMBER})}
				value={fromJS({criterionGroup: {items: [{operatorName: EQ}]}})}
			/>
		);

		expect(queryByText('is equal to')).not.toBeNull();
	});
});
