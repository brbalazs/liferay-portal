import React from 'react';
import TabRoutes from '../TabRoutes';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('TabRoutes', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<TabRoutes
					routes={[{component: jest.fn(), path: 'foo/path'}]}
				/>
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});
});
