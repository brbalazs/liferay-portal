import * as data from 'test/data';
import CreatedByCell from '../CreatedBy';
import React from 'react';
import {shallow} from 'enzyme';

describe('CreatedByCell', () => {
	it('should render', () => {
		const component = shallow(
			<CreatedByCell
				data={{
					dateModified: data.getTimestamp(),
					userName: 'Test Test'
				}}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
