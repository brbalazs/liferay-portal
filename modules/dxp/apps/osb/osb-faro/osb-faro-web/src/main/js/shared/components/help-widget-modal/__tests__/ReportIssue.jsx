import React from 'react';
import ReportIssue from '../ReportIssue';
import {noop} from 'lodash';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('ReportIssue', () => {
	it('should render', () => {
		const {container} = render(
			<ReportIssue onClose={noop} onNext={noop} />
		);

		expect(container).toMatchSnapshot();
	});

	it('should render a modal with the inputs', () => {
		const {queryByText} = render(
			<ReportIssue onClose={noop} onNext={noop} />
		);

		expect(queryByText('Issue Title')).toBeTruthy();
		expect(queryByText('Description')).toBeTruthy();
	});
});
