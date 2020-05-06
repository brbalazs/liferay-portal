import CrossPageSelect from '../CrossPageSelect';
import ListComponent from 'shared/hoc/ListComponent';
import React from 'react';
import {
	cleanup,
	fireEvent,
	getByTestId as getByTestIdGlobal,
	getByText as getByTextGlobal,
	render
} from '@testing-library/react';
import {inputSearchText, selectAllAndToggle} from 'test/helpers';
import {SelectionProvider} from 'shared/context/selection';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const mockItemArray = [
	{id: '1', name: 'orange'},
	{id: '2', name: 'apple'},
	{id: '3', name: 'banana'},
	{id: '4', name: 'grapefruit'},
	{id: '5', name: 'strawberry'},
	{id: '6', name: 'tangerine'}
];

const defaultProps = {
	columns: [{accessor: 'name', label: 'name'}],
	defaultOrderBy: 'asc',
	defaultOrderByField: 'name',
	defaultSort: {field: 'name', sortOrder: 'asc'},
	items: mockItemArray,
	total: mockItemArray.length
};

const DefaultComponent = props => (
	<StaticRouter>
		<SelectionProvider>
			<CrossPageSelect {...defaultProps} {...props}>
				{childProps => <ListComponent {...childProps} />}
			</CrossPageSelect>
		</SelectionProvider>
	</StaticRouter>
);

describe('CrossPageSelect', () => {
	afterEach(cleanup);

	it('should render the server data list by default', () => {
		const {container} = render(<DefaultComponent />);

		expect(container).toMatchSnapshot();
	});

	it('should render the selected list when the user presses the "view selected link"', () => {
		const {container, getByTestId} = render(<DefaultComponent />);

		const firstRowCheckbox = container.querySelector(
			'.table > tbody:nth-of-type(1) > tr .custom-checkbox input'
		);

		fireEvent.click(firstRowCheckbox);

		jest.runAllTimers();

		fireEvent.click(getByTestId('view-selected'));

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should be able to sort local data when a sort field is clicked', () => {
		const {container, getByText} = render(<DefaultComponent />);

		selectAllAndToggle(container);

		fireEvent.click(getByText('name'));
		jest.runAllTimers();

		const tableRows = container.querySelectorAll('tbody > tr');

		expect(tableRows.length).toBe(2);

		expect(tableRows[0]).toHaveTextContent('tangerine');
		expect(tableRows[1]).toHaveTextContent('strawberry');
	});

	it('should update local data displayed when a different pagination delta is chosen', () => {
		const {container, getByText} = render(<DefaultComponent />);

		selectAllAndToggle(container);

		fireEvent.click(getByText('2 Items'));
		const paginationOverlay = getByTestIdGlobal(document.body, 'overlay');

		fireEvent.click(getByTextGlobal(paginationOverlay, '3'));

		expect(getByText('apple')).toBeTruthy();
		expect(getByText('banana')).toBeTruthy();
		expect(getByText('grapefruit')).toBeTruthy();
	});

	it('it should search selected items when given a custom search function', () => {
		const mockSearcFn = ({items}) =>
			items.filter(({name}) => name === 'grapefruit');

		const {container} = render(
			<DefaultComponent searchSelectedFn={mockSearcFn} />
		);

		selectAllAndToggle(container);

		inputSearchText(container, 'fooQuery');

		const tableRows = container.querySelectorAll('tbody > tr');

		expect(tableRows.length).toBe(1);

		expect(tableRows[0]).toHaveTextContent('grapefruit');
	});
});
