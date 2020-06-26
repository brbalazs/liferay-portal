import OperatingSystem from '../OperatingSystem';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

const devices = [
	{
		data: [
			{
				percentage: 10,
				type: 'android',
				views: 99
			},
			{
				percentage: 40,
				type: 'android',
				views: 2100
			},
			{
				percentage: 10,
				type: 'android',
				views: 292
			}
		],
		percentageOfTotal: 70,
		totalViews: 2880,
		type: 'Smartphone'
	},
	{
		data: [
			{
				percentage: 10,
				type: 'android',
				views: 2
			},
			{
				percentage: 40,
				type: 'android',
				views: 10
			},
			{
				percentage: 10,
				type: 'android',
				views: 30
			}
		],
		percentageOfTotal: 15,
		totalViews: 400,
		type: 'Tablet'
	},
	{
		data: [
			{
				percentage: 10,
				type: 'android',
				views: 99
			},
			{
				percentage: 40,
				type: 'android',
				views: 100
			},
			{
				percentage: 10,
				type: 'android',
				views: 502
			}
		],
		percentageOfTotal: 15,
		totalViews: 1030,
		type: 'Desktop'
	},
	{
		data: [
			{
				percentage: 10,
				type: 'android',
				views: 99
			},
			{
				percentage: 40,
				type: 'android',
				views: 100
			},
			{
				percentage: 10,
				type: 'android',
				views: 502
			}
		],
		percentageOfTotal: 15,
		totalViews: 1030,
		type: 'Others'
	}
];

describe('OperatingSystem', () => {
	it('should render', () => {
		const {container} = render(<OperatingSystem devices={devices} />);

		expect(container).toMatchSnapshot();
	});
});
