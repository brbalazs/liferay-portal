import React from 'react';
import UserDropdown from '../index';
import {BrowserRouter} from 'react-router-dom';
import {cleanup, fireEvent, render} from '@testing-library/react';

jest.unmock('react-dom');

const mockMenuItems = () => [
	{
		items: [
			{
				externalLink: true,
				label: 'Link 1',
				url: '/link-1'
			},
			{
				label: 'Link 2',
				url: '/link-externo-2'
			}
		],
		subheaderLabel: 'test@test.com'
	}
];

describe('UserDropdown', () => {
	afterEach(cleanup);
	it('should render', () => {
		const {container} = render(
			<BrowserRouter>
				<UserDropdown
					menuItems={mockMenuItems()}
					userName='Test Test'
				/>
			</BrowserRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render dropdown menu when clicked', () => {
		const {container} = render(
			<BrowserRouter>
				<UserDropdown
					menuItems={mockMenuItems()}
					userName='Test Test'
				/>
			</BrowserRouter>
		);

		const toggleButton = container.querySelector('.user-menu');

		fireEvent.click(toggleButton);

		expect(document.body.querySelector('.dropdown-menu')).toBeTruthy();
		expect(document.body).toMatchSnapshot();
	});
});
