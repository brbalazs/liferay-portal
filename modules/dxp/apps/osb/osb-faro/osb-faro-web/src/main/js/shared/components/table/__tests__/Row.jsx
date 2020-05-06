import React from 'react';
import Row from '../Row';
import {mockIndividual} from 'test/data';
import {shallow} from 'enzyme';
const INDIVIDUAL = mockIndividual();

const INDIVIDUAL_NO_SALARY = mockIndividual(1, {
	salary: null
});

const COLUMNS = [
	{
		accessor: 'name',
		label: 'Name',
		title: true
	},
	{
		accessor: 'properties.salary',
		label: 'Salary'
	}
];

describe('Row', () => {
	it('should render', () => {
		const component = shallow(<Row />);
		expect(component).toMatchSnapshot();
	});

	it('should render with data', () => {
		const component = shallow(<Row columns={COLUMNS} data={INDIVIDUAL} />);
		expect(component).toMatchSnapshot();
	});

	it('should render with empty data if the column accessor value is null', () => {
		const component = shallow(
			<Row columns={COLUMNS} data={INDIVIDUAL_NO_SALARY} />
		);
		expect(component).toMatchSnapshot();
	});

	it('should render with empty data if the accessor does not exist at all on the object', () => {
		const component = shallow(
			<Row
				columns={COLUMNS.concat([
					{accessor: 'nonExistentAccessor', label: 'does not exist'}
				])}
				data={INDIVIDUAL}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with the header', () => {
		const component = shallow(
			<Row columns={COLUMNS} data={INDIVIDUAL} header />
		);
		expect(component).toMatchSnapshot();
	});
});
