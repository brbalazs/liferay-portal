import CreateMappingModal from '../CreateMappingModal';
import React from 'react';
import {noop} from 'lodash';
import {shallow} from 'enzyme';

describe('CreateMappingModal', () => {
	it('should render', () => {
		const component = shallow(
			<CreateMappingModal groupId='23' onClose={noop} />
		);
		expect(component).toMatchSnapshot();
	});

	it('should call onSubmit during handleSubmit', () => {
		const onSubmit = jest.fn();
		const component = shallow(
			<CreateMappingModal
				groupId='23'
				onClose={noop}
				onSubmit={onSubmit}
			/>
		);
		component.instance().handleSubmit({type: 'foo'});
		jest.runAllTimers();
		expect(onSubmit).toBeCalled();
	});
});
