import AttributeDropdown from '../index';
import React from 'react';
import {fireEvent, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('AttributeDropdown', () => {
	it('render', () => {
		const {container, getByTestId} = render(
			<AttributeDropdown
				onAttributeSelect={jest.fn()}
				trigger={<button data-testid='target'>{'click me'}</button>}
			/>
		);

		fireEvent.click(getByTestId('target'));

		jest.runAllTimers();

		expect(container).toMatchSnapshot();

		const dropdownMenu = document.body.getElementsByClassName(
			'base-dropdown-menu-root'
		)[0];

		expect(dropdownMenu).toMatchSnapshot();

		expect(
			dropdownMenu.getElementsByClassName('dropdown-item active')
		).toBeEmpty();
	});

	it('render w/ selected attribute', () => {
		const {getByTestId} = render(
			<AttributeDropdown
				attribute={{
					dataType: 'string',
					displayName: 'Filed Ticket',
					id: '4',
					name: 'filedTicket'
				}}
				onAttributeSelect={jest.fn()}
				trigger={<button data-testid='target'>{'click me'}</button>}
			/>
		);

		fireEvent.click(getByTestId('target'));

		jest.runAllTimers();

		expect(
			document.body.getElementsByClassName('dropdown-item active').length
		).toBe(1);
	});

	it('render w/ disabled attributes', () => {
		const {getByTestId} = render(
			<AttributeDropdown
				disabledIds={['1', '2']}
				onAttributeSelect={jest.fn()}
				trigger={<button data-testid='target'>{'click me'}</button>}
			/>
		);

		fireEvent.click(getByTestId('target'));

		jest.runAllTimers();

		expect(
			document.body.getElementsByClassName('dropdown-item disabled')
				.length
		).toBe(2);
	});
});
