import * as data from 'test/data';
import AssociatedSegmentsCard from '../AssociatedSegmentsCard';
import Promise from 'metal-promise';
import React from 'react';
import {shallow} from 'enzyme';

describe('AssociatedSegmentsCard', () => {
	it('should render', () => {
		const component = shallow(
			<AssociatedSegmentsCard
				dataSourceFn={() =>
					Promise.resolve(data.mockSearch(data.mockSegment, 2))
				}
				groupId={'23'}
				id={'123'}
				pageUrl={'/foo'}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render w/ loading overlay', () => {
		const component = shallow(
			<AssociatedSegmentsCard
				dataSourceFn={() => Promise.resolve({})}
				groupId={'23'}
				id={'123'}
				pageUrl={'/foo'}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with an error display', () => {
		const component = shallow(
			<AssociatedSegmentsCard
				dataSourceFn={() => Promise.reject({})}
				groupId={'23'}
				id={'123'}
				pageUrl={'/foo'}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render with an no results display', () => {
		const component = shallow(
			<AssociatedSegmentsCard
				dataSourceFn={() =>
					Promise.resolve(data.mockSearch(data.mockSegment, 0))
				}
				groupId={'23'}
				id={'123'}
				pageUrl={'/foo'}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});
});
