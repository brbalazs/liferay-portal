import CSVPreviewModal from '../CSVPreviewModal';
import React from 'react';
import {noop} from 'lodash';
import {shallow} from 'enzyme';

describe('CSVPreviewModal', () => {
	it('should render', () => {
		const component = shallow(
			<CSVPreviewModal
				fileName='test'
				groupId='23'
				id='test'
				onClose={noop}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});
});
