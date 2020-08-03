import * as data from 'test/data';
import React from 'react';
import {List} from '../List';
import {shallow} from 'enzyme';
import {User} from 'shared/util/records';

describe('List', () => {
	it('should render', () => {
		const component = shallow(
			<List
				currentUser={data.getImmutableMock(User, data.mockUser)}
				groupId='23'
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
