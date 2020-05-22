import AssetsListCard from '../AssetsListCard';
import BasePage from 'shared/components/base-page';
import client from 'shared/apollo/client';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {BrowserRouter} from 'react-router-dom';
import {shallow} from 'enzyme';

const items = [
	{
		assetId: '01234',
		assetType: 'journal',
		interactions: 1,
		title: '231494203',
		type: 'Web Content'
	},
	{
		assetId: '01234',
		assetType: 'journal',
		interactions: 1,
		title: '231494203',
		type: 'Web Content'
	},
	{
		assetId: '01234',
		assetType: 'journal',
		interactions: 1,
		title: '231494203',
		type: 'Web Content'
	},
	{
		assetId: '01234',
		assetType: 'journal',
		interactions: 1,
		title: '231494203',
		type: 'Web Content'
	},
	{
		assetId: '01234',
		assetType: 'journal',
		interactions: 1,
		title: '231494203',
		type: 'Web Content'
	},
	{
		assetId: '01234',
		assetType: 'journal',
		interactions: 1,
		title: '231494203',
		type: 'Web Content'
	},
	{
		assetId: '01234',
		assetType: 'journal',
		interactions: 1,
		title: '231494203',
		type: 'Web Content'
	},
	{
		assetId: '01234',
		assetType: 'journal',
		interactions: 1,
		title: '231494203',
		type: 'Web Content'
	},
	{
		assetId: '01234',
		assetType: 'journal',
		interactions: 1,
		title: '231494203',
		type: 'Web Content'
	}
];

describe('AssetsListCard', () => {
	let component;

	afterEach(() => {
		if (component) {
			component.unmount();
		}
	});

	const MOCK_CONTEXT = {
		router: {
			params: {
				channelId: 123,
				groupId: '2000',
				touchpoint: 'www.liferay.com'
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
					<AssetsListCard
						{...props}
						rangeSelectors={{rangeKey: '30'}}
					/>
				</BrowserRouter>
			</BasePage.Context.Provider>
		</ApolloProvider>
	);

	it('should render component', () => {
		component = shallow(<WrappedComponent />);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render component with items', () => {
		component = shallow(<WrappedComponent items={items} />);

		expect(component.render()).toMatchSnapshot();
	});
});
