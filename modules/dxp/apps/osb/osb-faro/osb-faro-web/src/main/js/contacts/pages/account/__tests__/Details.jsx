import * as data from 'test/data';
import Details from '../Details';
import React from 'react';
import {Account} from 'shared/util/records';
import {shallow} from 'enzyme';

describe('AccountDetails', () => {
	it('should render', () => {
		const component = shallow(
			<Details
				account={data.getImmutableMock(Account, data.mockAccount)}
				groupId={'23'}
				id={'test'}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
