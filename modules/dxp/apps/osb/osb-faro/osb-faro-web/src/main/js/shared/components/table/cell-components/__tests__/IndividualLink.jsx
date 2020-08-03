import IndividualLinkCell from '../IndividualLink';
import React from 'react';
import {shallow} from 'enzyme';

describe('IndividualLinkCell', () => {
	it('should render', () => {
		const component = shallow(
			<IndividualLinkCell
				data={{
					emailAddress: 'foo456@email',
					id: '456',
					name: 'Test Test'
				}}
				groupId='123'
			/>
		).shallow();

		expect(component).toMatchSnapshot();
	});

	it('should render with individual data', () => {
		const component = shallow(
			<IndividualLinkCell
				data={{
					individualDeleted: false,
					individualEmail: 'foo456@email',
					individualId: 'individual456',
					individualName: 'individual Test'
				}}
				groupId='123'
			/>
		).shallow();

		expect(component).toMatchSnapshot();
	});

	it('should NOT render as a link if the individual was deleted', () => {
		const component = shallow(
			<IndividualLinkCell
				data={{
					individualDeleted: true,
					individualEmail: 'foo456@email',
					individualId: 'individual456',
					individualName: 'individual Test'
				}}
				groupId='123'
			/>
		).shallow();

		expect(component.find('Link').length).toEqual(0);
	});

	it('should NOT render as a link if the individual is anonymous', () => {
		const component = shallow(
			<IndividualLinkCell
				data={{
					individualDeleted: true,
					individualId: 'individual456',
					individualName: 'individual Test'
				}}
				groupId='123'
			/>
		).shallow();

		expect(component.find('Link').length).toEqual(0);
	});

	it('should render with individualId in the link', () => {
		const individualId = 'individual456';

		const component = shallow(
			<IndividualLinkCell
				data={{
					id: 'id123',
					individualDeleted: false,
					individualEmail: 'foo456@email',
					individualId,
					individualName: 'individual Test'
				}}
				groupId='123'
			/>
		);

		expect(
			component
				.shallow()
				.find('Link')
				.prop('to')
		).toContain(individualId);
	});
});
