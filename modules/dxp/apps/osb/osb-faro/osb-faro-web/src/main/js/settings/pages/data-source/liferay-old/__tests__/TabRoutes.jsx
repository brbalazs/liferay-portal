import * as data from 'test/data';
import LiferayTabRoutes from '../TabRoutes';
import mockStore from 'test/mock-store';
import React from 'react';
import {DataSource} from 'shared/util/records';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {Routes, toRoute} from 'shared/util/router';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const defaultProps = {
	dataSource: data.getImmutableMock(DataSource, data.mockLiferayDataSource),
	groupId: 'foo',
	id: 'test'
};

describe('LiferayTabRoutes', () => {
	it('should render', () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter
					location={toRoute(
						Routes.SETTINGS_DATA_SOURCE,
						defaultProps
					)}
				>
					<LiferayTabRoutes {...defaultProps} />
				</StaticRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});
});
