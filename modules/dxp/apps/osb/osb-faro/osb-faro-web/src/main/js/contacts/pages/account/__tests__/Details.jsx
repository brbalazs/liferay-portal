import * as data from 'test/data';
import Details from '../Details';
import React from 'react';
import {Account} from 'shared/util/records';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('AccountDetails', () => {
	it('should render', async() => {
		const {container} = render(
			<StaticRouter>
				<Details
					account={data.getImmutableMock(Account, data.mockAccount)}
					groupId='23'
					id='test'
				/>
			</StaticRouter>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
