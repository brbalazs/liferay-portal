import Dropdown, {DropdownItem} from '../Dropdown';
import React from 'react';
import {
	fireEvent,
	getByText as getByTextInElement,
	render
} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

const MOCK_ITEMS = [
	{
		label: 'Item 1',
		value: 'item1'
	},
	{
		label: 'Item 2',
		value: 'item2'
	}
];

const DefaultDropdown = props => <Dropdown items={MOCK_ITEMS} {...props} />;

const DefaultDropdownItem = props => <DropdownItem label='Item 1' {...props} />;

describe('Dropdown', () => {
	it('should render', () => {
		const {container} = render(<DefaultDropdown />);

		expect(container).toMatchSnapshot();
	});

	it('should render dropdown with items', async() => {
		const {baseElement, container} = render(<DefaultDropdown />);

		fireEvent.click(container.querySelector('.button-root'));

		MOCK_ITEMS.forEach(({label}) => {
			expect(
				getByTextInElement(
					baseElement.querySelector('.dropdown-menu'),
					label
				)
			).toBeTruthy();
		});
	});

	it('should render dropdown with a selected value', () => {
		const {getByText} = render(
			<DefaultDropdown value={MOCK_ITEMS[1].value} />
		);

		expect(getByText(MOCK_ITEMS[1].label)).toBeTruthy();
	});

	it('should render a disabled dropdown', () => {
		const {container} = render(<DefaultDropdown disabled />);

		expect(container.querySelector('.button-root')).toBeDisabled();
	});
});

describe('Dropdown Item', () => {
	it('should render', () => {
		const {container} = render(<DefaultDropdownItem />);

		expect(container).toMatchSnapshot();
	});

	it('should render as active', () => {
		const {container} = render(<DefaultDropdownItem active />);

		expect(container.querySelector('.dropdown-item')).toHaveClass('active');
	});

	it('should render as disabled', () => {
		const {container} = render(<DefaultDropdownItem disabled />);

		expect(container.querySelector('.dropdown-item')).toHaveClass(
			'disabled'
		);
	});

	it('should render w/ description', () => {
		const {container} = render(
			<DefaultDropdownItem description='Description' />
		);

		expect(
			container.querySelector('.analytics-dropdown-description')
		).toBeTruthy();
	});

	it('should render w/ separator', () => {
		const {container} = render(<DropdownItem separator />);

		expect(container.querySelector('.dropdown-divider')).toBeTruthy();
	});

	it('should render w/ icon', () => {
		const {container} = render(<DefaultDropdownItem icon='plus' />);

		expect(container.querySelector('.lexicon-icon-plus')).toBeTruthy();
	});

	it('should render w/ Link', () => {
		const {container} = render(
			<StaticRouter>
				<DefaultDropdownItem href='/touchpoints/123' />
			</StaticRouter>
		);

		expect(container.querySelector('.dropdown-item')).toHaveAttribute(
			'href',
			'/touchpoints/123'
		);
	});
});
