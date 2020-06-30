import ManuallyRetrainModelModal from '../ManuallyRetrainModelModal';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('ManuallyRetrainModelModal', () => {
	it('should render', async() => {
		const {container} = render(<ManuallyRetrainModelModal />);

		expect(container).toMatchSnapshot();
	});
});
