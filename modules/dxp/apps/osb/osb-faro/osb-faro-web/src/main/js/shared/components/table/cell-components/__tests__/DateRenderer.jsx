import DateRenderer from '../DateRenderer';
import moment from 'moment';
import React from 'react';
import {getTimestamp} from 'test/data';
import {shallow} from 'enzyme';

describe('DateRenderer', () => {
	it('should render', () => {
		const component = shallow(
			<DateRenderer
				data={{
					dateCreated: getTimestamp()
				}}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with date provided in the datePath String', () => {
		const component = shallow(
			<DateRenderer
				data={{
					dateAdded: getTimestamp(),
					dateCreated: 0
				}}
				datePath='dateAdded'
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with date provided in the datePath Array', () => {
		const component = shallow(
			<DateRenderer
				data={{
					properties: {
						dateAdded: getTimestamp(),
						dateCreated: 0
					}
				}}
				datePath={['properties', 'dateAdded']}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should use a custom date formatter', () => {
		const component = shallow(
			<DateRenderer
				data={{
					dateCreated: getTimestamp()
				}}
				dateFormatter={date => moment(date).format('YYYY MMMM DD')}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
