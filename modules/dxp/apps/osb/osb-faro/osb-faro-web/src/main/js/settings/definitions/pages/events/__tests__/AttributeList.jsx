import AttributeList from '../AttributeList';
import mockStore from 'test/mock-store';
import React from 'react';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('AttributeList', () => {
	it('should render', async() => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<AttributeList groupId='23' />
				</StaticRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});
});
