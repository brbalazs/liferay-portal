import BarChartHTML from '../BarChartHTML';
import React from 'react';
import {shallow} from 'enzyme';

React.createRef = jest.fn();

const groupItemsRef = {
	current: {
		clientHeight: 0,
		offsetHeight: 0,
		scrollHeight: 0,
		scrollTop: 0
	}
};

const CLASSNAME = '.analytics-bar-chart-html';

const HEADER = [
	{
		color: 'red',
		icon: 'home',
		label: 'header column 1'
	},
	{
		color: 'green',
		icon: 'home',
		label: 'header column 1'
	}
];

const COLUMNS = [
	{
		color: 'red',
		icon: 'home',
		label: 'item column 1'
	},
	{
		color: 'green',
		icon: 'home',
		label: 'item column 2'
	}
];

const PROGRESS_WITH_STRING = [
	{
		color: 'red',
		value: '50%'
	},
	{
		color: 'blue',
		value: '50%'
	}
];

const PROGRESS_WITH_NUMBER = [
	{
		color: 'red',
		value: 500
	},
	{
		color: 'blue',
		value: 500
	}
];

const ITEMS = [
	{
		columns: COLUMNS,
		progress: PROGRESS_WITH_STRING
	}
];

const TOOLTIP = {
	header: [
		{
			label: 'tooltip header'
		}
	],
	rows: [
		{
			columns: [
				{
					label: 'tooltip column 1'
				},
				{
					label: 'tooltip column 2'
				}
			]
		}
	]
};

describe('BarChartHTML', () => {
	let component;

	afterEach(() => {
		if (component) {
			component.unmount();
		}
	});

	it('should render component without crashing', () => {
		component = shallow(<BarChartHTML data={{}} />);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render component with header', () => {
		component = shallow(<BarChartHTML header={HEADER} />);

		expect(
			component.find(`${CLASSNAME}-header`).render()
		).toMatchSnapshot();
	});

	it('should render component with items', () => {
		component = shallow(<BarChartHTML items={ITEMS} />);

		expect(component.find(`${CLASSNAME}-items`).render()).toMatchSnapshot();
	});

	it('should update component when receive new data', () => {
		let props = {items: ITEMS};

		React.createRef.mockReturnValueOnce(groupItemsRef);

		component = shallow(<BarChartHTML {...props} />);

		props = {
			items: [
				...ITEMS,
				{
					items: ITEMS
				}
			]
		};

		component.instance().componentDidUpdate(props);
		expect(component.find(`${CLASSNAME}-items`).render()).toMatchSnapshot();
	});

	it('should render component with items nested', () => {
		component = shallow(
			<BarChartHTML
				items={[
					...ITEMS,
					{
						expanded: true,
						items: ITEMS
					}
				]}
			/>
		);

		expect(
			component.find(`${CLASSNAME}-group-items`).render()
		).toMatchSnapshot();
	});

	it('should render component item with control button to toggle content', () => {
		component = shallow(
			<BarChartHTML
				items={[
					...ITEMS,
					{
						expanded: true,
						items: ITEMS,
						showControls: true
					}
				]}
			/>
		);

		expect(component.find(`${CLASSNAME}-button`).first()).toMatchSnapshot();
	});

	it('should toggle content when click in button', () => {
		component = shallow(
			<BarChartHTML
				items={[
					...ITEMS,
					{
						expanded: true,
						items: ITEMS,
						showControls: true
					}
				]}
			/>
		);

		const mockedEvent = {
			currentTarget: {
				dataset: {
					index: 0
				}
			}
		};

		const button = component.find(`${CLASSNAME}-button`).first();

		button.simulate('click', mockedEvent);

		expect(component.state().items[0].expanded).toBeTruthy();

		button.simulate('click', mockedEvent);

		expect(component.state().items[0].expanded).toBeFalsy();
	});

	it('should set tooltip when MouseEnter on Item', () => {
		component = shallow(
			<BarChartHTML
				items={[
					...ITEMS,
					{
						tooltip: TOOLTIP
					}
				]}
			/>
		);

		component.instance().handleMouseEnterItem(TOOLTIP);

		expect(component.state().tooltip.show).toBeTruthy();
		expect(component.state().tooltip).toEqual({
			header: [
				{
					label: 'tooltip header'
				}
			],
			position: {
				left: 0,
				top: 0
			},
			rows: [
				{
					columns: [
						{
							label: 'tooltip column 1'
						},
						{
							label: 'tooltip column 2'
						}
					]
				}
			],
			show: true
		});
	});

	it('should set tooltip when MouseLeave on Item', () => {
		component = shallow(
			<BarChartHTML
				items={[
					...ITEMS,
					{
						tooltip: TOOLTIP
					}
				]}
			/>
		);

		component.instance().handleMouseLeaveItem();

		expect(component.state().tooltip.show).toBeFalsy();
		expect(component.state().tooltip).toEqual({
			header: [],
			position: {
				left: 0,
				top: 0
			},
			rows: [],
			show: false
		});
	});

	it('should render Progress item based on grid', () => {
		component = shallow(
			<BarChartHTML
				grid={{
					formatter: value => value,
					maxValue: 1000,
					minValue: 0,
					show: true
				}}
				items={[
					{
						columns: COLUMNS,
						progress: PROGRESS_WITH_NUMBER
					}
				]}
			/>
		);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render Progress item based on grid and type of grid is percentage', () => {
		component = shallow(
			<BarChartHTML
				grid={{
					maxValue: 1000,
					minValue: 0,
					show: true,
					type: 'percentage'
				}}
				items={[
					{
						columns: COLUMNS,
						progress: PROGRESS_WITH_NUMBER
					}
				]}
			/>
		);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render Progress with Interval item based on grid and type of grid is percentage', () => {
		component = shallow(
			<BarChartHTML
				grid={{
					formatter: value => value,
					maxValue: 1000,
					minValue: 0,
					show: true,
					type: 'percentage'
				}}
				items={[
					{
						columns: COLUMNS,
						intervals: [
							{
								end: 600,
								start: 400
							},
							{
								end: 600,
								start: 400
							}
						],
						progress: PROGRESS_WITH_NUMBER
					}
				]}
			/>
		);

		expect(component.render()).toMatchSnapshot();
	});

	it('should return an array with intervals', () => {
		component = shallow(
			<BarChartHTML
				data={{
					items: [
						{
							columns: COLUMNS,
							progress: PROGRESS_WITH_NUMBER
						}
					]
				}}
				grid={{
					formatter: value => value,
					maxValue: 1000,
					minValue: 0,
					show: true
				}}
			/>
		);

		expect(component.instance().getIntervals()).toEqual([
			0,
			500,
			1000,
			1500
		]);
	});

	it('should render an arrow down icon when there is a scroll down', () => {
		const mockEvent = {
			target: {
				clientHeight: 500,
				offsetHeight: 500,
				scrollHeight: 600,
				scrollTop: 150
			}
		};

		component = shallow(<BarChartHTML items={ITEMS} />);

		component
			.find(`${CLASSNAME}-group-items`)
			.simulate('scroll', mockEvent);

		expect(
			component.instance().showArrowDownIcon(mockEvent.target)
		).toBeTruthy();
		expect(
			component.find('.icon.text-l-secondary').render()
		).toMatchSnapshot();
	});

	it('should render an arrow down icon when the scroll is at the end of content', () => {
		const mockEvent = {
			target: {
				clientHeight: 500,
				offsetHeight: 500,
				scrollHeight: 600,
				scrollTop: 100
			}
		};

		component = shallow(<BarChartHTML items={ITEMS} />);

		component
			.find(`${CLASSNAME}-group-items`)
			.simulate('scroll', mockEvent);

		expect(
			component.instance().showArrowDownIcon(mockEvent.target)
		).toBeFalsy();
	});

	it('should return true if there is items', () => {
		component = shallow(<BarChartHTML items={ITEMS} />);

		expect(component.instance().hasItems(ITEMS)).toBeTruthy();
		expect(component.instance().hasItems(HEADER)).toBeTruthy();
		expect(component.instance().hasItems(TOOLTIP.header)).toBeTruthy();
		expect(component.instance().hasItems(TOOLTIP.rows)).toBeTruthy();
	});

	it('should render label as component when passing to props', () => {
		const label = () => (
			<div className='label'>{'this is a label as component'}</div>
		);

		component = shallow(
			<BarChartHTML
				items={[
					{
						columns: [
							{
								label
							}
						]
					}
				]}
			/>
		);

		expect(component.instance().renderLabel(label).type).toEqual('div');
		expect(
			component.find(`${CLASSNAME}-column`).render()
		).toMatchSnapshot();
	});

	it('should render icon without Circle component when there is a not color', () => {
		const columns = [
			{
				icon: 'home',
				label: 'item column 1'
			}
		];

		component = shallow(
			<BarChartHTML
				items={[
					{
						columns
					}
				]}
			/>
		);

		const icon = shallow(component.instance().renderIcon(columns[0]));

		expect(icon.instance().render().type).toEqual('svg');
		expect(
			component.find(`${CLASSNAME}-column`).render()
		).toMatchSnapshot();
	});

	it('should return left and top positions based on event', () => {
		component = shallow(<BarChartHTML items={ITEMS} />);

		const mockEvent = {pageX: 100, pageY: 100};

		const instance = component.instance();

		expect(instance.alignTooltip(mockEvent, 100, 100)).toEqual({
			left: 50,
			top: -21
		});
		expect(instance.alignTooltip(mockEvent, 200, 200)).toEqual({
			left: 0,
			top: -121
		});
	});
});
