import CustomDateTimeInput from '../CustomDateTimeInput';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {createCustomValueMap} from '../../utils/custom-inputs';
import {Property} from 'shared/util/records';
import {RELATIONAL_OPERATORS} from '../../utils/constants';

jest.unmock('react-dom');

const mockValue = createCustomValueMap([
	{
		key: 'criterionGroup',
		value: [
			{
				operatorName: RELATIONAL_OPERATORS.GT,
				propertyName: 'completeDate',
				value: '2020-01-17T:00:00-08:00'
			}
		]
	}
]);

describe('CustomDateTimeInput', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<CustomDateTimeInput property={new Property()} value={mockValue} />
		);

		expect(container).toMatchSnapshot();
	});
});
