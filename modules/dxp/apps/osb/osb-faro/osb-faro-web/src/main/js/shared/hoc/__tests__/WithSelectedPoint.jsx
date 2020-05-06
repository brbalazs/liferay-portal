import React from 'react';
import withSelectedPoint from '../WithSelectedPoint';
import {shallow} from 'enzyme';

describe('withSelectedPoint', () => {
	it('should render the wrapped component', () => {
		const WrappedComponent = withSelectedPoint(() => <div>{'foo'}</div>);
		const component = shallow(<WrappedComponent />);
		expect(component.shallow()).toMatchSnapshot();
	});

	it('should pass the selected point to the wrapped component', () => {
		const WrappedComponent = withSelectedPoint(jest.fn());
		const component = shallow(<WrappedComponent />);
		component.props().onPointSelect({index: 2});
		expect(component.props().selectedPoint).toEqual(2);
		expect(component.props().hasSelectedPoint).toBe(true);
	});
});
