import * as data from 'test/data';
import React from 'react';
import {FormSelectFieldInput} from '../SelectFieldInput';
import {shallow} from 'enzyme';

describe('SelectFieldInput', () => {
	it('should render', () => {
		const component = shallow(
			<FormSelectFieldInput
				field={{name: 'foo'}}
				form={data.mockForm()}
				groupId={'23'}
				name={'foo'}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with a label', () => {
		const component = shallow(
			<FormSelectFieldInput
				field={{name: 'foo'}}
				form={data.mockForm()}
				groupId={'23'}
				label={'bar'}
				name={'foo'}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
