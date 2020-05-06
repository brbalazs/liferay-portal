import React from 'react';
import ReadyToGo from '../ReadyToGo';
import {cleanup, fireEvent, render} from '@testing-library/react';
import {noop} from 'lodash';

jest.unmock('react-dom');

describe('ReadyToGo', () => {
	afterEach(cleanup);

	it('renders', () => {
		const {container} = render(<ReadyToGo onClose={noop} />);

		expect(container).toMatchSnapshot();
	});

	it('calls onClose when "Get Started" is clicked', () => {
		const spy = jest.fn();

		const {queryByText} = render(<ReadyToGo onClose={spy} />);

		expect(spy).not.toBeCalled();

		fireEvent.click(queryByText('Get Started'));

		expect(spy).toBeCalled();
	});
});
