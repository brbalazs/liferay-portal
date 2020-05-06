import BasePage from 'shared/components/base-page';
import client from 'shared/apollo/client';
import React from 'react';
import TouchpointsListCard from '../TouchpointsListCard';
import {ApolloProvider} from '@apollo/react-components';
import {BrowserRouter} from 'react-router-dom';
import {shallow} from 'enzyme';

const items = [
	{
		title: 'https://www.liferay.com/digital-experience-platform',
		touchpoint: 'https://www.liferay.com/digital-experience-platform'
	},
	{
		title: 'https://www.liferay.com/digital-experience-platform',
		touchpoint: 'https://www.liferay.com/digital-experience-platform'
	},
	{
		title: 'https://www.liferay.com/digital-experience-platform',
		touchpoint: 'https://www.liferay.com/digital-experience-platform'
	}
];

const MOCK_CONTEXT = {
	router: {
		params: {
			channelId: '456',
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
			<BrowserRouter>
				<TouchpointsListCard {...props} />
			</BrowserRouter>
		</BasePage.Context.Provider>
	</ApolloProvider>
);

describe('TouchpointsListCard', () => {
	it('should render', () => {
		const component = shallow(<WrappedComponent />);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render with items', () => {
		const component = shallow(<WrappedComponent items={items} />);

		expect(component.render()).toMatchSnapshot();
	});
});
