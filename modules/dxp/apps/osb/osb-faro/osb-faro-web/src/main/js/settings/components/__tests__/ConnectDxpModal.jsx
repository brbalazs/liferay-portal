import ConnectDXPModal from '../ConnectDXPModal';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

describe('ConnectDXPModal', () => {
	afterEach(cleanup);

	it('renders', () => {
		const {container} = render(
			<StaticRouter>
				<ConnectDXPModal groupId='123' onClose={noop} />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});
});
