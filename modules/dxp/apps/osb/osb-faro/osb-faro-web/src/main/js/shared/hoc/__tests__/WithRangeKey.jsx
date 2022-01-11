import React from 'react';
import withRangeKey from '../WithRangeKey';
import {RangeKeyTimeRanges} from 'shared/util/constants';
import {render} from '@testing-library/react';

const RANGE_KEY_180 = RangeKeyTimeRanges.Last180Days;

jest.unmock('react-dom');

describe('WithRangeKey', () => {
	it('should render', () => {
		const WrappedComponent = withRangeKey(({rangeSelectors}) => (
			<div {...rangeSelectors}>{'Foobar'}</div>
		));

		const {container} = render(<WrappedComponent />);

		expect(container).toMatchSnapshot();
	});

	it('should pass rangeSelectors as a prop to the wrapped component', () => {
		const WrappedComponent = withRangeKey(
			({rangeSelectors: {rangeKey}}) => (
				<div>
					{rangeKey === RANGE_KEY_180
						? 'rangeKey is 180'
						: 'rangeKey is not 180'}
				</div>
			)
		);

		const {getByText} = render(
			<WrappedComponent
				rangeSelectors={{
					rangeEnd: null,
					rangeKey: RANGE_KEY_180,
					rangeStart: null
				}}
			/>
		);

		expect(getByText('rangeKey is 180'));
	});

	it('should change the rangeSelectors with the onRangeSelectorsChange function', () => {
		let handleRangeSelectors;

		const WrappedComponent = withRangeKey(
			({onRangeSelectorsChange, rangeSelectors: {rangeKey}}) => {
				handleRangeSelectors = onRangeSelectorsChange;

				return (
					<div>
						{rangeKey === RANGE_KEY_180
							? 'rangeKey is 180'
							: 'rangeKey is not 180'}
					</div>
				);
			}
		);

		const {getByText} = render(<WrappedComponent />);

		expect(getByText('rangeKey is not 180'));

		handleRangeSelectors({rangeKey: RANGE_KEY_180});

		expect(getByText('rangeKey is 180'));
	});
});
