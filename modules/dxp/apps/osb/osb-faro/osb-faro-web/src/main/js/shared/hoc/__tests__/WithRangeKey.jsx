import React from 'react';
import withRangeKey from '../WithRangeKey';
import {shallow} from 'enzyme';

describe('WithRangeKey', () => {
	it('should render the original component', () => {
		const componentSpy = jest.fn();
		const WrappedComponent = withRangeKey(componentSpy);

		shallow(<WrappedComponent rangeKey={'30'} />).shallow();

		expect(componentSpy).toHaveBeenCalled();
	});

	it('should pass rangeKey as a prop to the wrapped component', () => {
		const componentSpy = jest.fn();
		const WrappedComponent = withRangeKey(componentSpy);

		shallow(<WrappedComponent rangeKey={'30'} />).shallow();

		expect(componentSpy).toBeCalledWith(
			expect.objectContaining({
				onChangeRangeKey: expect.any(Function),
				rangeKey: '30'
			}),
			{}
		);
	});
});
