import React from 'react';
import UpgradeConnectionCard from '../UpgradeConnectionCard';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';

jest.unmock('react-dom');

const actionsMock = [
	{
		label: 'test',
		onClick: () => noop
	}
];

describe('UpgradeConnectionCard', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<UpgradeConnectionCard />);

		expect(container.firstChild).toHaveClass(
			'upgrade-connection-card-root'
		);
	});

	it('should render with actions', () => {
		const {container} = render(
			<UpgradeConnectionCard actions={actionsMock} />
		);

		expect(container).toHaveTextContent('test');
	});
});
