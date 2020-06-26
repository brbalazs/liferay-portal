import * as data from 'test/data';
import AssociatedSegments from '../AssociatedSegments';
import React from 'react';
import {Account} from 'shared/util/records';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('AccountAssociatedSegments', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<AssociatedSegments
					account={data.getImmutableMock(Account, data.mockAccount)}
					groupId={'23'}
					id={'test'}
				/>
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});
});
