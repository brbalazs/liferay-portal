import * as API from 'shared/api';
import client from 'shared/apollo/client';
import IndividualProfileCard from '../ProfileCard';
import Promise from 'metal-promise';
import React from 'react';
import {
	fireEvent,
	render,
	waitForElementToBeRemoved
} from '@testing-library/react';
import {Individual} from 'shared/util/records';
import {MockedProvider} from '@apollo/react-testing';
import {mockEventMetrics, mockIndividual, mockSessions} from 'test/data';
import {mockTimeRangeReq} from 'test/graphql-data';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

jest.mock('shared/apollo/client', () => ({
	query: jest.fn()
}));

const DefaultComponent = props => (
	<MockedProvider mocks={[mockTimeRangeReq()]}>
		<StaticRouter>
			<IndividualProfileCard
				channelId='123123'
				entity={new Individual(mockIndividual())}
				groupId='23'
				interval='D'
				rangeSelectors={{rangeKey: 30}}
				{...props}
			/>
		</StaticRouter>
	</MockedProvider>
);

const inputValue = 'add to cart';

describe('IndividualProfileCard', () => {
	it('should render', async () => {
		client.query
			.mockResolvedValueOnce({
				data: mockEventMetrics()
			})
			.mockResolvedValueOnce({
				data: mockSessions()
			});

		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});

	it('should clear search input when clear button is clicked', async () => {
		client.query
			.mockResolvedValueOnce({
				data: mockEventMetrics()
			})
			.mockResolvedValueOnce({
				data: mockSessions()
			})
			.mockResolvedValueOnce({
				data: mockEventMetrics()
			})
			.mockResolvedValueOnce({
				data: mockSessions()
			})
			.mockResolvedValueOnce({
				data: mockEventMetrics()
			})
			.mockResolvedValue({
				data: mockSessions()
			});

		const {container, getByPlaceholderText, getByText} = render(
			<DefaultComponent />
		);

		jest.runAllTimers();

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		const searchInput = getByPlaceholderText('Search');

		fireEvent.change(searchInput, {target: {value: inputValue}});

		fireEvent.keyDown(searchInput, {
			charCode: 13,
			code: 'Enter',
			key: 'Enter'
		});

		jest.runAllTimers();

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(getByPlaceholderText('Search')).toHaveValue(inputValue);

		fireEvent.click(getByText('Clear'));

		jest.runAllTimers();

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(getByPlaceholderText('Search')).toHaveValue('');
	});

	it('should clear search input when X clear button is clicked', async () => {
		client.query
			.mockResolvedValueOnce({
				data: mockEventMetrics()
			})
			.mockResolvedValueOnce({
				data: mockSessions()
			})
			.mockResolvedValueOnce({
				data: mockEventMetrics()
			})
			.mockResolvedValueOnce({
				data: mockSessions()
			})
			.mockResolvedValueOnce({
				data: mockEventMetrics()
			})
			.mockResolvedValue({
				data: mockSessions()
			});

		const {container, getByPlaceholderText} = render(<DefaultComponent />);

		jest.runAllTimers();

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		const searchInput = getByPlaceholderText('Search');

		fireEvent.change(searchInput, {target: {value: inputValue}});

		fireEvent.keyDown(searchInput, {
			charCode: 13,
			code: 'Enter',
			key: 'Enter'
		});

		jest.runAllTimers();

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(getByPlaceholderText('Search')).toHaveValue(inputValue);

		fireEvent.click(container.querySelector('.lexicon-icon-times'));

		jest.runAllTimers();

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(getByPlaceholderText('Search')).toHaveValue('');
	});

	xit('should render w/ an error display', () => {
		API.activities.fetchHistory.mockReturnValueOnce(Promise.reject({}));

		const {getByText} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(getByText('An unexpected error occurred.')).toBeTruthy();
	});

	it('should render w/ loading', () => {
		const {container} = render(<DefaultComponent />);

		expect(container.querySelector('.spinner-root')).toBeTruthy();
	});
});
