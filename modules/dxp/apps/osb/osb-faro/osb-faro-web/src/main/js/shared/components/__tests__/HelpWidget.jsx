import HelpWidget from '../HelpWidget';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('HelpWidget', () => {
	it('should render', () => {
		const {container} = render(<HelpWidget />);
		expect(container).toMatchSnapshot();
	});

	it('should render a dropdown', () => {
		const {getByText} = render(<HelpWidget />);
		expect(getByText('Report an Issue')).toBeTruthy();
	});
});
