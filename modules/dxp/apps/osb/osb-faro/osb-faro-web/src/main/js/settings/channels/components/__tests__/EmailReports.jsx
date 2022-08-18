import DisplayReportMessage from '../EmailReports';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

jest.mock('react-router-dom', () => ({
	...jest.requireActual('react-router-dom'),
	useParams: () => ({
		groupId: '2000'
	})
}));

describe('DisplayReportMessage', () => {
	it('should render', () => {
		const {container} = render(
			<DisplayReportMessage
				channelId='1234'
				className='test'
				sitesSynced={false}
			/>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render with config btn enabled', () => {
		const {container} = render(
			<DisplayReportMessage
				channelId='1234'
				className='test'
				sitesSynced
			/>
		);

		jest.runAllTimers();

		const configBtn = document.querySelector(
			'button[title="Configure Email Reports"]'
		);

		expect(container).toContainElement(configBtn);
		expect(configBtn).toBeInTheDocument();
		expect(configBtn).toBeEnabled();
	});

	it('should render with config btn disabled', () => {
		const {container} = render(
			<DisplayReportMessage
				channelId='1234'
				className='test'
				sitesSynced={false}
			/>
		);

		jest.runAllTimers();

		const configBtn = document.querySelector(
			'button[title="Configure Email Reports"]'
		);

		expect(container).toContainElement(configBtn);
		expect(configBtn).toBeInTheDocument();
		expect(configBtn).toBeDisabled();
	});
});

// TODO Create tests to show enable/disable messages when backend is done. To be done when [F]LRAC-11729 is completed.
