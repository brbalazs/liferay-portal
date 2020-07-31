import * as data from 'test/data';
import Constants from 'shared/util/constants';
import mockStore from 'test/mock-store';
import React from 'react';
import SalesforceTabRoutes from '../TabRoutes';
import {DataSource, User} from 'shared/util/records';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {Routes, toRoute} from 'shared/util/router';
import {StaticRouter} from 'react-router';

const {
	userRoleNames: {administrator}
} = Constants;

jest.unmock('react-dom');

const defaultProps = {
	currentUser: new User({roleName: administrator}),
	dataSource: data.getImmutableMock(
		DataSource,
		data.mockSalesforceDataSource
	),
	groupId: '23',
	id: 'test'
};

describe('SalesforceTabRoutes', () => {
	it('should render', () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter
					location={toRoute(
						Routes.SETTINGS_DATA_SOURCE,
						defaultProps
					)}
				>
					<SalesforceTabRoutes {...defaultProps} />
				</StaticRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});
});
