import * as data from 'test/data';
import OutputVersionsCard from '../OutputVersionsCard';
import React from 'react';
import {jobTrainingFrequencies} from 'shared/util/constants';
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
						data.mockRecommendationRunJob()
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
