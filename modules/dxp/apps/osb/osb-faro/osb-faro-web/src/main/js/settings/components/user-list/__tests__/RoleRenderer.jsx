import React from 'react';
import RoleRenderer from '../RoleRenderer';
import {shallow} from 'enzyme';

const userRoleOptions = [
	{label: 'Administrator', value: 'Site Administrator'},
	{label: 'Member', value: 'Site Member'},
	{label: 'Owner', value: 'Site Owner'}
];

describe('RoleRenderer', () => {
	it('should render', () => {
		const component = shallow(
			<RoleRenderer data={{roleName: 'Site Owner'}} />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render as being edited', () => {
		const component = shallow(
			<RoleRenderer
				data={{roleName: 'Site Member'}}
				editing
				options={userRoleOptions}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render and call the onUpdateEdits prop callback with the initial roleName', () => {
		const onUpdateEditsSpy = jest.fn();

		shallow(
			<RoleRenderer
				data={{roleName: 'Site Member'}}
				editing
				onUpdateEdits={onUpdateEditsSpy}
				options={userRoleOptions}
			/>
		);

		expect(onUpdateEditsSpy).toHaveBeenCalledWith(
			'roleName',
			'Site Member'
		);
	});
});
