import HelpWidgetModal from '../index';
import React from 'react';
import {noop} from 'lodash';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('HelpWidgetModal', () => {
	it('Should render', () => {
		const {container} = render(<HelpWidgetModal onClose={noop} />);

		expect(container).toMatchSnapshot();
	});
});
