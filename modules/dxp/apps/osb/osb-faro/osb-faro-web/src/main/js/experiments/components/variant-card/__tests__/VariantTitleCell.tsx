import Label from 'shared/components/Label';
import React from 'react';
import VariantTitle from '../VariantTitleCell';
import {shallow} from 'enzyme';

describe('VariantTitle', () => {
	let component;

	afterEach(() => {
		if (component) {
			component.unmount();
		}
	});

	it('should render VariantTitle', () => {
		component = shallow(<VariantTitle title='Variant Title' />);

		expect(component).toMatchSnapshot();
	});

	it('should render label component when a label prop is set', () => {
		component = shallow(
			<VariantTitle label='winner' title='Variant Title' />
		);

		expect(component.exists(Label)).toBe(true);
	});

	it('should render truncated text when it is a big title', () => {
		component = shallow(
			<VariantTitle
				label='winner'
				title='Variant Title with a big title should be truncated'
			/>
		);

		expect(component.exists('.text-truncate')).toBe(true);
	});
});
