import React from 'react';
import StagedSubnav from '../StagedSubnav';
import {shallow} from 'enzyme';

describe('StagedSubnav', () => {
	it('should render', () => {
		const component = shallow(
			<StagedSubnav
				viewCurrentLinkText={'view current items'}
				viewStagedLinkText={'view added items'}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with the showStaged active labels', () => {
		const component = shallow(
			<StagedSubnav
				showStaged
				viewCurrentLinkText={'view current items'}
				viewStagedLinkText={'view added items'}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
