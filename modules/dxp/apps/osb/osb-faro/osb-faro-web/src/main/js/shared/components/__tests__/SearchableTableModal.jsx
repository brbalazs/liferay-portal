import * as data from 'test/data';
import Promise from 'metal-promise';
import React from 'react';
import SearchableTableModal from '../SearchableTableModal';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const COLUMNS = [
	{
		accessor: 'name',
		className: 'table-cell-expand',
		label: 'name'
	},
	{
		accessor: 'email',
		label: 'email'
	}
];

const defaultProps = {
	columns: COLUMNS,
	dataSourceFn: () => Promise.resolve(),
	groupId: '23',
	onClose: noop
};

const DefaultComponent = props => (
	<StaticRouter>
		<SearchableTableModal {...defaultProps} {...props} />
	</StaticRouter>
);

describe('SearchableTableModal', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		expect(container).toMatchSnapshot();
	});

	it('should render with a custom title', () => {
		const {container} = render(<DefaultComponent title='Custom Title' />);

		expect(container.querySelector('.modal-title')).toHaveTextContent(
			'Custom Title'
		);
	});

	it('should render with a custom submit button message', () => {
		const {container} = render(
			<DefaultComponent submitMessage='Custom Submit Message' />
		);

		expect(container.querySelector('.btn-primary')).toHaveTextContent(
			'Custom Submit Message'
		);
	});

	it('should render with preselected items', () => {
		const {container} = render(
			<DefaultComponent
				dataSourceFn={() =>
					Promise.resolve(
						data.mockSearch(data.mockSegment, 1, {id: 'foo'})
					)
				}
				selectedItems={[{id: 'foo', name: 'fooSegmentName'}]}
				submitMessage='Custom Submit Message'
			/>
		);

		jest.runAllTimers();

		expect(
			container.querySelector(
				'.table > tbody:nth-of-type(1) > tr .custom-checkbox input:checked'
			).checked
		).toBeTrue();
	});
});
