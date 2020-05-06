import * as data from 'test/data';
import AssociatedSegments from '../AssociatedSegments';
import React from 'react';
import {Account} from 'shared/util/records';
import {shallow} from 'enzyme';

describe('AccountAssociatedSegments', () => {
	it('should render', () => {
		const component = shallow(
			<AssociatedSegments
				account={data.getImmutableMock(Account, data.mockAccount)}
				groupId={'23'}
				id={'test'}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
