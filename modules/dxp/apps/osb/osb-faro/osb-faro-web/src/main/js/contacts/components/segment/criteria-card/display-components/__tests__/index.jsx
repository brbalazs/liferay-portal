import DisplayComponent from '../index';
import React from 'react';
import {shallow} from 'enzyme';

describe('DisplayComponent', () => {
	it.each`
		propertyKey     | displayName
		${'account'}    | ${'AccountDisplay'}
		${'session'}    | ${'SessionDisplay'}
		${'interest'}   | ${'InterestDisplay'}
		${'web'}        | ${'BehaviorDisplay'}
		${'individual'} | ${'IndividualDisplay'}
	`('renders $displayName for $propertyKey', ({displayName, propertyKey}) => {
		const component = shallow(
			<DisplayComponent property={{propertyKey}} />
		);

		expect(component.name()).toBe(displayName);
	});
});
