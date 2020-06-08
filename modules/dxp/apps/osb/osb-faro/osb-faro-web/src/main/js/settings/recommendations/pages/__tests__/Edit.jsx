import * as data from 'test/data';
import Edit from '../Edit';
import mockStore from 'test/mock-store';
import React from 'react';
import RecommendationQuery from '../../queries/RecommendationQuery';
import {MockedProvider} from '@apollo/react-testing';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';
import {waitForLoading} from 'test/helpers';

jest.unmock('react-dom');

const defaultProps = {
	router: {params: {groupId: '23'}, query: {delta: '10', page: '1'}}
};

export function mockRecommendationReq() {
	return {
		request: {
			query: RecommendationQuery,
			variables: {
				jobId: '321'
			}
		},
		result: {
			data: {
				job: {...data.mockRecommendationJob('321'), __typename: 'Job'}
			}
		}
	};
}

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<MockedProvider mocks={[mockRecommendationReq()]}>
				<Edit
					{...defaultProps}
					{...props}
					router={{params: {groupId: '123', jobId: '321'}}}
				/>
			</MockedProvider>
		</StaticRouter>
	</Provider>
);

describe('Edit', () => {
	it('should render', async() => {
		const {container} = render(<DefaultComponent />);

		await waitForLoading(container);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
