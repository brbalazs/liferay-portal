import React from 'react';
import TimeSelector from '../TimeSelector';
import {cleanup, fireEvent, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('TimeSelector', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<TimeSelector />);

		expect(container).toMatchSnapshot();
	});

	it('should change the input', () => {
		const {container} = render(
			<TimeSelector onChange={jest.fn()} value='test' />
		);
		fireEvent.change(container.querySelector('.input-root'));

		expect(container).toMatchSnapshot();
	});
});
