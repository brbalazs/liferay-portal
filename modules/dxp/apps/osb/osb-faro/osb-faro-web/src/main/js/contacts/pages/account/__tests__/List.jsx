import * as data from 'test/data';
import mockStore from 'test/mock-store';
import React from 'react';
import {List} from '../List';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';
import {User} from 'shared/util/records';

jest.unmock('react-dom');

describe('List', () => {
	it('should render', () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<List
						currentUser={data.getImmutableMock(User, data.mockUser)}
						groupId='23'
					/>
				</StaticRouter>
			</Provider>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
