import React from 'react';
import TimePeriodInput from '../TimePeriodInput';
import {cleanup, render} from '@testing-library/react';
import {LAST_7_DAYS} from '../../../utils/constants';

jest.unmock('react-dom');

describe('TimePeriodInput', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<TimePeriodInput onChange={jest.fn()} value={LAST_7_DAYS} />
		);

		expect(container).toMatchSnapshot();
	});
});
