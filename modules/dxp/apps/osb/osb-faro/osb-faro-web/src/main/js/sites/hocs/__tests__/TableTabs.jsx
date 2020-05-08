import BasePage from 'shared/components/base-page';
import client from 'shared/apollo/client';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';
import {withTableTabs} from '../TableTabs';

jest.unmock('react-dom');

const TableTabsWrappedComponent = withTableTabs(
	() => C => props => <C {...props} />,
	[{getColumns: jest.fn()}]
);

const MOCK_CONTEXT = {
	rangeKey: {defaultValue: '30'},
	router: {
		params: {
			channelId: '123',
			groupId: '2000'
		},
		query: {
			rangeKey: '30'
		}
	}
};

const WrappedComponent = props => (
	<ApolloProvider client={client}>
		<BasePage.Context.Provider value={MOCK_CONTEXT}>
			<StaticRouter>
				<TableTabsWrappedComponent {...props} />
			</StaticRouter>
		</BasePage.Context.Provider>
	</ApolloProvider>
);

describe('TableTabs', () => {
	it('render', () => {
		const {container} = render(<WrappedComponent footerHref='foo/route' />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
