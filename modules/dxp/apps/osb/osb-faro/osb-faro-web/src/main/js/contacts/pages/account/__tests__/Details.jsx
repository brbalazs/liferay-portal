import * as data from 'test/data';
import Details from '../Details';
import React from 'react';
import {Account} from 'shared/util/records';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('AccountDetails', () => {
	it('should render', () => {
		const {container} = render(
			<Details
				account={data.getImmutableMock(Account, data.mockAccount)}
				groupId={'23'}
				id={'test'}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
