import BaseSelect, {Item} from '../BaseSelect';
import Promise from 'metal-promise';
import React from 'react';
import {cleanup, fireEvent, render} from '@testing-library/react';
import {noop} from 'lodash';

jest.unmock('react-dom');

describe('BaseSelect', () => {
	afterEach(cleanup);

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

	it('should fetch items with focus', () => {
		const {container} = render(
			<BaseSelect
				dataSourceFn={() => Promise.resolve([])}
				focusOnInit
				itemRenderer={({name}) => name}
				onFocus={noop}
				selectedItem={{name: 'test'}}
			/>
		);

		fireEvent.click(container.querySelector('.input-group'));

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should focus on the selected item', () => {
		const {container} = render(
			<BaseSelect
				dataSourceFn={() => Promise.resolve([])}
				focusOnInit
				itemRenderer={({name}) => name}
				onFocus={noop}
				selectedItem={{name: 'test'}}
			/>
		);

		fireEvent.keyDown(container.querySelector('.input-root'), {
			key: 'Enter',
			keyCode: 13
		});

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should defocus the selected item', () => {
		const {container} = render(
			<BaseSelect
				dataSourceFn={() => Promise.resolve([])}
				focusOnInit
				itemRenderer={({name}) => name}
				onFocus={noop}
				selectedItem={{name: 'test'}}
			/>
		);

		fireEvent.keyDown(container.querySelector('.input-root'));

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should focus on the previous item', () => {
		const {container} = render(
			<BaseSelect
				dataSourceFn={() => Promise.resolve([])}
				focusOnInit
				itemRenderer={({name}) => name}
				onFocus={noop}
				selectedItem={{name: 'test'}}
			/>
		);

		fireEvent.keyDown(container.querySelector('.input-root'), {
			key: 'ArrowUp',
			keyCode: 38
		});

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should focus on the next item', () => {
		const {container} = render(
			<BaseSelect
				dataSourceFn={() => Promise.resolve([])}
				focusOnInit
				itemRenderer={({name}) => name}
				onFocus={noop}
				selectedItem={{name: 'test'}}
			/>
		);

		fireEvent.keyDown(container.querySelector('.input-root'), {
			key: 'ArrowDown',
			keyCode: 40
		});

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});

describe('Item', () => {
	afterEach(cleanup);

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
