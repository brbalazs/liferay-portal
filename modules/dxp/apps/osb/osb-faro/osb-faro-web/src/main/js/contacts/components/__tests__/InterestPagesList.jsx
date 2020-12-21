import InterestPagesList from '../InterestPagesList';
import React from 'react';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('InterestPagesList', () => {
	it('should render an activePages component', async() => {
		const {container} = render(
			<StaticRouter>
				<InterestPagesList dataSourceParams={{active: true}} />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render an InactivePages component', async() => {
		const {container} = render(
			<StaticRouter>
				<InterestPagesList dataSourceParams={{active: false}} />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});
});
