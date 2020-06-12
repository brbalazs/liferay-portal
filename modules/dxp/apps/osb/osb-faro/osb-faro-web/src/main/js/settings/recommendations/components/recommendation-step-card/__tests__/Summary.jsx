import * as data from 'test/data';
import Form from 'shared/components/form';
import React from 'react';
import Summary from '../Summary';
import {
	jobTrainingFrequencies,
	jobTrainingPeriods,
	jobTypes
} from 'shared/util/constants';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

describe('Summary', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<Form
					initialValues={{
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
		);

		expect(container).toMatchSnapshot();
	});
});
