import CSVPreviewModal from '../CSVPreviewModal';
import React from 'react';
import {noop} from 'lodash';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('CSVPreviewModal', () => {
	it('should render', () => {
		const {container} = render(
			<CSVPreviewModal
				fileName='test'
				groupId='23'
				id='test'
				onClose={noop}
			/>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
