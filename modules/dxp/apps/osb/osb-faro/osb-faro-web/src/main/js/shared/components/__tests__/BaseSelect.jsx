import BaseSelect, {Item} from '../BaseSelect';
import Promise from 'metal-promise';
import React from 'react';
import {
	fireEvent,
	render,
	waitForElementToBeRemoved
} from '@testing-library/react';
import {noop} from 'lodash';

jest.unmock('react-dom');

describe('BaseSelect', () => {
	it('should render', () => {
		const {container} = render(
			<BaseSelect
				dataSourceFn={() => Promise.resolve([])}
				itemRenderer={jest.fn()}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render as disabled', () => {
		const dataSourceFn = jest.fn();

		const {container} = render(
			<BaseSelect
				dataSourceFn={dataSourceFn}
				disabled
				itemRenderer={({name}) => name}
			/>
		);

		jest.runAllTimers();

		expect(dataSourceFn).not.toHaveBeenCalled();

		fireEvent.click(container.querySelector('.input-group'));

		jest.runAllTimers();

		expect(dataSourceFn).not.toHaveBeenCalled();

		expect(container).toMatchSnapshot();
	});

	it('should render w/ selectedItem', () => {
		const {container} = render(
			<BaseSelect
				dataSourceFn={() =>
					Promise.resolve([
						{name: 'test'},
						{name: 'foo'},
						{name: 'bar'}
					])
				}
				itemRenderer={({name}) => name}
				onFocus={noop}
				selectedItem={{name: 'foo'}}
			/>
		);

		jest.runAllTimers();

		expect(
			container.querySelector('.selected-item-container').innerHTML
		).toEqual('foo');
	});

	it('should fetch items with focus', async () => {
		const {container} = render(
			<BaseSelect
				dataSourceFn={() => Promise.resolve([{name: 'test'}])}
				focusOnInit
				itemRenderer={({name}) => name}
				onFocus={noop}
			/>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		).then(() => {
			const dropdownMenu = document.body.getElementsByClassName(
				'dropdown-root'
			)[0];

			expect(dropdownMenu).toMatchSnapshot();
		});
	});

	it('should render w/ menu title', async () => {
		const {container} = render(
			<BaseSelect
				dataSourceFn={() => Promise.resolve([{name: 'test'}])}
				focusOnInit
				itemRenderer={({name}) => name}
				menuTitle='Test Menu Title'
				onFocus={noop}
			/>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		).then(() => {
			const dropdownMenu = document.body.getElementsByClassName(
				'dropdown-root'
			)[0];

			expect(
				dropdownMenu.getElementsByClassName('dropdown-header')[0]
					.innerHTML
			).toEqual('Test Menu Title');
		});
	});

	it('should focus on the previous item', async () => {
		const {container} = render(
			<BaseSelect
				dataSourceFn={() =>
					Promise.resolve([
						{name: 'test'},
						{name: 'foo'},
						{name: 'bar'}
					])
				}
				focusOnInit
				itemRenderer={({name}) => name}
				onFocus={noop}
			/>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		).then(async () => {
			const dropdownMenu = document.body.getElementsByClassName(
				'dropdown-root'
			)[0];

			fireEvent.keyDown(container.querySelector('.input-root'), {
				key: 'ArrowUp',
				keyCode: 38
			});

			expect(
				dropdownMenu.getElementsByClassName('active')[0].innerHTML
			).toEqual('bar');
		});
	});

	it('should focus on the next item', async () => {
		const {container} = render(
			<BaseSelect
				dataSourceFn={() =>
					Promise.resolve([
						{name: 'test'},
						{name: 'foo'},
						{name: 'bar'}
					])
				}
				focusOnInit
				itemRenderer={({name}) => name}
				onFocus={noop}
			/>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		).then(async () => {
			const dropdownMenu = document.body.getElementsByClassName(
				'dropdown-root'
			)[0];

			fireEvent.keyDown(container.querySelector('.input-root'), {
				key: 'ArrowDown',
				keyCode: 40
			});

			expect(
				dropdownMenu.getElementsByClassName('active')[0].innerHTML
			).toEqual('foo');
		});
	});
});

describe('Item', () => {
	it('should render', () => {
		const {container} = render(
			<Item item={{name: 'test'}} itemRenderer={({name}) => name} />
		);

		expect(container).toMatchSnapshot();
	});

	it('should select an item', () => {
		const {container, getByText} = render(
			<Item
				item={{name: 'test'}}
				itemRenderer={({name}) => name}
				onSelect={noop}
			/>
		);

		fireEvent.click(getByText('test'));
		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
