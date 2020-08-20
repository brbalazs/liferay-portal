import * as data from 'test/data';
import AddWorkspaceForm, {
	emailDomainValidation,
	emailDomainValidationArr
} from '../AddWorkspaceForm';
import mockStore from 'test/mock-store';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {Project} from 'shared/util/records';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('AddWorkspaceForm', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<AddWorkspaceForm />
				</StaticRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render the edit version', () => {
		const {getByTestId, queryByText} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<AddWorkspaceForm
						editing
						project={data.getImmutableMock(
							Project,
							data.mockProject
						)}
					/>
				</StaticRouter>
			</Provider>
		);

		expect(
			queryByText('You can only set your friendly workspace url once')
		).toBeNull();

		expect(queryByText('Save')).not.toBeNull();
		expect(getByTestId('server-location-input')).toBeDisabled();
	});

	it('should disable friendlyURL input if friendlyURL exists on Project', () => {
		const {getByTestId} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<AddWorkspaceForm
						project={data.getImmutableMock(
							Project,
							data.mockProject,
							1,
							{friendlyURL: 'foo'}
						)}
					/>
				</StaticRouter>
			</Provider>
		);

		expect(getByTestId('friendly-url-input')).toBeDisabled();
	});

	describe('emailDomainValidation', () => {
		it.each`
			domain                     | isValid
			${'liferay.com'}           | ${true}
			${'test@liferay.com'}      | ${false}
			${'liferay.com.'}          | ${false}
			${'liferay.com(JoeSmith)'} | ${false}
			${'111.222.333.444'}       | ${false}
			${'[123.123.123.123]'}     | ${false}
		`(
			'should return whether the email domain is considered valid',
			({domain, isValid}) => {
				const result = emailDomainValidation(domain);

				expect(result).toEqual(isValid);
			}
		);

		it('should return an empty string if there are valid email domains', () => {
			expect(
				emailDomainValidationArr(['liferay.com.br', 'liferay.com'])
			).toEqual('');
		});

		it('should return error message when is not validated email domain', () => {
			expect(
				emailDomainValidationArr(['test@liferay.com', 'liferay.com'])
			).toEqual('Please enter the domain in this format: domain.com');
		});
	});
});
