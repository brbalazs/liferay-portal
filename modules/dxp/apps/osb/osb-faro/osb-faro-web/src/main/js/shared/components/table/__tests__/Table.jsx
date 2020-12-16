import React from 'react';
import Table, {getRowIdentifierValue} from '../index';
import {mockIndividual} from 'test/data';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';
import {times} from 'lodash';

jest.unmock('react-dom');

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
		const {container} = render(
			<StaticRouter>
				<Table
					columns={COLUMNS}
					items={INDIVIDUALS}
					rowIdentifier='id'
				/>
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render without items', () => {
		const {container} = render(
			<StaticRouter>
				<Table columns={COLUMNS} rowIdentifier='id' />
			</StaticRouter>
		);

		expect(container.querySelector('tbody')).toBeNull();
	});

	it('should render with borders', () => {
		const {container} = render(
			<StaticRouter>
				<Table
					bordered
					columns={COLUMNS}
					items={INDIVIDUALS}
					rowIdentifier='id'
				/>
			</StaticRouter>
		);

		expect(container.querySelector('.table-bordered')).toBeTruthy();
	});

	it('should render with nowrap headings', () => {
		const {container} = render(
			<StaticRouter>
				<Table
					columns={COLUMNS}
					headingNowrap
					items={INDIVIDUALS}
					rowIdentifier='id'
				/>
			</StaticRouter>
		);

		expect(container.querySelector('.table-heading-nowrap')).toBeTruthy();
	});

	it('should render with nowrap rows', () => {
		const {container} = render(
			<StaticRouter>
				<Table
					columns={COLUMNS}
					items={INDIVIDUALS}
					nowrap
					rowIdentifier='id'
				/>
			</StaticRouter>
		);

		expect(container.querySelector('.table-nowrap')).toBeTruthy();
	});

	it('should render with nested tables', () => {
		const {container} = render(
			<StaticRouter>
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
			</StaticRouter>
		);

		expect(
			container.querySelector('.table-action-link').firstChild
		).toHaveClass('lexicon-icon-caret-right');
	});

	it('should render with a loading spinner', () => {
		const {container} = render(
			<StaticRouter>
				<Table
					columns={COLUMNS}
					items={INDIVIDUALS}
					loading
					rowIdentifier='id'
				/>
			</StaticRouter>
		);

		expect(container.querySelector('.spinner-root')).toBeTruthy();
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
