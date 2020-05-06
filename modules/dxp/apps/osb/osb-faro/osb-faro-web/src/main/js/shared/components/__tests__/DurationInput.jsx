import DurationInput from '../DurationInput';
import React from 'react';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('DurationInput', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<DurationInput />);

		expect(container).toMatchSnapshot();
	});

	it('should render default unit as seconds', () => {
		const {queryByDisplayValue} = render(<DurationInput value={6000} />);

		expect(queryByDisplayValue('6')).toBeTruthy();
	});

	it('should render default unit as minutes', () => {
		const {queryByDisplayValue} = render(<DurationInput value={60000} />);

		expect(queryByDisplayValue('1')).toBeTruthy();
	});

	it('should render default unit as hours', () => {
		const {queryByDisplayValue} = render(<DurationInput value={3600000} />);

		expect(queryByDisplayValue('1')).toBeTruthy();
	});
});
