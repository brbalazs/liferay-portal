import * as data from 'test/data';
import LiferayDataSourceOld from '../LiferayOld';
import mockStore from 'test/mock-store';
import React from 'react';
import {DataSource} from 'shared/util/records';
import {Provider} from 'react-redux';
import {render, waitForElementToBeRemoved} from '@testing-library/react';
import {Routes, toRoute} from 'shared/util/router';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const defaultProps = {
	dataSource: data.getImmutableMock(DataSource, data.mockLiferayDataSource),
	groupId: 'foo',
	id: 'test'
};

jest.useRealTimers();

describe('LiferayDataSourceOld', () => {
	it('should render', async() => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter
					location={toRoute(
						Routes.SETTINGS_DATA_SOURCE,
						defaultProps
					)}
				>
					<LiferayDataSourceOld {...defaultProps} />
				</StaticRouter>
			</Provider>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});
});
