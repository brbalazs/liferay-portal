import Form from '../index';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Form', () => {
	
	it('should render a Form', () => {
		const {container} = render(<Form />);

		expect(container).toMatchSnapshot();
	});

	it('should render a Form Group', () => {
		const {container} = render(<Form.Group />);

		expect(container).toMatchSnapshot();
	});

	it('should render an inline Form Group', () => {
		const {container} = render(<Form.Group inline />);

		expect(container).toMatchSnapshot();
	});

	it('should render an autofit Form Group', () => {
		const {container} = render(<Form.Group autoFit />);

		expect(container).toMatchSnapshot();
	});
});
