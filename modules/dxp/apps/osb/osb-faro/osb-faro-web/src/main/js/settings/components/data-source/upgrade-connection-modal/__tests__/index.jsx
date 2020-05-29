import React from 'react';
import UpgradeConnectionModal from '..';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';

jest.unmock('react-dom');

describe('UpgradeConnectionModal', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<UpgradeConnectionModal groupId='123' id='456' onClose={noop} />
		);

		expect(container).toHaveTextContent('Upgrading your connection type.');
	});
});
