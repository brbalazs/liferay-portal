import * as data from 'test/data';
import client from 'shared/apollo/client';
import Overview from '../Overview';
import React from 'react';
import {ApolloProvider} from '@apollo/react-hoc';
import {Individual} from 'shared/util/records';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('IndividualOverview', () => {
	it('should render', () => {
		const {container} = render(
			<ApolloProvider client={client}>
				<StaticRouter>
					<Overview
						groupId={'23'}
						id={'test'}
						individual={data.getImmutableMock(
							Individual,
							data.mockIndividual
							)}
							/>
				</StaticRouter>
			</ApolloProvider>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
