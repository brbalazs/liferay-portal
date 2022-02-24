import EmptyStateDashboard from '../EmptyStateDashboard';
import React from 'react';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

const WrappedComponent = props => (
	<EmptyStateDashboard {...props} title='Empty State Dashboard Title' />
);

describe('EmptyStateDashboard', () => {
	afterEach(cleanup);

	it('should render a EmptyStateDashboard component', () => {
		const {container, getByText} = render(<WrappedComponent />);

		expect(
			getByText('Empty State Dashboard Title').classList.contains('title')
		).toBeTruthy();
		expect(container).toMatchSnapshot();
	});

	it('should render a EmptyStateDashboard component with a description', () => {
		const {container, getByText} = render(
			<WrappedComponent description='Empty State Dashboard Description' />
		);

		expect(
			getByText('Empty State Dashboard Description').classList.contains(
				'secondary-info'
			)
		).toBeTruthy();
		expect(container).toMatchSnapshot();
	});

	it('should render a EmptyStateDashboard component with a icon', () => {
		const {container} = render(<WrappedComponent symbol='ac-satellite' />);

		expect(
			container.querySelector('svg').classList.contains('icon-size-xxxl')
		).toBeTruthy();
		expect(container).toMatchSnapshot();
	});

	it('should render a EmptyStateDashboard component with autofit class', () => {
		const {container} = render(
			<WrappedComponent autoFit symbol='ac-satellite' />
		);

		const emptyStateDiv = container.querySelector(
			'div .empty-state-dashboard-root'
		);

		expect(emptyStateDiv).toHaveClass('autofit');
	});
});
