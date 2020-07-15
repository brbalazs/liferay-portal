import * as data from 'test/data';
import Form from 'shared/components/form';
import React from 'react';
import Summary from '../Summary';
import {
	jobRunDataPeriods,
	jobRunFrequencies,
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
					mockRecommendationActivitiesReq([]),
					mockRecommendationActivitiesReq([], {rangeKey: 60})
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
							runDataPeriod: jobRunDataPeriods.last30Days,
							runFrequency: jobRunFrequencies.manual,
							type: jobTypes.itemSimilarity
						}}
					>
						{({initialValues, values}) => (
							<Form.Form>
								<Summary
									initialValues={initialValues}
									runDate={data.getTimestamp()}
									setFieldValue={jest.fn()}
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
