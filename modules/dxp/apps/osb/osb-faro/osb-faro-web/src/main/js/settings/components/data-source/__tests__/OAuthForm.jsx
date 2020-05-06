import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {DataSource} from 'shared/util/records';
import {OAUTH_CALLBACK_URL} from 'shared/util/oauth';
import {OAuthForm} from '../OAuthForm';
import {shallow} from 'enzyme';

const {dataSourceStates, dataSourceStatuses, dataSourceTypes} = FaroConstants;

React.createRef = jest.fn();

const getMockRef = () => ({
	current: {
		validateForm: jest.fn()
	}
});

const defaultProps = {
	authorized: true,
	callbackUrl: OAUTH_CALLBACK_URL,
	dataSource: data.getImmutableMock(
		DataSource,
		data.mockLiferayDataSource,
		0
	),
	defaultUrl: 'https://foo.com',
	groupId: '23',
	onAuthorize: jest.fn(),
	onSubmit: jest.fn(),
	type: dataSourceTypes.liferay
};

describe('OAuthForm', () => {
	it('should render', () => {
		const component = shallow(<OAuthForm {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});

	it('should render with oauth owner', () => {
		const component = shallow(
			<OAuthForm
				{...defaultProps}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource,
					23,
					{
						credentials: {
							oAuthOwner: {
								emailAddress: 'test@liferay.com',
								name: 'test test'
							}
						}
					}
				)}
			/>
		);

		expect(
			component
				.shallow()
				.find('.oauth-owner .name')
				.text()
		).toBe('test test');
	});

	it('should render with "remove" button', () => {
		const component = shallow(
			<OAuthForm
				{...defaultProps}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource,
					23,
					{
						credentials: {
							oAuthOwner: {
								emailAddress: 'test@liferay.com',
								name: 'test test'
							}
						}
					}
				)}
			/>
		);

		component.setState({editing: true});

		expect(
			component
				.shallow()
				.find('.oauth-owner')
				.find('Button')
				.children()
				.text()
		).toBe('Remove');
	});

	it('should render without an edit button if authorized is false', () => {
		const component = shallow(
			<OAuthForm {...defaultProps} authorized={false} />
		);

		expect(component.shallow()).toMatchSnapshot();
	});

	it('should render by default as disabled with an edit button if this is an existing data source', () => {
		const component = shallow(
			<OAuthForm {...defaultProps} authorized id='23' />
		);

		expect(
			component
				.shallow()
				.find('Button')
				.children()
				.text()
		).toBe('Edit');
	});

	it('should render with an"Authorize & Save" and "Cancel" button if the edit button is clicked', () => {
		React.createRef.mockReturnValueOnce(getMockRef('foo'));

		const component = shallow(
			<OAuthForm
				{...defaultProps}
				authorized
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockSalesforceDataSource
				)}
				id='23'
				type={dataSourceTypes.salesforce}
			/>
		);

		expect(
			component
				.shallow()
				.find('Button')
				.children()
				.text()
		).toBe('Edit');

		component.instance().handleToggleEditing();

		jest.runAllTimers();

		expect(
			component
				.shallow()
				.find('Button')
				.at(0)
				.children()
				.text()
		).toBe('Authorize & Save');
	});

	it('should render with all inputs disabled if the user is not authorized and the datasource has an UNDEFINED_ERROR state', () => {
		const component = shallow(
			<OAuthForm
				{...defaultProps}
				authorized={false}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource,
					{
						state: dataSourceStates.undefinedError,
						status: dataSourceStatuses.inactive
					}
				)}
				id='23'
				type={dataSourceTypes.liferay}
			/>
		);

		expect(
			component
				.shallow()
				.find('ForwardRef')
				.everyWhere(node => node.props().disabled)
		).toBe(true);

		expect(
			component
				.shallow()
				.find('PasswordInput')
				.props().disabled
		).toBe(true);

		expect(
			component
				.shallow()
				.find('.oauth-owner')
				.hasClass('disabled')
		).toBe(true);
	});
});
