import CreateEventAnalysis from '../CreateEventAnalysis';
import mockStore from 'test/mock-store';
import React from 'react';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

describe('Create Event Analysis', () => {
	it('render', () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<CreateEventAnalysis
						router={{
							params: {channelId: '456', groupId: '123'}
						}}
					/>
				</StaticRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});
});
