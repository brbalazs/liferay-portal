import * as data from 'test/data';
import NewRuleModal from '../NewRuleModal';
import mockStore from 'test/mock-store';
import React from 'react';
import {mockRecommendationPageAssetsReq} from 'test/graphql-data';
import {MockedProvider} from '@apollo/react-testing';
import {Provider} from 'react-redux';
import {range} from 'lodash';
import {render} from '@testing-library/react';
import {waitForLoading} from 'test/helpers';

jest.unmock('react-dom');

describe('NewRuleModal', () => {
	it('should render', async () => {
		const {container} = render(
			<MockedProvider mocks={[mockRecommendationPageAssetsReq([])]}>
				<Provider store={mockStore()}>
					<NewRuleModal
						delta={5}
						orderBy='desc'
						orderByField='title'
						page={0}
					/>
				</Provider>
			</MockedProvider>
		);

		await waitForLoading(container);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
