import client from 'shared/apollo/client';
import EventAnalysisEditor from '../index';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Event Analysis Editor', () => {
	it('render', () => {
		const {container} = render(
			<ApolloProvider client={client}>
				<EventAnalysisEditor />
			</ApolloProvider>
		);

		expect(container).toMatchSnapshot();
	});
});
