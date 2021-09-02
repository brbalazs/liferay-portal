import * as API from 'shared/api';
import client from 'shared/apollo/client';
import IndividualProfileCard from '../ProfileCard';
import Promise from 'metal-promise';
import React from 'react';
import {Individual} from 'shared/util/records';
import {MockedProvider} from '@apollo/react-testing';
import {mockIndividual, mockSessions} from 'test/data';
import {mockTimeRangeReq} from 'test/graphql-data';
import {render, waitForElementToBeRemoved} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

jest.mock('shared/apollo/client', () => ({
	query: jest.fn()
}));

client.query.mockResolvedValue({
	data: mockSessions()
});

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

describe('IndividualProfileCard', () => {
	it('should render', async () => {
		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
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
