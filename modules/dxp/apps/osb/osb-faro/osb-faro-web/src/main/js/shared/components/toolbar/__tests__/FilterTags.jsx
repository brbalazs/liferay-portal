import FilterTags from '../FilterTags';
import React from 'react';
import {range} from 'lodash';
import {shallow} from 'enzyme';

describe('FilterTags', () => {
	it('should render a list of tags', () => {
		const component = shallow(
			<FilterTags
				tags={range(3).map(i => ({
					key: `foo${i}`,
					label: `Foo Label${i}`,
					value: `foo-value${i}`
				}))}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
