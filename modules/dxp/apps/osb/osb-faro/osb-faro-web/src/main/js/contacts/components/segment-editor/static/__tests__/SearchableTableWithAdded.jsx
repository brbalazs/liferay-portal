import Promise from 'metal-promise';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {MemoryRouter} from 'react-router';
import {mockIndividual} from 'test/data';
import {OrderedMap} from 'immutable';
import {SearchableTableWithAdded} from '../SearchableTableWithAdded';
import {times} from 'lodash';

const COLUMNS = [
	{
		accessor: 'name',
		className: 'table-cell-expand',
		label: 'Name',
		title: true
	},
	{
		accessor: 'properties.email',
		label: 'Email'
	}
];

const TOTAL = 5;

const INDIVIDUALS = times(TOTAL, i =>
	mockIndividual(i, {email: `email${i}@liferay.com`})
);

const defaultProps = {
	addedItemsIOMap: new OrderedMap(INDIVIDUALS.map(item => [item.id, item])),
	columns: COLUMNS,
	dataSourceFn: () =>
		Promise.resolve({
			items: INDIVIDUALS,
			total: INDIVIDUALS.length
		}),
	rowIdentifier: 'id',
	showStaged: true
};

const DefaultComponent = props => (
	<MemoryRouter>
		<SearchableTableWithAdded {...defaultProps} {...props} />
	</MemoryRouter>
);

jest.unmock('react-dom');

describe('SearchableTableWithAdded', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<DefaultComponent
				addedItemsIOMap={new OrderedMap()}
				dataSourceFn={() => Promise.resolve({items: [], total: 0})}
				showStaged={false}
				stagedProps={{}}
			/>
		);

		jest.runAllTimers();
		expect(container).toMatchSnapshot();
	});

	it('should show the staged table if showStaged is true', () => {
		const {container} = render(<DefaultComponent {...defaultProps} />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should show the staged table with a query', () => {
		const {container} = render(
			<DefaultComponent
				{...defaultProps}
				stagedProps={{...defaultProps.stagedProps, query: 'fooQuery'}}
			/>
		);

		jest.runAllTimers();

		expect(container).toBeTruthy();
	});
});
