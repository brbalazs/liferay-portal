import HeaderCard from '../HeaderCard';
import React from 'react';
import {ACTIVITIES} from 'shared/util/router';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

const DefaultComponent = props => (
	<HeaderCard label='Title' tabId={ACTIVITIES} {...props} />
);

describe('HeaderProfile', () => {
	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
