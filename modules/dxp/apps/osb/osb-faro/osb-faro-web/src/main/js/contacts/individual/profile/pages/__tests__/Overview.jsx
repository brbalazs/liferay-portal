import * as data from 'test/data';
import Overview from '../Overview';
import React from 'react';
import {Individual} from 'shared/util/records';
import {MockedProvider} from '@apollo/react-testing';
import {mockTimeRangeReq} from 'test/graphql-data';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('IndividualOverview', () => {
	it('should render', () => {
		const {container} = render(
			<MockedProvider mocks={[mockTimeRangeReq()]}>
				<StaticRouter>
					<Overview
						groupId='23'
						id='test'
						individual={data.getImmutableMock(
							Individual,
							data.mockIndividual
							)}
							/>
				</StaticRouter>
			</MockedProvider>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
