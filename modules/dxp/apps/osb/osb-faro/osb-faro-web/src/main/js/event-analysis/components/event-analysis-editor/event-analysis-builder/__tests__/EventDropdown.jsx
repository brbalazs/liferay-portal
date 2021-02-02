import EventDropdown from '../EventDropdown';
import React from 'react';
import {fireEvent, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('EventDropdown', () => {
	it('render', () => {
		const {container, getByTestId} = render(
			<EventDropdown
				trigger={<button data-testid='target'>{'click me'}</button>}
			/>
		);

		fireEvent.click(getByTestId('target'));

		jest.runAllTimers();

		expect(container).toMatchSnapshot();

		const dropdownMenu = document.body.getElementsByClassName(
			'event-analysis-dropdown-menu-root'
		)[0];

		expect(dropdownMenu).toMatchSnapshot();

		expect(
			dropdownMenu.getElementsByClassName('dropdown-item active')
		).toBeEmpty();
	});

	it('render with selected event', () => {
		const {getByTestId} = render(
			<EventDropdown
				eventId='3'
				trigger={<button data-testid='target'>{'click me'}</button>}
			/>
		);

		fireEvent.click(getByTestId('target'));

		jest.runAllTimers();

		expect(
			document.body.getElementsByClassName('dropdown-item active').length
		).toBe(1);
	});
});
