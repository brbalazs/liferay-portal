import React from 'react';
import {render, shallow} from 'enzyme';
import {withEmpty, withError} from '../utils';

const MyAwesomeComponent = () => <div>{'my awesome component'}</div>;

describe('withEmpty', () => {
	it('should render empty state when "empty" props is true', () => {
		const ComposedComponent = withEmpty('an empty title')(
			MyAwesomeComponent
		);

		expect(
			shallow(<ComposedComponent empty />).is('NoResultsDisplay')
		).toBe(true);
	});

	it('should not render empty state when "empty" props is false', () => {
		const ComposedComponent = withEmpty()(MyAwesomeComponent);

		expect(
			shallow(<ComposedComponent empty={false} />).is('NoResultsDisplay')
		).toBe(false);
	});
});

describe('withError', () => {
	it('should render error state when "error" props is true', () => {
		const ComposedComponent = withError()(MyAwesomeComponent);

		const ExpectedComponent = () => <ComposedComponent error />;

		expect(render(<ExpectedComponent />)).toMatchSnapshot();
	});

	it('should not render error state when "error" props is true', () => {
		const ComposedComponent = withError()(MyAwesomeComponent);

		const ExpectedComponent = () => <ComposedComponent error={false} />;

		expect(render(<ExpectedComponent />)).toMatchSnapshot();
	});

	it('should render custom error state message when "error" props is true', () => {
		const ComposedComponent = withError()(MyAwesomeComponent);

		const ExpectedComponent = () => (
			<ComposedComponent error errorMessage='Sorry, it is an error!' />
		);

		expect(render(<ExpectedComponent />)).toMatchSnapshot();
	});
});
