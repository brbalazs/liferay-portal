import HeaderCell from '../HeaderCell';
import React from 'react';
import {shallow} from 'enzyme';

describe('HeaderCell', () => {
	it('should render', () => {
		const component = shallow(<HeaderCell />);
		expect(component).toMatchSnapshot();
	});

	it('should render with sort disabled', () => {
		const component = shallow(<HeaderCell sortable={false} />);
		expect(component).toMatchSnapshot();
	});

	it('should render the header cell as a link if headerLink is true', () => {
		const component = shallow(<HeaderCell headerLink />);
		expect(component).toMatchSnapshot();
	});
});
