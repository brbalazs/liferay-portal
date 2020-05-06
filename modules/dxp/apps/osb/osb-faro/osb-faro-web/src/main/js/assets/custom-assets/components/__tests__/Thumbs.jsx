import React from 'react';
import Thumbs from '../Thumbs';
import {shallow} from 'enzyme';

const items = [
	{
		selected: true,
		svg: 'cerebro-thumb-line-chart',
		text: 'this is a thumb 1',
		value: 'line'
	},
	{
		selected: false,
		svg: 'cerebro-thumb-line-chart',
		text: 'this is a thumb 2',
		value: 'line'
	}
];

const defaultProps = {
	items,
	onSelectThumb: jest.fn()
};

describe('AddReport', () => {
	let component;

	afterEach(() => {
		if (component) {
			component.unmount();
		}
	});

	it('should render component', () => {
		const component = shallow(<Thumbs {...defaultProps} />);
		expect(component).toMatchSnapshot();
	});

	it('should select the second thumb when handleClickSelectThumb is called', () => {
		jest.useFakeTimers();
		const component = shallow(<Thumbs {...defaultProps} />);

		component.instance().handleClickSelectThumb({
			target: {parentNode: {dataset: {id: 1}}}
		});

		jest.runAllTimers();
		expect(component).toMatchSnapshot();
	});
});
