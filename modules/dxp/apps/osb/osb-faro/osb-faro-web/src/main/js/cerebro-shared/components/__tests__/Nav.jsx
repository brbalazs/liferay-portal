import Nav from '../Nav';
import React from 'react';
import {shallow} from 'enzyme';

describe('Nav', () => {
	const items = [
		<Nav.Item active href='#' key={1}>
			{'foo'}
		</Nav.Item>,
		<Nav.Item key={2}>{'bar'}</Nav.Item>,
		<Nav.Item key={3}>{'baz'}</Nav.Item>
	];

	it('should render', () => {
		const component = shallow(<Nav />);
		expect(component).toMatchSnapshot();
	});

	it('should render vertically', () => {
		const component = shallow(<Nav vertical />);

		expect(component).toMatchSnapshot();
	});

	it('should render with items', () => {
		const component = shallow(<Nav children={items} />);

		expect(component).toMatchSnapshot();
	});

	it('should render with underline class', () => {
		const component = shallow(<Nav children={items} display='underline' />);

		expect(component).toMatchSnapshot();
	});
});

describe('Nav.Item', () => {
	it('should render', () => {
		const component = shallow(<Nav.Item children='Child' />);
		expect(component).toMatchSnapshot();
	});

	it('should render as monospaced', () => {
		const component = shallow(
			<Nav.Item children='Child' href='www.liferay.com' linkMonospaced />
		);

		expect(component).toMatchSnapshot();
	});
});
