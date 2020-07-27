import Nav from '../Nav';
import React from 'react';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const MOCK_ITEMS = [
	<Nav.Item active href='#' key={1}>
		{'foo'}
	</Nav.Item>,
	<Nav.Item key={2}>{'bar'}</Nav.Item>,
	<Nav.Item key={3}>{'baz'}</Nav.Item>
];

describe('Nav', () => {
	it('should render', () => {
		const {container} = render(<Nav />);

		expect(container).toMatchSnapshot();
	});

	it('should render vertically', () => {
		const {container} = render(<Nav vertical />);

		expect(container.querySelector('.flex-column')).toBeTruthy();
	});

	it('should render with items', () => {
		const {getByText} = render(
			<StaticRouter>
				<Nav children={MOCK_ITEMS} />
			</StaticRouter>
		);

		MOCK_ITEMS.forEach(({props: {children}}) => {
			expect(getByText(children)).toBeTruthy();
		});
	});

	it('should render with underline class', () => {
		const {container} = render(
			<StaticRouter>
				<Nav display='underline' />
			</StaticRouter>
		);

		expect(container.querySelector('.nav-underline')).toBeTruthy();
	});
});

describe('Nav.Item', () => {
	it('should render', () => {
		const {container} = render(<Nav.Item children='Child' />);

		expect(container).toMatchSnapshot();
	});

	it('should render as monospaced', () => {
		const {container} = render(
			<StaticRouter>
				<Nav.Item
					children='Child'
					href='www.liferay.com'
					linkMonospaced
				/>
			</StaticRouter>
		);

		expect(container.querySelector('.nav-link-monospaced')).toBeTruthy();
	});
});
