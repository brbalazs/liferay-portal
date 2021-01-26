import EventList from '../EventList';
import mockStore from 'test/mock-store';
import React from 'react';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('EventList', () => {
	it('should render', async() => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<EventList groupId='23' />
				</StaticRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});

	it('should list only custom events', async() => {
		const {getByText} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<EventList customEvents groupId='23' />
				</StaticRouter>
			</Provider>
		);

		expect(getByText('seconddisplayCUSTOM')).toMatchSnapshot();
	});
});
