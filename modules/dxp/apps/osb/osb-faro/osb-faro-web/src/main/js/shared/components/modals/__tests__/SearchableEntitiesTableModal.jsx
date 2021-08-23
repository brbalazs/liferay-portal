import FaroConstants from 'shared/util/constants';
import Promise from 'metal-promise';
import React from 'react';
import SearchableEntitiesTableModal from '../SearchableEntitiesTableModal';
import {cleanup, render} from '@testing-library/react';
import {EMAIL_ADDRESS} from 'shared/util/pagination';
import {noop} from 'lodash';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const {orderDescending} = FaroConstants.pagination;

const DefaultComponent = props => (
	<StaticRouter>
		<SearchableEntitiesTableModal
			columns={[
				{
					accessor: 'name',
					className: 'table-cell-expand',
					label: 'name'
				},
				{
					accessor: 'emailAddress',
					label: 'email'
				}
			]}
			dataSourceFn={() => Promise.resolve()}
			groupId='23'
			onClose={noop}
			{...props}
		/>
	</StaticRouter>
);

describe('SearchableEntitiesTableModal', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		expect(container).toMatchSnapshot();
	});

	it('should render w/ defaultParams', () => {
		const {container} = render(
			<DefaultComponent
				defaultParams={{
					defaultOrderBy: orderDescending,
					defaultOrderByField: EMAIL_ADDRESS
				}}
			/>
		);

		const emailHeaderButton = container.querySelectorAll(
			'.table-head-title > button'
		)[1];

		expect(emailHeaderButton).toHaveTextContent('email');
		expect(
			emailHeaderButton.querySelector('.lexicon-icon-order-arrow-down')
		).toBeTruthy();
	});
});
