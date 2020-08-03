import * as data from 'test/data';
import Overview from '../Overview';
import React from 'react';
import {Account} from 'shared/util/records';
import {shallow} from 'enzyme';

describe('AccountOverview', () => {
	it('should render', () => {
		const component = shallow(
			<Overview
				account={data.getImmutableMock(Account, data.mockAccount)}
				groupId='23'
				id='test'
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
