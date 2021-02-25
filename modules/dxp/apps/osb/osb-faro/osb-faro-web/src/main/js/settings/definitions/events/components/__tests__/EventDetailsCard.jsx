import EventDetailsCard from '../EventDetailsCard';
import mockStore from 'test/mock-store';
import React from 'react';
import {fireEvent, render} from '@testing-library/react';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('EventDetailsCard', () => {
	it('should render', () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<EventDetailsCard eventName='viewArticle' groupId='23' />
				</StaticRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});

	it('should match the displayed code with the selected attributes', () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<EventDetailsCard eventName='viewArticle' groupId='23' />
				</StaticRouter>
			</Provider>
		);

		fireEvent.click(container.querySelector('.clickable'));

		expect(container.querySelector('.copy-button')).toHaveAttribute(
			'data-clipboard-text',
			[
				"Analytics.track('viewArticle', {",
				"\n\t'testingtest': '2',",
				"\n\t'anothernamet': '3',",
				'\n});'
			].join('')
		);
	});
});
