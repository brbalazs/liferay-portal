import React from 'react';
import Sankey from '../Sankey';
import {shallow} from 'enzyme';

jest.unmock('clay-charts');

const data = {
	links: [
		{
			source: 0,
			target: 4,
			value: 11
		},
		{
			source: 1,
			target: 4,
			value: 10
		},
		{
			source: 2,
			target: 4,
			value: 8
		},
		{
			source: 3,
			target: 4,
			value: 61
		}
	],
	nodes: [
		{
			directAccessMetric: 112,
			indirectAccessMetric: 65460,
			name: 'https://www-nightly.liferay.com/digital-experience-platform',
			url: 'https://www-nightly.liferay.com/'
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
			name: 'https://www-nightly.liferay.com/',
			url: 'https://www-nightly.liferay.com/'
		}
	]
};

const mainTouchpointItems = [
	{
		assetId: '33986',
		assetType: 'journal',
		interactions: 4,
		title: 'web content to test segments',
		type: 'Web Content'
	}
];

const props = {
	data,
	height: 650,
	renderTouchpointComponent: () => <div />,
	width: 900
};

describe('Sankey', () => {
	let component;

	afterEach(() => {
		if (component) {
			component.unmount();
		}
	});

	it('should render Sankey component', () => {
		component = shallow(<Sankey {...props} />);

		expect(component).toMatchSnapshot();
	});

	it('should render Sankey again when data changes', () => {
		component = shallow(<Sankey {...props} />);

		const newData = {
			links: [
				{
					source: 0,
					target: 4,
					value: 11
				},
				{
					source: 1,
					target: 4,
					value: 10
				},
				{
					source: 2,
					target: 4,
					value: 8
				},
				{
					source: 3,
					target: 4,
					value: 61
				}
			],
			nodes: [
				{
					name: 'https://www-nightly.liferay.com/link1'
				},
				{
					name: 'https://www-nightly.liferay.com/link1'
				},
				{
					name: 'https://www-nightly.liferay.com/link1'
				},
				{
					name: 'others-test'
				},
				{
					directAccessMetric: 112,
					indirectAccessMetric: 65460,
					name: 'https://www.liferay.com/'
				}
			]
		};

		// eslint-disable-next-line new-cap
		component.instance().UNSAFE_componentWillReceiveProps({
			data: {newVal: newData}
		});

		expect(component).toMatchSnapshot();
	});

	it('should change state with willReceiveProps when element is not ready', () => {
		component = shallow(<Sankey {...props} />);

		component.instance().inDocument = false;

		// eslint-disable-next-line new-cap
		component.instance().UNSAFE_componentWillReceiveProps({
			width: {newVal: 900}
		});

		expect(component).toMatchSnapshot();
	});

	it('should test mouseenter on Sankey chart', () => {
		component = shallow(<Sankey {...props} />);

		component.instance().handleMouseEnter({
			currentTarget: {
				dataset: {
					index: 0
				}
			}
		});

		expect(component).toMatchSnapshot();
	});

	it('should test mouseleave on svg group', () => {
		component = shallow(<Sankey {...props} />);

		const node = component.find('.analytics-sankey-path');

		component.instance().handleMouseLeave({currentTarget: node});

		expect(component).toMatchSnapshot();
	});

	it('should test sankey when a item is expanded', () => {
		component = shallow(<Sankey {...props} />);

		component.instance().handleChangeTouchpointIndex({
			index: 0,
			items: [
				{
					title: 'Liferay',
					url: 'https://www.liferay.com'
				}
			]
		});

		expect(component).toMatchSnapshot();
	});

	it('should caculate the position of an expanded touchpoint', () => {
		component = shallow(<Sankey {...props} />);

		component.instance().handleChangeTouchpointIndex({
			index: 1,
			items: [
				{
					title: 'Liferay',
					url: 'https://www.liferay.com'
				}
			]
		});

		component.instance().calcExpandedTouchpointPosition({
			index: 1
		});

		expect(component).toMatchSnapshot();
	});

	it("should calculate a sakey's asset list", () => {
		component = shallow(<Sankey {...props} />);

		const result = component.instance().calcSankeyNodePositionByAssetList(
			{
				index: 0
			},
			59.99
		);

		expect(result).toEqual({calculatedY0: 92.99000000000001, index: 0});
	});

	it('should render sankey path', () => {
		const component = shallow(<Sankey {...props} />);

		component.state().mainTouchpointItems = mainTouchpointItems;

		expect(component).toMatchSnapshot();
	});

	it('should render sankey with empty state', () => {
		component = shallow(
			<Sankey
				{...props}
				renderTouchpointComponent={() => <div>{'direct access'}</div>}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render sankey with empty state', () => {
		component = shallow(
			<Sankey
				{...props}
				renderTouchpointComponent={() => <div>{'empty state'}</div>}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render empty component when is loading', () => {
		component = shallow(
			<Sankey
				{...props}
				renderTouchpointComponent={() => <div>{'empty state'}</div>}
			/>
		);

		component.state().loading = true;

		expect(component).toMatchSnapshot();
	});
});
