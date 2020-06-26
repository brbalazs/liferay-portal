import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import Salesforce from '../Salesforce';
import {DataSource, User} from 'shared/util/records';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const {
	userRoleNames: {administrator}
} = FaroConstants;

const defaultProps = {
	currentUser: new User({roleName: administrator}),
	dataSource: data.getImmutableMock(
		DataSource,
		data.mockSalesforceDataSource
	),
	groupId: '23',
	id: 'test'
};

describe('Salesforce', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<Salesforce {...defaultProps} />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});
});
