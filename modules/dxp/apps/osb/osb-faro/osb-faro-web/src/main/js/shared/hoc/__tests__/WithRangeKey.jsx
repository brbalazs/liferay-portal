import React from 'react';
import withRangeKey from '../WithRangeKey';
import {shallow} from 'enzyme';

describe('WithRangeKey', () => {
	it('should render the original component', () => {
		const componentSpy = jest.fn();
		const WrappedComponent = withRangeKey(componentSpy);

		shallow(
			<WrappedComponent
				rangeSelectors={{
					rangeEnd: null,
					rangeKey: '30',
					rangeStart: null
				}}
			/>
		).shallow();

		expect(componentSpy).toHaveBeenCalled();
	});

	it('should pass rangeSelectors as a prop to the wrapped component', () => {
		const componentSpy = jest.fn();
		const WrappedComponent = withRangeKey(componentSpy);

		shallow(
			<WrappedComponent
				rangeSelectors={{
					rangeEnd: null,
					rangeKey: '30',
					rangeStart: null
				}}
			/>
		).shallow();

		expect(componentSpy).toBeCalledWith(
			expect.objectContaining({
				onRangeSelectorsChange: expect.any(Function),
				rangeSelectors: {
					rangeEnd: null,
					rangeKey: '30',
					rangeStart: null
				}
			}),
			{}
		);
	});
});
