import BasePage from 'shared/components/base-page';
import client from 'shared/apollo/client';
import React from 'react';
import SankeyTouchpoint from '../SankeyTouchpoint';
import {ApolloProvider} from '@apollo/react-components';
import {BrowserRouter} from 'react-router-dom';
import {shallow} from 'enzyme';

const nodes = [
	{
		name: 'https://www-nightly.liferay.com/digital-experience-platform'
	},
	{
		name: 'ライフレイ：世界で最も利用されているポータルプラットフォーム'
	},
	{
		name: 'https://www-nightly.liferay.com/request-a-demo'
	},
	{
		name: 'others'
	},
	{
		directAccessMetric: 112,
		indirectAccessMetric: 65460,
		name: 'https://www-nightly.liferay.com/'
	}
];

const node = {
	directAccessMetric: 112,
	external: false,
	indirectAccessMetric: 65460,
	name: 'https://www-nightly.liferay.com/',
	url: 'https://www-nightly.liferay.com/'
};

const items = [
	{
		assetId: '1000',
		assetType: 'blog',
		title: 'liferay.com',
		url: 'https://www.liferay.com'
	}
];

/**
 * Calculate Expanded Touchpoint PositionFn
 */
const calcExpandedTouchpointPositionFn = () => 2;

/**
 * Calculate Sankey Node PositionFn
 */
const calcSankeyNodePositionFn = () => ({calculatedY0: 2});

/**
 * Get Delta Y Fn
 */
const getDeltaYFn = () => 2;

const onTouchpointLoaded = () => true;

const props = {
	activeIndex: 0,
	calcExpandedTouchpointPositionFn,
	calcSankeyNodePositionFn,
	events: {
		handleChangeTouchpointIndex: jest.fn(),
		handleMouseEnter: jest.fn(),
		handleMouseLeave: jest.fn(),
		touchpointLoaded: jest.fn()
	},
	getDeltaYFn,
	items,
	node,
	onTouchpointLoaded,
	rangeSelectors: {rangeKey: '30'},
	touchpoint: node.url,
	touchpointList: nodes
};

describe('SankeyTouchpoint', () => {
	const MOCK_CONTEXT = {
		router: {
			params: {
				assetType: 'blogs',
				channelId: '123',
				groupId: '2000',
				title: 'Sankey touchpoint tests'
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
					<SankeyTouchpoint {...props} />
				</BrowserRouter>
			</BasePage.Context.Provider>
		</ApolloProvider>
	);

	it('should render component Sankey Chart Main', () => {
		const component = shallow(<WrappedComponent {...props} isMain />);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render component Sankey Chart', () => {
		const component = shallow(<WrappedComponent {...props} />);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render component Sankey Chart with direct access', () => {
		const component = shallow(
			<WrappedComponent {...props} isDirectAccess />
		);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render component Sankey Chart with direct access and asset list', () => {
		const component = shallow(
			<WrappedComponent
				{...props}
				isDirectAccess
				items={[
					...items,
					{
						id: '5000',
						title: 'liferay.com/carrers',
						type: 'forms',
						url: 'https://liferay.com/carrers'
					}
				]}
			/>
		);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render component Sankey Chart with empty state', () => {
		const component = shallow(
			<WrappedComponent
				isEmptyState
				node={node}
				rangeSelectors={{rangeKey: '30'}}
			/>
		);

		expect(component.render()).toMatchSnapshot();
	});
});
