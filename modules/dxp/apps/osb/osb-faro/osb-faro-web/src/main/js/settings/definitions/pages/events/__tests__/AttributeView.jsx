import AttributeView from '../AttributeView';
import mockStore from 'test/mock-store';
import React from 'react';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

jest.mock('react-router-dom', () => ({
	...jest.requireActual('react-router-dom'),
	useParams: () => ({
		attributeId: 12345
	})
}));

describe('AttributeView', () => {
	it('should render', async() => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<AttributeView groupId='23' />
				</StaticRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});
});
