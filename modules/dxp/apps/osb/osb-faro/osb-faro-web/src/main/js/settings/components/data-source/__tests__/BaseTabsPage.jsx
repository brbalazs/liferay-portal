import * as data from 'test/data';
import BaseTabsPage from '../BaseTabsPage';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {DataSource, User} from 'shared/util/records';
import {Routes} from 'shared/util/router';
import {shallow} from 'enzyme';

const {dataSourceStates} = FaroConstants;

function getMockLiferayDataSource(id, config) {
	return data.getImmutableMock(
		DataSource,
		data.mockLiferayDataSource,
		id,
		config
	);
}

const defaultProps = {
	addRoute: Routes.SETTINGS_LIFERAY_ADD,
	configurationRoute: Routes.SETTINGS_LIFERAY_CONFIGURATION_STATUS,
	currentUser: new User(data.mockUser()),
	dataSource: getMockLiferayDataSource(1, {
		provider: {
			type: 'LIFERAY'
		}
	}),
	groupId: '23',
	id: 'test'
};

const allConfigured = {
	provider: {
		analyticsConfiguration: {sites: ['1']},
		contactsConfiguration: {enableAllContacts: true},
		type: 'LIFERAY'
	}
};

describe('BaseTabsPage', () => {
	it('should render', () => {
		const component = shallow(<BaseTabsPage {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});

	it('should render without a sticker in the configure datasource tab if all items are configured', () => {
		const component = shallow(
			<BaseTabsPage
				{...defaultProps}
				dataSource={getMockLiferayDataSource(1, allConfigured)}
			/>
		);

		expect(component.find('.badge').length).toBe(0);
	});

	it('should render with the CONFIGURE_DATA_SOURCE tab as active', () => {
		const component = shallow(
			<BaseTabsPage {...defaultProps} activeTabId='configureDataSource' />
		);

		expect(
			component
				.find('Item')
				.at(1)
				.prop('active')
		).toBe(true);
	});

	it('should render with the CONFIGURE_DATA_SOURCE tab enabled if state is ready', () => {
		const component = shallow(
			<BaseTabsPage
				{...defaultProps}
				dataSource={getMockLiferayDataSource(0, {
					state: dataSourceStates.ready
				})}
			/>
		);

		expect(
			component
				.find('Item')
				.at(1)
				.prop('disabled')
		).toBe(false);
	});

	it('should render with the CONFIGURE_DATA_SOURCE tab disabled if state is not valid', () => {
		const component = shallow(
			<BaseTabsPage
				{...defaultProps}
				dataSource={getMockLiferayDataSource(0, {
					state: dataSourceStates.credentialsInvalid
				})}
			/>
		);

		expect(
			component
				.find('Item')
				.at(1)
				.prop('disabled')
		).toBe(true);
	});
});
