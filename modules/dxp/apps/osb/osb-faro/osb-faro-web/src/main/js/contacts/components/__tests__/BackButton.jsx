import BackButton from '../BackButton.tsx';
import React from 'react';
import {shallow} from 'enzyme';

describe('BackButton', () => {
	it('should render', () => {
		const component = shallow(
			<BackButton href='foo.url' label='foo label' />
		);

		expect(component).toMatchSnapshot();
	});
});
