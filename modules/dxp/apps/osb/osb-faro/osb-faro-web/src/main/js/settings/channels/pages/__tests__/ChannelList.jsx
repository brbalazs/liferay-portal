import * as data from 'test/data';
import mockStore from 'test/mock-store';
import React from 'react';
import {ChannelList} from '../ChannelList';
import {cleanup, fireEvent, render} from '@testing-library/react';
import {noop} from 'lodash';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router';
import {User} from 'shared/util/records';

jest.unmock('react-dom');

const defaultProps = {
	addAlert: noop,
	close: noop,
	currentUser: new User(data.mockUser()),
	groupId: '23',
	open: noop
};

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<ChannelList {...defaultProps} {...props} />
		</StaticRouter>
	</Provider>
);

describe('Channels List', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should run open function after click on add property button', () => {
		const spy = jest.fn();

		const {getByTestId} = render(<DefaultComponent open={spy} />);

		fireEvent.click(getByTestId('addproperty-button'));

		expect(spy).toBeCalled();
	});

	it('should not render add button if user is not an admin', () => {
		const {queryByText} = render(
			<DefaultComponent currentUser={new User(data.mockMemberUser())} />
		);

		expect(queryByText('New Property')).toBeNull();
	});
});
