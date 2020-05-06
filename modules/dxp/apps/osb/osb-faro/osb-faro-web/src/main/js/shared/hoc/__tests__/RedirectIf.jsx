import * as Router from 'shared/util/router';
import React from 'react';
import redirectIf from '../RedirectIf';
import {Routes, toRoute} from 'shared/util/router';
import {shallow} from 'enzyme';
Router.navigate = jest.fn();

class TestComponent extends React.Component {
	render() {
		return (
			<div
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				{'component body'}
			</div>
		);
	}
}

describe('redirectIf', () => {
	it('should render a <Redirect /> if the routingFn returns a string', () => {
		const expectedRoute = toRoute(Routes.WORKSPACES);
		const Component = redirectIf(() => expectedRoute)(TestComponent);
		const component = shallow(<Component />);
		expect(component.find('Redirect').exists()).toBe(true);
	});

	it('should render the passed component if the routingFn does not return a string', () => {
		const Component = redirectIf(() => null)(TestComponent);
		const component = shallow(<Component />);
		expect(component).toMatchSnapshot();
	});
});
