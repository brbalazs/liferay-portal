import * as data from 'test/data';
import LiferayDataSourceOld from '../LiferayOld';
import React from 'react';
import {DataSource, User} from 'shared/util/records';
import {shallow} from 'enzyme';

const defaultProps = {
	currentUser: data.getImmutableMock(User, data.mockUser),
	dataSource: data.getImmutableMock(DataSource, data.mockLiferayDataSource),
	groupId: '23',
	id: 'test'
};

describe('LiferayDataSourceOld', () => {
	it('should render', () => {
		const component = shallow(<LiferayDataSourceOld {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});
});
