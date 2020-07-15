import Form from 'shared/components/form';
import Interactions from '../Interactions';
import React from 'react';
import {jobRunDataPeriods} from 'shared/util/constants';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Interactions', () => {
	it('should render', () => {
		const {container} = render(
			<Form
				initialValues={{
					includePreviousPeriod: false,
					runDataPeriod: jobRunDataPeriods.last30Days
				}}
			>
				{() => (
					<Form.Form>
						<Interactions />
					</Form.Form>
				)}
			</Form>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render with Include Previous Period Checkbox Checked', () => {
		const {queryByTestId} = render(
			<Form
				initialValues={{
					includePreviousPeriod: true,
					runDataPeriod: jobRunDataPeriods.last30Days
				}}
			>
				{() => (
					<Form.Form>
						<Interactions />
					</Form.Form>
				)}
			</Form>
		);

		const checkbox = queryByTestId('include-previous-period-checkbox');

		expect(checkbox.checked).toBeTrue();
	});
});
