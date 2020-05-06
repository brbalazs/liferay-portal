import CodeSnippet from '../CodeSnippet';
import React from 'react';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('CodeSnippet', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<CodeSnippet />);
		expect(container).toMatchSnapshot();
	});
});
