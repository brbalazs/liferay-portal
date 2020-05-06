import * as data from 'test/data';
import KnownIndividuals from '../KnownIndividuals';
import React from 'react';
import {Account} from 'shared/util/records';
import {shallow} from 'enzyme';

describe('KnownIndividuals', () => {
	it('should render', () => {
		const component = shallow(
			<KnownIndividuals
				account={data.getImmutableMock(Account, data.mockAccount)}
				channelId='123123'
				groupId={'23'}
				id={'test'}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
