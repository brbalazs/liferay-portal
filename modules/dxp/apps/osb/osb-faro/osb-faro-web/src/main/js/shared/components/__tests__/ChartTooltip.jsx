import ChartTooltip from '../ChartTooltip';
import React from 'react';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('ChartTooltip', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<ChartTooltip
				items={[{label: 'Test Label', value: 'Test Value'}]}
				subtitle='Test Subtitle'
				title='Test Title'
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
