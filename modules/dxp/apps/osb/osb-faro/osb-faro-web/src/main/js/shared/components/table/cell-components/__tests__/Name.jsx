import Name from '../Name';
import React from 'react';
import {shallow} from 'enzyme';

describe('Name', () => {
	it('should render', () => {
		const component = shallow(
			<Name data={{name: 'foo'}} renderSecondaryInfo={() => 'bar'} />
		);
		expect(component).toMatchSnapshot();
	});

	it('should render without a link in the name if disabled is true', () => {
		const component = shallow(
			<Name
				data={{id: 'test', name: 'foo'}}
				disabled
				groupId='23'
				routeFn={({data: {id}}) => `/foo/${id}`}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render the name as a link if a route is passed', () => {
		const component = shallow(
			<Name
				data={{id: 'test', name: 'foo'}}
				groupId='23'
				routeFn={({data: {id}}) => `/foo/${id}`}
			/>
		);
		expect(component).toMatchSnapshot();
	});

	it('should render with secondary info', () => {
		const component = shallow(
			<Name
				data={{id: 'test', name: 'foo'}}
				groupId='23'
				renderSecondaryInfo={() => 'bar'}
				routeFn={({data: {id}}) => `/foo/${id}`}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with an icon', () => {
		const component = shallow(
			<Name
				data={{id: 'test', name: 'foo'}}
				groupId='23'
				renderIcon={() => <div>{'foo icon'}</div>}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render using the nameKey', () => {
		const component = shallow(
			<Name data={{id: 'test', title: 'foo'}} nameKey='title' />
		);

		expect(component.render().text()).toEqual('foo');
	});

	it('should render the display name in TextTruncate if the tooltip prop is true', () => {
		const component = shallow(
			<Name data={{id: 'test', title: 'foo'}} tooltip />
		);

		expect(component.find('TextTruncate').exists()).toBe(true);
	});
});
