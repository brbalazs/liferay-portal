import React from 'react';
import UpgradeConnectionType from '../UpgradeConnectionType';
import {cleanup, fireEvent, render} from '@testing-library/react';
import {noop} from 'lodash';

jest.unmock('react-dom');

describe('UpgradeConnectionType', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<UpgradeConnectionType onClose={noop} onNext={noop} />
		);

		expect(container).toHaveTextContent('Upgrading your connection type.');
	});

	it('calls onNext when "Continue With Upgrade" is clicked', () => {
		const spy = jest.fn();

		const {queryByText} = render(
			<UpgradeConnectionType onClose={noop} onNext={spy} />
		);

		expect(spy).not.toBeCalled();

		fireEvent.click(queryByText('Continue With Upgrade'));

		expect(spy).toBeCalled();
	});

	it('calls onClose when "Upgrade Later" is clicked', () => {
		const spy = jest.fn();

		const {queryByText} = render(
			<UpgradeConnectionType onClose={spy} onNext={noop} />
		);

		expect(spy).not.toBeCalled();

		fireEvent.click(queryByText('Upgrade Later'));

		expect(spy).toBeCalled();
	});
});
