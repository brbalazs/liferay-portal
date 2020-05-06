import FieldDropDown from '../FieldDropDown';
import React from 'react';
import {Map} from 'immutable';
import {shallow} from 'enzyme';

describe('FieldDropDown', () => {
	it('should render', () => {
		const component = shallow(
			<FieldDropDown dataIMap={new Map()} searchItems={[]} />
		);
		expect(component).toMatchSnapshot();
	});

	it('should render with a title', () => {
		const component = shallow(
			<FieldDropDown
				dataIMap={new Map()}
				searchItems={[]}
				title={'FOO BAR'}
			/>
		);
		expect(component).toMatchSnapshot();
	});
});
