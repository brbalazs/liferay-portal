import OrganizationsQuery from 'contacts/components/segment-editor/dynamic/queries/OrganizationsQuery';
import React from 'react';
import SearchableTableModalGraphql from '../SearchableTableModalGraphql';
import {cleanup, render} from '@testing-library/react';
import {
	getMapResultToProps,
	mapPropsToOptions
} from 'contacts/components/segment-editor/dynamic/mappers/dxp-entity-bag-mapper';
import {MockedProvider} from '@apollo/react-testing';
import {mockOrganizationsListReq} from 'test/graphql-data';
import {noop} from 'lodash';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const COLUMNS = [
	{
		accessor: 'name',
		label: 'name'
	}
];

const defaultProps = {
	columns: COLUMNS,
	delta: 5,
	graphqlQuery: OrganizationsQuery,
	groupId: '23',
	mapPropsToOptions,
	mapResultToProps: getMapResultToProps('organizations'),
	onClose: noop,
	orderBy: 'asc',
	orderByField: 'name',
	page: 1
};

const DefaultComponent = props => (
	<StaticRouter>
		<MockedProvider mocks={[mockOrganizationsListReq()]}>
			<SearchableTableModalGraphql {...defaultProps} {...props} />
		</MockedProvider>
	</StaticRouter>
);

describe('SearchableTableModalGraphql', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

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
				selectedItems={[{id: 0, name: 'fooOrganization-0'}]}
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
