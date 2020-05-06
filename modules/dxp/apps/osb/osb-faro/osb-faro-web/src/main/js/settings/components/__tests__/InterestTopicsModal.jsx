import InterestTopicsModal from '../InterestTopicsModal';
import React from 'react';
import {noop} from 'lodash';
import {shallow} from 'enzyme';

describe('InterestTopicsModal', () => {
	it('should render', () => {
		const component = shallow(<InterestTopicsModal onClose={noop} />);
		expect(component).toMatchSnapshot();
	});
});
