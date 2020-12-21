import InterestPagesList from '../InterestPagesList';
import React from 'react';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('InterestPagesList', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<InterestPagesList dataSourceParams={{}} />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render an activePages component', () => {
		const {container} = render(
			<StaticRouter>
				<InterestPagesList dataSourceParams={{active: true}} />
			</StaticRouter>
		);

		expect(
			container.querySelector('.lexicon-icon-order-arrow-down')
		).toBeTruthy();
	});

	it('should render an InactivePages component', () => {
		const {container} = render(
			<StaticRouter>
				<InterestPagesList dataSourceParams={{active: false}} />
			</StaticRouter>
		);

		expect(
			container.querySelector('.lexicon-icon-order-arrow-down')
		).toBeFalsy();
	});
});
