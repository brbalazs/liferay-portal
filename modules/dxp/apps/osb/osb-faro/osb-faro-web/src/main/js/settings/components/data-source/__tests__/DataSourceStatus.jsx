import DataSourceStatus from '../DataSourceStatus';
import React from 'react';
import {shallow} from 'enzyme';

describe('DataSourceStatus', () => {
	it('should render', () => {
		const component = shallow(
			<DataSourceStatus display={'info'} label={'foo'} message={'bar'} />
		);
		expect(component).toMatchSnapshot();
	});
});
