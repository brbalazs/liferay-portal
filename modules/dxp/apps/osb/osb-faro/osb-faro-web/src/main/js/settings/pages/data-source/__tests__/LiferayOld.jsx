import * as data from 'test/data';
import LiferayDataSourceOld from '../LiferayOld';
import React from 'react';
import {DataSource, User} from 'shared/util/records';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const defaultProps = {
	currentUser: data.getImmutableMock(User, data.mockUser),
	dataSource: data.getImmutableMock(DataSource, data.mockLiferayDataSource),
	groupId: '23',
	id: 'test'
};

describe('LiferayDataSourceOld', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<LiferayDataSourceOld {...defaultProps} />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});
});
