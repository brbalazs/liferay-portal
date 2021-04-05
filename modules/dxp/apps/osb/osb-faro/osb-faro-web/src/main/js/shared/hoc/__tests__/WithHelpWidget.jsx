import React from 'react';
import withHelpWidget from '../WithHelpWidget';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

const wrappedComponentText = () => 'wrapped component text';

describe('withHelpWidget', () => {
	it('should render a wrapped component', () => {
		const WrappedComponent = withHelpWidget(wrappedComponentText);

		const {container} = render(<WrappedComponent groupId='123' />);

		expect(container.textContent).toBe('wrapped component text');
	});

	it('should render a HelpWidget Component', () => {
		const WrappedComponent = withHelpWidget(wrappedComponentText);

		const {container} = render(<WrappedComponent groupId='123' />);

		expect(container.querySelector('.help-widget-root')).toBeTruthy();
	});
});
