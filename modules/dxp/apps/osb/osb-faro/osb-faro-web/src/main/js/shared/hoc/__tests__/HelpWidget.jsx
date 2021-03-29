import HelpWidget from '../HelpWidget';
import React from 'react';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

const wrappedComponentText = () => 'wrapped component text';

describe('HelpWidget', () => {
	afterEach(cleanup);

	it('should render a wrapped component', () => {
		const WrappedComponent = HelpWidget(wrappedComponentText);

		const {container} = render(<WrappedComponent groupId='123' />);

		expect(container.textContent).toBe('wrapped component text');
	});

	it('should render a helper widget', () => {
		const WrappedComponent = HelpWidget(wrappedComponentText);

		const {container} = render(<WrappedComponent groupId='123' />);

		expect(container.querySelector('.helper-widget-wrapper')).toBeTruthy();
	});
});
