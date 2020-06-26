import FieldDropDown from '../FieldDropDown';
import React from 'react';
import {Map} from 'immutable';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('FieldDropDown', () => {
	
	it('should render', () => {
		const {container} = render(
			<FieldDropDown dataIMap={new Map()} searchItems={[]} />
		);

		expect(container).toMatchSnapshot();
	});

	it('should render with a title', () => {
		const {container} = render(
			<FieldDropDown
				dataIMap={new Map()}
				searchItems={[]}
				title={'FOO BAR'}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
