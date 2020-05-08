import React from 'react';
import {render} from '@testing-library/react';
import {withEmpty, withError} from '../utils';

jest.unmock('react-dom');

const MyAwesomeComponent = () => <div>{'my awesome component'}</div>;

describe('withEmpty', () => {
	it('should render with emptyTitle if query exists & items is empty', () => {
		const ComposedComponent = withEmpty({emptyTitle: 'an empty title'})(
			MyAwesomeComponent
		);

		const {queryByText} = render(
			<ComposedComponent items={[]} total={0} />
		);

		expect(queryByText('an empty title')).toBeTruthy();
	});

	it('should render with default no results display if total is greater than 0 & items is empty', () => {
		const ComposedComponent = withEmpty({emptyTitle: 'an empty title'})(
			MyAwesomeComponent
		);

		const {queryByText} = render(
			<ComposedComponent entityLabel='tests' items={[]} total={1} />
		);

		expect(queryByText('an empty title')).toBeNull();
		expect(queryByText('There are no tests found.')).toBeTruthy();
	});

	it('should render with default no results display if total is 0 & items is empty', () => {
		const ComposedComponent = withEmpty({emptyTitle: 'an empty title'})(
			MyAwesomeComponent
		);

		const {queryByText} = render(
			<ComposedComponent
				entityLabel='tests'
				items={[]}
				query='asdf'
				total={0}
			/>
		);

		expect(queryByText('an empty title')).toBeNull();
		expect(queryByText('There are no tests found.')).toBeTruthy();
	});

	it('should not render empty state when items is not empty', () => {
		const ComposedComponent = withEmpty()(MyAwesomeComponent);

		const {queryByText} = render(
			<ComposedComponent items={['test']} total={1} />
		);

		expect(queryByText('my awesome component')).toBeTruthy();
	});
});

describe('withError', () => {
	it('should render error state when "error" props is true', () => {
		const ComposedComponent = withError()(MyAwesomeComponent);

		const {queryByText} = render(<ComposedComponent error />);

		expect(queryByText('Sorry, an error occurred.')).toBeTruthy();
	});

	it('should not render error state when "error" props is false', () => {
		const ComposedComponent = withError()(MyAwesomeComponent);

		const {queryByText} = render(<ComposedComponent error={false} />);

		expect(queryByText('my awesome component')).toBeTruthy();
	});

	it('should render custom error state message when "error" props is true', () => {
		const ComposedComponent = withError()(MyAwesomeComponent);

		const {queryByText} = render(
			<ComposedComponent error errorMessage='Sorry, it is an error!' />
		);

		expect(queryByText('Sorry, it is an error!')).toBeTruthy();
	});
});
