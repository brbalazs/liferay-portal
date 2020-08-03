import React from 'react';
import Table, {getRowIdentifierValue} from '../index';
import {mockIndividual} from 'test/data';
import {shallow} from 'enzyme';
import {times} from 'lodash';
const INDIVIDUALS = times(5, i => mockIndividual(i));

const COLUMNS = [
	{
		accessor: 'name',
		label: 'Name'
	},
	{
		accessor: 'properties.salary',
		label: 'Salary'
	}
];

const INDIVIDUALS_EUROPE_ARCHITECT = times(5, i =>
	mockIndividual(i, {
		country: 'Europe',
		jobTitle: 'Architect'
	})
);

const INDIVIDUALS_EUROPE_WRITER = times(5, i =>
	mockIndividual(i, {
		country: 'Europe',
		jobTitle: 'Writer'
	})
);

const GROUPS = [
	{
		jobTitles: [
			{
				individuals: INDIVIDUALS,
				total: 5,
				value: 'Developer'
			}
		],
		total: '5',
		totalSalary: '250000',
		value: 'USA'
	},
	{
		jobTitles: [
			{
				individuals: INDIVIDUALS_EUROPE_ARCHITECT,
				total: 5,
				value: 'Architect'
			},
			{
				individuals: INDIVIDUALS_EUROPE_WRITER,
				total: 5,
				value: 'Writer'
			}
		],
		total: '10',
		totalSalary: '500000',
		value: 'Europe'
	}
];

describe('Table', () => {
	it('should render', () => {
		const component = shallow(
			<Table columns={COLUMNS} items={INDIVIDUALS} rowIdentifier='id' />
		);
		expect(component).toMatchSnapshot();
	});

	it('should render without items', () => {
		const component = shallow(
			<Table columns={COLUMNS} rowIdentifier='id' />
		);
		expect(component).toMatchSnapshot();
	});

	it('should render with borders', () => {
		const component = shallow(
			<Table
				bordered
				columns={COLUMNS}
				items={INDIVIDUALS}
				rowIdentifier='id'
			/>
		);

		expect(component.find('.table-bordered').exists()).toBe(true);
	});

	it('should render with nowrap headings', () => {
		const component = shallow(
			<Table
				columns={COLUMNS}
				headingNowrap
				items={INDIVIDUALS}
				rowIdentifier='id'
			/>
		);

		expect(component.find('.table-heading-nowrap').exists()).toBe(true);
	});

	it('should render with nowrap rows', () => {
		const component = shallow(
			<Table
				columns={COLUMNS}
				items={INDIVIDUALS}
				nowrap
				rowIdentifier='id'
			/>
		);

		expect(component.find('.table-nowrap').exists()).toBe(true);
	});

	it('should render and set orderParams state from the defaultSort if available', () => {
		const defaultSort = {
			field: 'name',
			sortOrder: 'asc'
		};

		const component = shallow(
			<Table
				columns={COLUMNS}
				defaultSort={defaultSort}
				items={INDIVIDUALS}
				rowIdentifier='id'
			/>
		);

		expect(component.state().orderParams.toJS()).toEqual(defaultSort);
	});

	it('should render with nested tables', () => {
		const component = shallow(
			<Table
				columns={COLUMNS}
				defaultSort={{
					field: 'name',
					sortOrder: 'desc'
				}}
				items={GROUPS}
				nestedTables={[
					{
						accessor: 'jobTitles',
						columns: [
							{
								accessor: 'value',
								label: 'Job Title'
							},
							{
								accessor: 'total',
								label: 'Individual Count'
							}
						],
						rowIdentifier: 'value'
					},
					{
						accessor: 'individuals',
						columns: [
							{
								accessor: 'name',
								label: 'Name'
							},
							{
								accessor: 'properties.salary',
								label: 'Salary'
							}
						],
						rowIdentifier: 'id'
					}
				]}
				rowIdentifier='id'
			/>
		);

		expect(component.findWhere(n => n.props().clickable).length).toBe(2);
	});

	it('should render with a loading spinner', () => {
		const component = shallow(
			<Table
				columns={COLUMNS}
				items={INDIVIDUALS}
				loading
				rowIdentifier='id'
			/>
		);

		expect(component.find('Spinner').exists()).toBe(true);
	});

	describe('getRowIdentifierValue', () => {
		it('should return a combination of the items specified in the rowIdentifier', () => {
			expect(
				getRowIdentifierValue(
					{company: 'Testers, Inc.', name: 'Test', title: 'tester'},
					['name', 'title']
				)
			).toBe('Testtester');
		});
	});
});
