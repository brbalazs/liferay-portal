import * as API from 'shared/api';
import * as data from 'test/data';
import IndividualProfileCard from '../ProfileCard';
import Promise from 'metal-promise';
import React from 'react';
import {Individual} from 'shared/util/records';
import {MockedProvider} from '@apollo/react-testing';
import {mockTimeRangeReq} from 'test/graphql-data';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const DefaultComponent = props => (
	<MockedProvider mocks={[mockTimeRangeReq()]}>
		<StaticRouter>
			<IndividualProfileCard
				channelId='123123'
				entity={new Individual(data.mockIndividual())}
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
