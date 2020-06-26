import FormNavigation from '../FormNavigation';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('FormNavigation', () => {
	it('should render', () => {
		const {container} = render(<FormNavigation cancelHref='' />);

		expect(container).toMatchSnapshot();
	});
});
