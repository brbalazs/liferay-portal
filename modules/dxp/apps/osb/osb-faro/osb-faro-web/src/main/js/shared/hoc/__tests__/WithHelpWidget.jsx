import React from 'react';
import withHelpWidget from '../WithHelpWidget';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

const wrappedComponentText = () => 'wrapped component text';

describe('withHelpWidget', () => {
	afterEach(cleanup);

	it('should render a wrapped component', () => {
		const WrappedComponent = withHelpWidget(wrappedComponentText);

		const {container} = render(<WrappedComponent groupId='123' />);

		expect(container.textContent).toBe('wrapped component text');
	});

	it('should render a HelperWidget Component', () => {
		const WrappedComponent = withHelpWidget(wrappedComponentText);

		const {container} = render(<WrappedComponent groupId='123' />);

		expect(container.querySelector('.helper-widget-wrapper')).toBeTruthy();
	});
});
