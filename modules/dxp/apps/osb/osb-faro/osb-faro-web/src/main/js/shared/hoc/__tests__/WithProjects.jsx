import React from 'react';
import {shallow} from 'enzyme';
import {withProjects} from '../WithProjects';

describe('WithProjects', () => {
	it('should pass projects to the WrappedComponent', () => {
		const WrappedComponent = withProjects(() => <div>{'foo'}</div>);

		const component = shallow(<WrappedComponent projects={[{}]} />);

		const hasProjectsProperty = Object.prototype.hasOwnProperty.call(
			component.props(),
			'projects'
		);

		expect(hasProjectsProperty).toBe(true);
	});
});
