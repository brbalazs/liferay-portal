import EventAnalysisToolbar from '../EventAnalysisToolbar';
import React from 'react';
import {Formik} from 'formik';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

jest.mock('react-router-dom', () => ({
	...jest.requireActual('react-router-dom'),
	useParams: () => ({
		channelId: '456',
		groupId: '123'
	})
}));

const WrappedComponent = ({isValid = true}) => (
	<StaticRouter>
		<Formik>
			<EventAnalysisToolbar isValid={isValid} />
		</Formik>
	</StaticRouter>
);

describe('EventAnalysisToolbar', () => {
	it('should render', () => {
		const {container} = render(<WrappedComponent />);

		expect(container).toMatchSnapshot();
	});

	it('should Save Analysis button be Disabled', () => {
		const {getByText} = render(<WrappedComponent isValid={false} />);

		expect(getByText('Save Analysis')).toBeDisabled();
	});
});
