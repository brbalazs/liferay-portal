import FieldPreviewModal from '../FieldPreviewModal';
import Promise from 'metal-promise';
import React from 'react';
import {noop} from 'lodash';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('FieldPreviewModal', () => {
	it('should render', () => {
		const {container} = render(
			<FieldPreviewModal
				dataSourceFn={() => Promise.resolve()}
				onClose={noop}
				sourceName={'foo'}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
