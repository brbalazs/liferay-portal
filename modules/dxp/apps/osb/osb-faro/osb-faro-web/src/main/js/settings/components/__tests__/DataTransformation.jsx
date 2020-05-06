import * as API from 'shared/api';
import Promise from 'metal-promise';
import React from 'react';
import {DataTransformation, processFieldMappings} from '../DataTransformation';
import {fromJS} from 'immutable';
import {mockFieldMapping, mockMapping} from 'test/data';
import {shallow} from 'enzyme';

const defaultProps = {
	groupId: '23',
	id: '123',
	onSubmit: jest.fn()
};

describe('processFieldMappings', () => {
	it('should return fieldMappings', () => {
		const foo = 'foo';
		const bar = 'bar';
		const baz = 'baz';

		const inputValue = fromJS([
			{source: {name: foo}, suggestion: {}},
			{source: {name: bar}, suggestion: {}},
			{source: {name: baz}, suggestion: {}}
		]);

		const result = processFieldMappings(inputValue);

		expect(result.length).toEqual(3);

		expect(result).toMatchSnapshot();
	});
});

describe('DataTransformation', () => {
	it('should render', () => {
		const component = shallow(<DataTransformation {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ the done button enabled', () => {
		API.dataSource.fetchMappings.mockReturnValue(
			Promise.resolve([
				mockMapping('Matched Field', {
					suggestions: [mockFieldMapping()]
				})
			])
		);

		const component = shallow(<DataTransformation {...defaultProps} />);

		expect(component.find('FormNavigation').prop('enableNext')).toBe(false);

		jest.runAllTimers();

		expect(component.find('FormNavigation').prop('enableNext')).toBe(true);
	});

	it('should render w/ a mapped field', () => {
		API.dataSource.fetchMappings.mockReturnValue(
			Promise.resolve([
				mockMapping('Matched Field', {
					suggestions: [mockFieldMapping()]
				}),
				mockMapping('Unmatched Field')
			])
		);

		const component = shallow(<DataTransformation {...defaultProps} />);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should hide mapped fields', () => {
		API.dataSource.fetchMappings.mockReturnValue(
			Promise.resolve([
				mockMapping('Matched Field', {
					suggestions: [mockFieldMapping()]
				}),
				mockMapping('Unmatched Field')
			])
		);

		const component = shallow(<DataTransformation {...defaultProps} />);

		component.setState({hideMappedFields: true});

		jest.runAllTimers();

		expect(
			component.find('DataTransformationList').prop('hideMappedFields')
		).toBe(true);
	});

	it('should hide unmatched fields', () => {
		API.dataSource.fetchMappings.mockReturnValue(
			Promise.resolve([
				mockMapping('Has default match 1', {
					suggestions: [mockFieldMapping(null, {name: 'foo'})]
				}),
				mockMapping('Has default match 2', {
					suggestions: [mockFieldMapping(null, {name: 'bar'})]
				}),
				mockMapping('No default match')
			])
		);

		const component = shallow(
			<DataTransformation {...defaultProps} showUnmatchedFields={false} />
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render w/ the done button disabled if there are duplicate SCV field mappings', () => {
		const component = shallow(<DataTransformation {...defaultProps} />);

		const mockSuggestion = {
			suggestion: {name: 'additionalName', value: 'foo'}
		};

		jest.runAllTimers();

		component.setState({
			fieldsIList: fromJS([
				{
					source: {name: 'first_name', value: 'bar'},
					...mockSuggestion
				},
				{
					source: {name: 'nick_name', value: 'baz'},
					...mockSuggestion
				}
			])
		});

		jest.runAllTimers();

		expect(component.find('FormNavigation').prop('enableNext')).toBe(false);
	});
});
