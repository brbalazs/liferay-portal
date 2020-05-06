import * as API from 'shared/api';
import * as data from 'test/data';
import BaseFieldMappingView from '../BaseFieldMappingView';
import FaroConstants from 'shared/util/constants';
import Promise from 'metal-promise';
import React from 'react';
import {DataSource, User} from 'shared/util/records';
import {shallow} from 'enzyme';

const {fieldContexts} = FaroConstants;

describe('BaseFieldMappingView', () => {
	it('should render', () => {
		const component = shallow(
			<BaseFieldMappingView
				context={fieldContexts.demographics}
				currentUser={data.getImmutableMock(User, data.mockUser)}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockSalesforceDataSource
				)}
				groupId='23'
				id='123'
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render w/o loading', () => {
		const component = shallow(
			<BaseFieldMappingView
				context={fieldContexts.demographics}
				currentUser={data.getImmutableMock(User, data.mockUser)}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockSalesforceDataSource
				)}
				groupId='23'
				id='123'
			/>
		);

		jest.runAllTimers();

		expect(component.find('Spinner').length).toEqual(0);
	});

	it('should render w/ error display', () => {
		API.dataSource.fetchMappingsLite.mockReturnValueOnce(
			Promise.reject({})
		);

		const component = shallow(
			<BaseFieldMappingView
				context={fieldContexts.demographics}
				currentUser={data.getImmutableMock(User, data.mockUser)}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockSalesforceDataSource
				)}
				groupId='23'
				id='123'
				title='This is a title'
			/>
		);

		jest.runAllTimers();

		expect(component.find('ErrorDisplay').length).toEqual(1);
	});

	it('should render w/ details', () => {
		const details = 'This is the details';
		const component = shallow(
			<BaseFieldMappingView
				context={fieldContexts.demographics}
				currentUser={data.getImmutableMock(User, data.mockUser)}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockSalesforceDataSource
				)}
				details={details}
				groupId='23'
				id='123'
			/>
		);

		jest.runAllTimers();

		expect(component.findWhere(n => n.text() === details).length).toEqual(
			1
		);
	});

	it('should render w/ title', () => {
		const title = 'This is a title';
		const component = shallow(
			<BaseFieldMappingView
				context={fieldContexts.demographics}
				currentUser={data.getImmutableMock(User, data.mockUser)}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockSalesforceDataSource
				)}
				groupId='23'
				id='123'
				title={title}
			/>
		);

		jest.runAllTimers();

		expect(
			component.findWhere(n => n.type() === 'h4' && n.text() === title)
				.length
		).toEqual(1);
	});
});
