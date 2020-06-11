import Form from 'shared/components/form';
import Items from '../Items';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Items', () => {
	it('should render', () => {
		const {container} = render(
			<Form
				initialValues={{
					itemFilters: [
						{
							count: 12,
							id: "includeFilter - og:title ~ ''blog*''",
							name: 'includeFilter',
							value: "og:title ~ ''blog*''"
						},
						{
							count: 5,
							id: 'includeFilter - https://www.google.com',
							name: 'includeFilter',
							value: 'https://www.google.com'
						}
					]
				}}
			>
				{({values: {itemFilters}}) => (
					<Form.Form>
						<Items itemFilters={itemFilters} />
					</Form.Form>
				)}
			</Form>
		);

		expect(container).toMatchSnapshot();
	});
});
