import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import mockStore from 'test/mock-store';
import React from 'react';
import {DataSource, User} from 'shared/util/records';
import {LiferayAuthorization} from '../Authorization';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const {
	userRoleNames: {member}
} = FaroConstants;

const defaultProps = {
	currentUser: data.getImmutableMock(User, data.mockUser),
	dataSource: data.getImmutableMock(DataSource, data.mockLiferayDataSource),
	groupId: '23',
	id: 'test'
};

describe('LiferayAuthorization', () => {
	it('should render', () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<LiferayAuthorization {...defaultProps} />
				</StaticRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render as read-only if the user is not authorized to make changes', () => {
		const {queryByText} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<LiferayAuthorization
						{...defaultProps}
						currentUser={data.getImmutableMock(
							User,
							data.mockUser,
							'23',
							{
								roleName: member
							}
						)}
					/>
				</StaticRouter>
			</Provider>
		);

		expect(queryByText('Delete Data Source')).toBeFalsy();
	});
});
