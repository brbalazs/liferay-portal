import React from 'react';
import SummarySection from '../index';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('SummarySection', () => {
	it('should render component', () => {
		const {container} = render(
			<SummarySection>
				<SummarySection.Variant lift='5%' status='up' />
				<SummarySection.Description value='My Summary Description' />
				<SummarySection.Heading value='My Summary Heading' />
				<SummarySection.ProgressBar value={100} />
				<SummarySection.MetricType value='Click Rate' />
			</SummarySection>
		);

		expect(container).toMatchSnapshot();
	});
});
