import FaroConstants from 'shared/util/constants';
import Promise from 'metal-promise';
import React from 'react';
import SearchableTableWithStaged from '../index';
import {cleanup, fireEvent, render} from '@testing-library/react';
import {inputSearchText} from 'test/helpers';
import {MemoryRouter, StaticRouter} from 'react-router';
import {mockIndividual} from 'test/data';
import {NAME} from 'shared/util/pagination';
import {SelectionProvider} from 'shared/context/selection';
import {times} from 'lodash';

const {
	pagination: {cur, delta: defaultDelta, orderAscending}
} = FaroConstants;

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
	columns: COLUMNS,
	dataSourceFn: () =>
		Promise.resolve({
			items: INDIVIDUALS,
			total: INDIVIDUALS.length
		}),
	rowIdentifier: 'id',
	showStaged: true,
	stagedProps: {
		delta: defaultDelta,
		orderBy: orderAscending,
		orderByField: NAME,
		page: cur
	}
};

const DefaultComponent = props => (
	<SelectionProvider>
		<StaticRouter>
			<SearchableTableWithStaged {...defaultProps} {...props} />
		</StaticRouter>
	</SelectionProvider>
);

jest.unmock('react-dom');

describe('SearchableTableWithStaged', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<DefaultComponent
				dataSourceFn={() => Promise.resolve({items: [], total: 0})}
				showStaged={false}
				stagedProps={{}}
			/>
		);

		jest.runAllTimers();
		expect(container).toMatchSnapshot();
	});

	it('should show the staged table if showStaged is true', () => {
		const {container, getByTestId} = render(
			<SelectionProvider selectedItems={INDIVIDUALS.slice(0, 1)}>
				<StaticRouter>
					<SearchableTableWithStaged {...defaultProps} />
				</StaticRouter>
			</SelectionProvider>
		);

		jest.runAllTimers();

		fireEvent.click(getByTestId('view-selected'));

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should show the staged table with a query', () => {
		const {container, getByTestId} = render(
			<SelectionProvider selectedItems={INDIVIDUALS.slice(0, 1)}>
				<MemoryRouter>
					<SearchableTableWithStaged {...defaultProps} />
				</MemoryRouter>
			</SelectionProvider>
		);

		jest.runAllTimers();

		fireEvent.click(getByTestId('view-selected'));

		jest.runAllTimers();

		inputSearchText(container, 'fooQuery');

		expect(container).toBeTruthy();
	});
});
