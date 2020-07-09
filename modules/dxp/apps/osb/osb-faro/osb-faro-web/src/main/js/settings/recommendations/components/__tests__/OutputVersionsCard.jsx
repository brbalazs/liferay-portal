import * as data from 'test/data';
import OutputVersionsCard from '../OutputVersionsCard';
import React from 'react';
import {jobRunStatuses, jobTrainingFrequencies} from 'shared/util/constants';
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
						data.mockRecommendationRunJob(0),
						data.mockRecommendationRunJob(1, {
							status: jobRunStatuses.failed
						}),
						data.mockRecommendationRunJob(2, {
							status: jobRunStatuses.published
						}),
						data.mockRecommendationRunJob(3, {
							status: jobRunStatuses.running
						})
					])
				]}
			>
				<OutputVersionsCard
					router={{params: {jobId: '321'}}}
					trainingFrequency={jobTrainingFrequencies.every14Days}
				/>
			</MockedProvider>
		);

		await waitForLoading(container);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
