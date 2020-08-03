import Promise from 'metal-promise';
import React from 'react';
import SelectItemsModal, {ItemComponent} from '../SelectItemsModal';
import {cleanup, fireEvent, render} from '@testing-library/react';
import {noop} from 'lodash';

jest.unmock('react-dom');

const ITEM_COMPONENT_MOCK = {
	name: 'test'
};

const MESSAGE = 'message';

const SELECTED_ITEMS_MOCK = [{id: 1, name: 'test1'}, {id: 2, name: 'test2'}];

describe('SelectItemsModal', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<SelectItemsModal
				dataSourceFn={() => Promise.resolve()}
				groupId='23'
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render with a custom title', () => {
		const {container} = render(
			<SelectItemsModal
				dataSourceFn={() => Promise.resolve()}
				groupId='23'
				title='Custom Title'
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render with a custom submit message', () => {
		const {container} = render(
			<SelectItemsModal
				dataSourceFn={() => Promise.resolve()}
				groupId='23'
				submitMessage='Custom Submit Message'
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render items as an entity list', () => {
		const {container} = render(
			<SelectItemsModal
				dataSourceFn={() => Promise.resolve()}
				entityType={4}
				groupId='23'
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render without the sort button', () => {
		const {container} = render(
			<SelectItemsModal
				dataSourceFn={() => Promise.resolve()}
				groupId='23'
				showSortButton={false}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render without a toolbar', () => {
		const {container} = render(
			<SelectItemsModal
				dataSourceFn={() => Promise.resolve()}
				groupId='23'
				showToolbar={false}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render with selectedItems', () => {
		const {container} = render(
			<SelectItemsModal
				dataSourceFn={() => Promise.resolve()}
				groupId='23'
				selectedItems={SELECTED_ITEMS_MOCK}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render with selectedItems with the disable selected datasource', () => {
		const {container} = render(
			<SelectItemsModal
				dataSourceFn={() => Promise.resolve()}
				disabledSelectedDataSourceFn={() =>
					Promise.resolve({items: SELECTED_ITEMS_MOCK})
				}
				groupId='23'
			/>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render with a successful submit', () => {
		const {container, getByText} = render(
			<SelectItemsModal
				dataSourceFn={() => Promise.resolve()}
				groupId='23'
				onClose={noop}
				onSubmit={() => Promise.resolve()}
				requireSelection={false}
				submitMessage={MESSAGE}
			/>
		);

		fireEvent.click(getByText(MESSAGE));
		jest.runAllTimers();
		expect(container).toMatchSnapshot();
	});

	it('should render with an unsuccessful submit', () => {
		const {container, getByText} = render(
			<SelectItemsModal
				dataSourceFn={() => Promise.resolve()}
				groupId='23'
				onClose={noop}
				onSubmit={() =>
					Promise.reject({IS_CANCELLATION_ERROR: 'error'})
				}
				requireSelection={false}
				submitMessage={MESSAGE}
			/>
		);

		fireEvent.click(getByText(MESSAGE));
		jest.runAllTimers();
		expect(container).toMatchSnapshot();
	});

	it('should render with a blank submit', () => {
		const {container, getByText} = render(
			<SelectItemsModal
				dataSourceFn={() => Promise.resolve()}
				groupId='23'
				onClose={noop}
				onSubmit={noop({IS_CANCELLATION_ERROR: 'error'})}
				requireSelection={false}
				submitMessage={MESSAGE}
			/>
		);

		fireEvent.click(getByText(MESSAGE));
		jest.runAllTimers();
		expect(container).toMatchSnapshot();
	});

	describe('ItemComponent', () => {
		afterEach(cleanup);

		it('should render', () => {
			const {container} = render(
				<ItemComponent item={ITEM_COMPONENT_MOCK} />
			);

			expect(container).toMatchSnapshot();
		});

		it('should render with a custom className', () => {
			const {container} = render(
				<ItemComponent className='test' item={ITEM_COMPONENT_MOCK} />
			);

			expect(container).toMatchSnapshot();
		});
	});
});
