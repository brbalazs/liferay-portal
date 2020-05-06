import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {TooltipChart, TooltipTmpl} from '../TooltipChart';

jest.unmock('react-dom');

const header = [
	{
		label: 'title'
	},
	{
		label: 'description'
	}
];

const rows = [
	{
		className: 'class-custom-row',
		columns: [
			{
				align: 'left',
				className: 'class-custom-column-1',
				color: 'red',
				label:
					'Lorem ipsum dolor sit amet consectetur adipisicing elit. Eius hic ex, vero laboriosam necessitatibus, repudiandae est voluptatem.',
				truncated: true,
				weight: 'normal',
				width: 100
			},
			{
				align: 'right',
				className: 'class-custom-column-2',
				label: 'column 2 description',
				weight: 'semibold',
				width: 50
			}
		]
	},
	{
		columns: [
			{
				align: 'left',
				color: 'blue',
				label: 'column 1 title',
				weight: 'light'
			},
			{
				align: 'right',
				label: 'column 2 description',
				weight: 'semibold'
			}
		]
	}
];

describe('TooltipChart', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<TooltipChart
				className='custom-tooltip-class'
				header={header}
				rows={rows}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});

describe('TooltipTmpl', () => {
	it('should render', () => {
		const {container} = render(<TooltipTmpl children='TooltipTmpl' />);

		expect(container).toMatchSnapshot();
	});
});

describe('TooltipTmpl.Body', () => {
	it('should render', () => {
		const {container} = render(<TooltipTmpl.Body children='Body' />);

		expect(container).toMatchSnapshot();
	});
});

describe('TooltipTmpl.Column', () => {
	it('should render', () => {
		const {container} = render(<TooltipTmpl.Column children='Column' />);

		expect(container).toMatchSnapshot();
	});

	it('should render as truncated', () => {
		const {container} = render(
			<TooltipTmpl.Column children='Column' truncated />
		);

		expect(container).toMatchSnapshot();
	});

	it('should render w/ right alignment', () => {
		const {container} = render(
			<TooltipTmpl.Column alignment='right' children='Column' />
		);

		expect(container).toMatchSnapshot();
	});

	it('should render w/ weight of light', () => {
		const {container} = render(
			<TooltipTmpl.Column children='Column' weight='light' />
		);

		expect(container).toMatchSnapshot();
	});
});

describe('TooltipTmpl.Header', () => {
	it('should render', () => {
		const {container} = render(<TooltipTmpl.Header children='Header' />);

		expect(container).toMatchSnapshot();
	});
});

describe('TooltipTmpl.Row', () => {
	it('should render', () => {
		const {container} = render(<TooltipTmpl.Row children='Row' />);

		expect(container).toMatchSnapshot();
	});
});
