import AnalysisSection from '../AnalysisSection';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('AnalysisSection', () => {
	it('render', () => {
		const {container} = render(<AnalysisSection />);

		expect(container).toMatchSnapshot();
	});

	it('render with event', () => {
		const {container} = render(
			<AnalysisSection event={{name: 'View Article'}} />
		);

		expect(container).toMatchSnapshot();
	});
});
