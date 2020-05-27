import IndividualAttributeModal from '../IndividualAttributesModal';
import React from 'react';
import {mockIndividualAttributes} from 'test/data';
import {noop} from 'lodash';
import {render} from '@testing-library/react';

const {dataSources, fieldName} = mockIndividualAttributes();

describe('IndividualAttributeModal', () => {
	it('should render', () => {
		const {container} = render(
			<IndividualAttributeModal
				dataSources={dataSources}
				fieldName={fieldName}
				onClose={noop}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
