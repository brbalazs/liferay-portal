import ClearDataModal from '../ClearDataModal';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';

jest.unmock('react-dom');

describe('ClearDataModal', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<ClearDataModal onCloseFn={noop} onSubmitFn={noop} />
		);

		expect(container).toMatchSnapshot();
	});
});
