import LiferayTabRoutes from '../TabRoutes';
import React from 'react';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('LiferayTabRoutes', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<LiferayTabRoutes />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});
});
