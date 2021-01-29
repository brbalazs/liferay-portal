import AnalysisDropdown from '../AnalysisDropdown';
import React from 'react';
import {fireEvent, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('AnalysisDropdown', () => {
	it('render', () => {
		const {container, getByTestId} = render(
			<AnalysisDropdown
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
			<AnalysisDropdown
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
