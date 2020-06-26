import React from 'react';
import SalesforceTabRoutes from '../TabRoutes';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('SalesforceTabRoutes', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<SalesforceTabRoutes />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});
});
