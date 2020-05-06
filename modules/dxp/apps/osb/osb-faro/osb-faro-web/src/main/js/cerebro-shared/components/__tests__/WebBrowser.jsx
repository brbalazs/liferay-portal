import React from 'react';
import WebBrowser from '../WebBrowser';
import {render} from 'enzyme';

const browsers = [
	{
		color: '#4B9BFF',
		data: [1],
		id: '0Firefox'
	},
	{
		color: '#4B9B00',
		data: [2],
		id: '1Chrome'
	},
	{
		color: '#4B9B99',
		data: [3],
		id: '3Safari'
	}
];

describe('WebBrowser', () => {
	it('should render', () => {
		const component = render(<WebBrowser browsers={browsers} />);

		expect(component).toMatchSnapshot();
	});

	it('should render as empty', () => {
		const component = render(<WebBrowser empty metricLabel={'Views'} />);

		expect(component).toMatchSnapshot();
	});
});
