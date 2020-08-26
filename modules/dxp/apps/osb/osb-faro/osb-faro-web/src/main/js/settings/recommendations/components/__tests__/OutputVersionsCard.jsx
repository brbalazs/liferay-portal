import * as data from 'test/data';
import OutputVersionsCard from '../OutputVersionsCard';
import React from 'react';
import {jobRunFrequencies, jobRunStatuses} from 'shared/util/constants';
import {MockedProvider} from '@apollo/react-testing';
import {mockRecommendationJobRunsReq} from 'test/graphql-data';
import {render} from '@testing-library/react';
import {waitForLoading} from 'test/helpers';

jest.unmock('react-dom');

describe('OutputVersionsCard', () => {
	it('should render', async() => {
		const {container} = render(
			<MockedProvider
				mocks={[
					mockRecommendationJobRunsReq([
						data.mockRecommendationJobRun(0),
						data.mockRecommendationJobRun(1, {
							status: jobRunStatuses.failed
						}),
						data.mockRecommendationJobRun(2, {
							status: jobRunStatuses.published
						}),
						data.mockRecommendationJobRun(3, {
							status: jobRunStatuses.running
						})
					])
				]}
			>
				<OutputVersionsCard
					nextRunDate={new Date()}
					router={{params: {jobId: '321'}}}
					runFrequency={jobRunFrequencies.every14Days}
				/>
			</MockedProvider>
		);

		await waitForLoading(container);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render w/o "Next X" date', async() => {
		const {queryByText} = render(
			<MockedProvider
				mocks={[
					mockRecommendationJobRunsReq([
						data.mockRecommendationJobRun(0),
						data.mockRecommendationJobRun(1, {
							status: jobRunStatuses.failed
						}),
						data.mockRecommendationJobRun(2, {
							status: jobRunStatuses.published
						}),
						data.mockRecommendationJobRun(3, {
							status: jobRunStatuses.running
						})
					])
				]}
			>
				<OutputVersionsCard
					router={{params: {jobId: '321'}}}
					runFrequency={jobRunFrequencies.every14Days}
				/>
			</MockedProvider>
		);

		jest.runAllTimers();

		expect(queryByText(/Next/)).toBeNull();
	});
});
