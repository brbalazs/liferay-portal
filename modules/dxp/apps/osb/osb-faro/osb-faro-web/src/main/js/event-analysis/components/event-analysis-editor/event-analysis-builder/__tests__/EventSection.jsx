import EventSection from '../EventSection';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('EventSection', () => {
	it('render', () => {
		const {container} = render(<EventSection />);

		expect(container).toMatchSnapshot();
	});

	it('render with event', () => {
		const {container} = render(
			<EventSection event={{name: 'View Article'}} />
		);

		expect(container).toMatchSnapshot();
	});
});
