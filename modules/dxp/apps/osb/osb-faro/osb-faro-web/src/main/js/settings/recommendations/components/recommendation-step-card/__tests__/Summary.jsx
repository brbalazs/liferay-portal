import * as data from 'test/data';
import Form from 'shared/components/form';
import React from 'react';
import Summary from '../Summary';
import {
	jobTrainingFrequencies,
	jobTrainingPeriods,
	jobTypes
} from 'shared/util/constants';
import {MockedProvider} from '@apollo/react-testing';
import {
	mockRecommendationActivitiesReq,
	mockRecommendationPageAssetsReq
} from 'test/graphql-data';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

describe('Summary', () => {
	it('should render', () => {
		const {container} = render(
			<MockedProvider
				mocks={[
					mockRecommendationPageAssetsReq([], {size: 0}),
					mockRecommendationActivitiesReq([])
				]}
			>
				<StaticRouter>
					<Form
						initialValues={{
							itemFilters: [
								{
									id: 'includeFilter - url ~ .*custom-assets',
									name: 'includeFilter',
									value: 'url ~ .*custom-assets'
								}
							],
							name: 'Test Name',
							trainingFrequency: jobTrainingFrequencies.manual,
							trainingPeriod: jobTrainingPeriods.last30Days,
							type: jobTypes.itemSimilarity
						}}
					>
						{({initialValues, values}) => (
							<Form.Form>
								<Summary
									initialValues={initialValues}
									trainingDate={data.getTimestamp()}
									{...values}
								/>
							</Form.Form>
						)}
					</Form>
				</StaticRouter>
			</MockedProvider>
		);

		expect(container).toMatchSnapshot();
	});
});
