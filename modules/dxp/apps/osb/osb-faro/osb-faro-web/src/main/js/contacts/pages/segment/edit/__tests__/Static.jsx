import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import Form from 'shared/components/form';
import React from 'react';
import {Changeset, Segment} from 'shared/util/records';
import {INDIVIDUALS} from 'shared/util/router';
import {Map} from 'immutable';
import {shallow} from 'enzyme';
import {StaticSegmentEdit} from '../Static';

const {segmentTypes} = FaroConstants;

describe('StaticSegmentEdit', () => {
	it('should render', () => {
		const component = shallow(
			<StaticSegmentEdit groupId='23' type={INDIVIDUALS} />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render a navigation warning', () => {
		const component = shallow(
			<StaticSegmentEdit
				groupId='23'
				segment={data.getImmutableMock(Segment, data.mockSegment, 1, {
					segmentType: segmentTypes.static
				})}
				type={INDIVIDUALS}
			/>
		);

		component.setState({
			changeset: new Changeset({added: new Map({test: {}})})
		});

		expect(component.find(Form).shallow()).toMatchSnapshot();
	});

	it('should not render a navigation warning after being changed back to the original value', () => {
		const component = shallow(
			<StaticSegmentEdit groupId='23' type={INDIVIDUALS} />
		);

		component.setState({
			changeset: new Changeset({added: new Map({test: {}})})
		});

		expect(component.find(Form).shallow()).toMatchSnapshot();

		component.setState({changeset: new Changeset()});

		expect(component.find(Form).shallow()).toMatchSnapshot();
	});
});
