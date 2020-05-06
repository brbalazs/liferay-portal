import FieldPreviewModal from '../FieldPreviewModal';
import Promise from 'metal-promise';
import React from 'react';
import {noop} from 'lodash';
import {shallow} from 'enzyme';

describe('FieldPreviewModal', () => {
	it('should render', () => {
		const component = shallow(
			<FieldPreviewModal
				dataSourceFn={() => Promise.resolve()}
				onClose={noop}
				sourceName={'foo'}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
