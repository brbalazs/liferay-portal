import Constants from 'shared/util/constants';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {
	defaultSearch,
	defaultSort,
	fetchLocalData,
	ViewSelectedToggle,
	withSelection
} from '../WithCrossPageSelect';
import {NAME} from 'shared/util/pagination';
import {OrderedMap} from 'immutable';
import {range} from 'lodash';

const {
	pagination: {orderAscending}
} = Constants;

jest.unmock('react-dom');

const mockItemArray = [
	{id: '1', name: 'orange'},
	{id: '2', name: 'apple'},
	{id: '3', name: 'banana'},
	{id: '4', name: 'grapefruit'},
	{id: '5', name: 'strawberry'},
	{id: '6', name: 'tangerine'}
];

const mockData = new OrderedMap(mockItemArray.map(item => [item.id, item]));

describe('defaultSearch', () => {
	it('should return the results of a search on the given items', () => {
		expect(
			defaultSearch({items: mockData, query: 'orange'}).toArray()
		).toEqual(mockItemArray.slice(0, 1));
	});
});

describe('defaultSort', () => {
	it('should return the results of a sort on the given items', () => {
		expect(defaultSort(mockData, orderAscending, NAME).toArray()).toEqual([
			mockItemArray[1],
			mockItemArray[2],
			mockItemArray[3],
			mockItemArray[0],
			mockItemArray[4],
			mockItemArray[5]
		]);
	});
});

describe('fetchLocalData', () => {
	it('should return the paginated results', () => {
		const mockData = new OrderedMap(
			range(9)
				.map(i => ({id: i, name: `name-${i}`}))
				.map(item => [item.id, item])
		);

		expect(
			fetchLocalData({
				delta: 5,
				items: mockData,
				orderBy: orderAscending,
				orderByField: NAME,
				page: 1,
				query: ''
			})
		).toEqual(
			expect.objectContaining({
				items: mockData.slice(0, 5).toArray(),
				total: 9
			})
		);
	});
});

describe('WithSelection', () => {
	const expectedArgs = {
		onSelectItemsChange: expect.any(Function),
		selectedItemsIOMap: expect.any(OrderedMap),
		showCheckbox: true,
		toolbarProps: expect.objectContaining({
			onSelectEntirePage: expect.any(Function),
			selectEntirePage: false,
			selectEntirePageIndeterminate: false
		})
	};

	afterEach(cleanup);

	it('should return a function component with the mapped props', () => {
		const componentSpy = jest.fn(() => <div />);

		const WrappedComponent = withSelection(componentSpy);

		render(<WrappedComponent items={mockItemArray} />);

		expect(componentSpy).toBeCalledWith(
			expect.objectContaining({
				items: [...mockItemArray],
				...expectedArgs
			}),
			{}
		);
	});

	it('should NOT mark the toolbar as all checked if every item is disabled and there are no selected items', () => {
		const componentSpy = jest.fn(() => <div />);

		const WrappedComponent = withSelection(componentSpy);

		render(
			<WrappedComponent
				checkDisabled={({name}) => name === 'orange'}
				items={[mockItemArray[0]]}
			/>
		);

		expect(componentSpy).toBeCalledWith(
			expect.objectContaining({
				items: [mockItemArray[0]],
				...expectedArgs
			}),
			{}
		);
	});
});

describe('ViewSelectedToggle', () => {
	const defaultProps = {onClick: jest.fn(), selectedITemsCount: 1};

	it('should render with the "view selected" message', () => {
		const {container} = render(
			<ViewSelectedToggle {...defaultProps} showSelected={false} />
		);

		expect(container).toMatchSnapshot();
	});

	it('should render with the "return to list" message', () => {
		const {container} = render(
			<ViewSelectedToggle {...defaultProps} showSelected />
		);

		expect(container).toMatchSnapshot();
	});
});
