import React from 'react';
import toggleSwitch from '../ToggleSwitch';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

const DefaultComponent = props => toggleSwitch({field: {}, ...props});

describe('ToggleSwitch', () => {
	
	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		expect(container).toMatchSnapshot();
	});

	it('should render with an initial value', () => {
		const CheckedComponent = props =>
			toggleSwitch({field: {value: true}, ...props});

		const {container} = render(<CheckedComponent />);

		expect(container).toMatchSnapshot();
	});
});
