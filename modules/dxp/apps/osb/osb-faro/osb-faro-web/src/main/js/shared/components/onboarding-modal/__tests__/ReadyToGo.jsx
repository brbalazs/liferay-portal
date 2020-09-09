import React from 'react';
import ReadyToGo from '../ReadyToGo';
import {cleanup, fireEvent, render} from '@testing-library/react';
import {noop} from 'lodash';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

describe('ReadyToGo', () => {
	afterEach(cleanup);

	it('renders', () => {
		const {container} = render(
			<StaticRouter>
				<ReadyToGo groupId='123' onClose={noop} />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('calls onClose when "Get Started" is clicked', () => {
		const spy = jest.fn();

		const {queryByText} = render(
			<StaticRouter>
				<ReadyToGo groupId='123' onClose={spy} />
			</StaticRouter>
		);

		expect(spy).not.toBeCalled();

		fireEvent.click(queryByText('Get Started'));

		expect(spy).toBeCalled();
	});
});
