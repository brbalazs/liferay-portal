import Cell from '../Cell';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Cell', () => {
	it('should render', () => {
		const {container} = render(<Cell />);
		expect(container).toMatchSnapshot();
	});

	it('should render a children', () => {
		const {getByText} = render(
			<Cell>
				<span>{'Test'}</span>
			</Cell>
		);

		expect(getByText('Test')).toBeTruthy();
	});

	it('should render as a table title', () => {
		const {container} = render(
			<Cell title>
				<span>{'Test'}</span>
			</Cell>
		);

		expect(container.querySelector('.table-title')).toBeTruthy();
	});
});
