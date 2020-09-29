import * as data from 'test/data';
import Membership, {ChartViews} from '../Membership';
import mockStore from 'test/mock-store';
import React from 'react';
import {ENGAGEMENT, GROWTH} from 'shared/util/router';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {Segment} from 'shared/util/records';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const defaultProps = {
	channelId: '123',
	engagementHistory: {data: [], previousScore: 0},
	groupId: '23',
	growthHistory: {data: []},
	id: '321',
	segment: data.getImmutableMock(Segment, data.mockSegment),
	timeZoneId: 'UTC'
};

describe('Membership', () => {
	const WrappedComponent = props => (
		<Provider store={mockStore()}>
			<StaticRouter>
				<Membership {...defaultProps} {...props} />
			</StaticRouter>
		</Provider>
	);

	it('should render', () => {
		const {container} = render(<WrappedComponent />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render the growth tab', () => {
		const {queryByTestId} = render(<WrappedComponent />);

		jest.runAllTimers();

		expect(queryByTestId(GROWTH).className).toContain('active');
		expect(queryByTestId(ENGAGEMENT).className).not.toContain('active');
	});

	it('should render the engagement tab', () => {
		const {queryByTestId} = render(<WrappedComponent tabId={ENGAGEMENT} />);

		jest.runAllTimers();

		expect(queryByTestId(GROWTH).className).not.toContain('active');
		expect(queryByTestId(ENGAGEMENT).className).toContain('active');
	});
});

describe('ChartViews', () => {
	const WrappedComponent = props => (
		<StaticRouter>
			<ChartViews {...defaultProps} {...props} />
		</StaticRouter>
	);

	it('should render', () => {
		const {container} = render(<WrappedComponent />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render the growth tab', () => {
		const {queryByText} = render(<WrappedComponent />);

		jest.runAllTimers();

		expect(queryByText(/Known Members/)).toBeTruthy();
	});

	it('should render the engagement tab', () => {
		const {queryByText} = render(<WrappedComponent tabId={ENGAGEMENT} />);

		jest.runAllTimers();

		expect(queryByText(/Engaged Members/)).toBeTruthy();
	});
});
