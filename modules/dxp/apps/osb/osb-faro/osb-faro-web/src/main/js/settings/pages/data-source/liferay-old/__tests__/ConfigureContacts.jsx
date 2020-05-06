import * as data from 'test/data';
import React from 'react';
import {ConfigureLiferayContacts as ConfigureContacts} from '../ConfigureContacts';
import {DataSource} from 'shared/util/records';
import {shallow} from 'enzyme';

const defaultProps = {
	dataSource: data.getImmutableMock(
		DataSource,
		data.mockLiferayDataSource,
		23,
		{
			provider: {
				analyticsConfiguration: {sites: ['1']},
				contactsConfiguration: {
					enableAllContacts: true
				}
			}
		}
	),
	groupId: '23',
	id: '23'
};

describe('ConfigureContacts', () => {
	it('should render', () => {
		const component = shallow(<ConfigureContacts {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});

	it('should route the user to the liferay contacts page if contacts are not configured', () => {
		const spy = jest.fn();

		shallow(
			<ConfigureContacts
				{...defaultProps}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource,
					23,
					{contactsConfiguration: null}
				)}
				history={{push: spy}}
			/>
		);

		expect(spy).toBeCalled();
	});
});
