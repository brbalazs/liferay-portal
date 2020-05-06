import * as API from 'shared/api';
import * as data from 'test/data';
import Promise from 'metal-promise';
import React from 'react';
import withPropertyGroups from '../WithPropertyGroups';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

const TestComponent = ({propertyGroupsIList}) => (
	<div>
		{propertyGroupsIList.map((attribute, i) => {
			if (attribute) {
				return (
					<div key={i}>
						{attribute.label}

						{attribute.propertySubgroups.map(({properties}, i) => (
							<div
								key={i}
							>{`${attribute.label}-${i}: ${properties.size}`}</div>
						))}
					</div>
				);
			}
		})}
	</div>
);

describe('WithPropertyGroups', () => {
	afterEach(cleanup);

	it('should pass propertyGroups to the WrappedComponent', () => {
		API.fieldMappings.search.mockReturnValueOnce(
			Promise.resolve({
				items: [
					{
						context: 'demographics',
						displayName: 'Individual Value',
						id: '123',
						name: 'Individual val',
						ownerType: 'individual',
						rawType: 'Text',
						type: 'Text'
					}
				],
				total: 1
			})
		);

		API.fieldMappings.search.mockReturnValueOnce(
			Promise.resolve({
				items: [
					{
						context: 'custom',
						displayName: 'Individual Custom',
						id: '123',
						name: 'Individual Custom',
						ownerType: 'individual',
						rawType: 'Text',
						type: 'Text'
					}
				],
				total: 1
			})
		);

		API.fieldMappings.search.mockReturnValueOnce(
			Promise.resolve({
				items: [
					{
						context: 'organization',
						displayName: 'Account Value',
						id: '123',
						name: 'Account Value',
						ownerType: 'account',
						rawType: 'Text',
						type: 'Text'
					}
				],
				total: 1
			})
		);

		API.fieldMappings.search.mockReturnValueOnce(
			Promise.resolve({
				items: [
					{
						context: 'custom',
						displayName: 'Organization Custom',
						id: '123',
						name: 'Organization Custom',
						ownerType: 'organization',
						rawType: 'Text',
						type: 'Text'
					}
				],
				total: 1
			})
		);

		const WrappedComponent = withPropertyGroups(TestComponent);

		const {container} = render(
			<WrappedComponent channelId='123' groupId='123' />
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should not contain certain individual & organization attributes when tokenAuth is false', () => {
		API.channels.fetch.mockReturnValueOnce(
			Promise.resolve(data.mockChannel(1, 0, {tokenAuth: false}))
		);

		API.fieldMappings.search.mockReturnValueOnce(
			Promise.resolve({
				items: [
					{
						context: 'demographics',
						displayName: 'Individual Value',
						id: '123',
						name: 'Individual val',
						ownerType: 'individual',
						rawType: 'Text',
						type: 'Text'
					}
				],
				total: 1
			})
		);

		API.fieldMappings.search.mockReturnValueOnce(
			Promise.resolve({
				items: [
					{
						context: 'custom',
						displayName: 'Individual Custom',
						id: '123',
						name: 'Individual Custom',
						ownerType: 'individual',
						rawType: 'Text',
						type: 'Text'
					}
				],
				total: 1
			})
		);

		API.fieldMappings.search.mockReturnValueOnce(
			Promise.resolve({
				items: [
					{
						context: 'organization',
						displayName: 'Account Value',
						id: '123',
						name: 'Account Value',
						ownerType: 'account',
						rawType: 'Text',
						type: 'Text'
					}
				],
				total: 1
			})
		);

		API.fieldMappings.search.mockReturnValueOnce(
			Promise.resolve({
				items: [
					{
						context: 'custom',
						displayName: 'Organization Custom',
						id: '123',
						name: 'Organization Custom',
						ownerType: 'organization',
						rawType: 'Text',
						type: 'Text'
					}
				],
				total: 1
			})
		);

		const WrappedComponent = withPropertyGroups(TestComponent);

		const {queryByText} = render(
			<WrappedComponent channelId='123' groupId='123' />
		);

		jest.runAllTimers();

		expect(queryByText('Individual Attributes-0: 1')).toBeTruthy();
		expect(queryByText('Individual Attributes-1')).toBeNull();
		expect(queryByText('Organization Attributes')).toBeNull();
	});
});
