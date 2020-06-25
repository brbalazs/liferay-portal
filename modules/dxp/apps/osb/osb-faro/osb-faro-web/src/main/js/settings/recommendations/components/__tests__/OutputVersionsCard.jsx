import * as data from 'test/data';
import mockStore from 'test/mock-store';
import OutputVersionsCard from '../OutputVersionsCard';
import React from 'react';
import {jobTrainingFrequencies} from 'shared/util/constants';
import {MockedProvider} from '@apollo/react-testing';
import {mockRecommendationJobRunsReq} from 'test/graphql-data';
import {Provider} from 'react-redux';
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
				<Provider store={mockStore()}>
					<OutputVersionsCard
						router={{params: {jobId: '321'}}}
						trainingFrequency={jobTrainingFrequencies.every14Days}
					/>
				</Provider>
			</MockedProvider>
		);

		await waitForLoading(container);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
