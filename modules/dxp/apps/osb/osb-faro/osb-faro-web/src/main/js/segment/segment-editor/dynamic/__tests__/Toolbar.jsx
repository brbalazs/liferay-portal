import * as API from 'shared/api';
import * as data from 'test/data';
import React from 'react';
import {
	cleanup,
	render,
	waitForElementToBeRemoved
} from '@testing-library/react';
import {Formik} from 'formik';
import {StaticRouter} from 'react-router';
import {Toolbar} from '../Toolbar';

jest.unmock('react-dom');

describe('Toolbar', () => {
	afterEach(() => {
		jest.clearAllMocks();

		cleanup();
	});

	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<Formik>
					<Toolbar
						channelId='321'
						criteria={data.mockNewCriteria(1, {valid: false})}
						groupId='123'
					/>
				</Formik>
			</StaticRouter>
		);
		expect(container).toMatchSnapshot();
	});

	it('should render w/ preview button disabled if criteria is valid', () => {
		const {getByTestId} = render(
			<StaticRouter>
				<Formik>
					<Toolbar
						channelId='321'
						criteria={data.mockNewCriteria(1, {valid: true})}
						groupId='123'
					/>
				</Formik>
			</StaticRouter>
		);

		expect(getByTestId('preview-criteria-button')).toBeDisabled();
	});

	it('should render w/ preview button disabled if total members count is bigger thant 0', () => {
		const {getByTestId} = render(
			<StaticRouter>
				<Formik>
					<Toolbar
						channelId='321'
						criteria={data.mockNewCriteria(1, {valid: false})}
						groupId='123'
					/>
				</Formik>
			</StaticRouter>
		);

		expect(getByTestId('preview-criteria-button')).toBeDisabled();
	});

	it('should render w/ preview button enabled if total members count is bigger than 0 and criteria is valid', async () => {
		API.individuals.search.mockReturnValue(Promise.resolve({total: 1}));

		const {container, getByTestId} = render(
			<StaticRouter>
				<Formik>
					<Toolbar
						channelId='321'
						criteria={data.mockNewCriteria(1, {valid: true})}
						groupId='123'
						valid
					/>
				</Formik>
			</StaticRouter>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(getByTestId('preview-criteria-button')).toBeEnabled();
	});
});
