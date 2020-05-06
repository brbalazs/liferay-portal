import React from 'react';
import ReactDOM from 'react-dom';
import SummaryBaseCard from '../index';
import {shallow} from 'enzyme';

ReactDOM.createPortal.mockImplementationOnce(component => component);

describe('SummaryBaseCard', () => {
	it('should render component', () => {
		const component = shallow(<SummaryBaseCard />);

		expect(component.length).toBe(1);
		expect(component.hasClass('card-root card analytics-summary-card'));
		expect(component.render()).toMatchSnapshot();
	});
});

describe('SummaryBaseCard.Header Actions', () => {
	it('should render component with Header', () => {
		const component = shallow(
			<SummaryBaseCard>
				<SummaryBaseCard.Header>
					{'Summary Card with Header'}
				</SummaryBaseCard.Header>
			</SummaryBaseCard>
		);

		expect(component.find('Header').length).toBe(1);
		expect(component.find('Header').props().children).toEqual(
			'Summary Card with Header'
		);
		expect(component.children().render()).toMatchSnapshot();
	});

	it('should render component with Header and 1 action', () => {
		const MODALS = [
			{
				title: 'action 01'
			}
		];

		const component = shallow(
			<SummaryBaseCard>
				<SummaryBaseCard.Header modals={MODALS}>
					{'Summary Card with Header'}
				</SummaryBaseCard.Header>
			</SummaryBaseCard>
		);

		expect(component.find('Header').props().modals).toEqual(MODALS);
		expect(
			component
				.find('Header')
				.shallow()
				.find('ForwardRef').length
		).toBe(1);
	});

	it('should render component with Header and 2 modals', () => {
		const MODALS = [
			{
				title: 'action 01'
			},
			{
				label: 'action 02',
				type: 'action02'
			}
		];

		const component = shallow(
			<SummaryBaseCard>
				<SummaryBaseCard.Header modals={MODALS}>
					{'Summary Card with Header'}
				</SummaryBaseCard.Header>
			</SummaryBaseCard>
		);

		expect(component.find('Header').props().modals).toEqual(MODALS);
		expect(
			component
				.find('Header')
				.shallow()
				.find('ClayDropDownItemList')
				.shallow()
				.find('ForwardRef').length
		).toBe(1);
	});

	it('should render component with Header and 3 modals', () => {
		const MODALS = [
			{
				title: 'action 01'
			},
			{
				label: 'action 02',
				type: 'action02'
			},
			{
				label: 'action 03',
				type: 'action03'
			},
			{
				label: 'action 04',
				type: 'action04'
			}
		];

		const component = shallow(
			<SummaryBaseCard>
				<SummaryBaseCard.Header modals={MODALS}>
					{'Summary Card with Header'}
				</SummaryBaseCard.Header>
			</SummaryBaseCard>
		);

		expect(component.find('Header').props().modals).toEqual(MODALS);
		expect(
			component
				.find('Header')
				.shallow()
				.find('ClayDropDownItemList')
				.shallow()
				.find('ForwardRef').length
		).toBe(3);
	});
});

describe('SummaryBaseCard.Header cardModals', () => {
	it('should render component with Header and 1 card action', () => {
		const CARD_MODALS = [
			{
				title: 'action 01'
			}
		];

		const component = shallow(
			<SummaryBaseCard>
				<SummaryBaseCard.Header cardModals={CARD_MODALS}>
					{'Summary Card with Header'}
				</SummaryBaseCard.Header>
			</SummaryBaseCard>
		);

		expect(component.find('Header').props().cardModals).toEqual(
			CARD_MODALS
		);
		expect(
			component
				.find('Header')
				.shallow()
				.find('ForwardRef').length
		).toBe(1);
	});

	it('should render component with Header and 2 card modals', () => {
		const CARD_MODALS = [
			{
				title: 'action 01'
			},
			{
				label: 'action 02',
				type: 'action02'
			}
		];

		const component = shallow(
			<SummaryBaseCard>
				<SummaryBaseCard.Header cardModals={CARD_MODALS}>
					{'Summary Card with Header'}
				</SummaryBaseCard.Header>
			</SummaryBaseCard>
		);

		expect(component.find('Header').props().cardModals).toEqual(
			CARD_MODALS
		);
		expect(
			component
				.find('Header')
				.shallow()
				.find('ForwardRef').length
		).toBe(2);
	});
});

describe('SummaryBaseCard.Body', () => {
	it('should render component with Body', () => {
		const component = shallow(
			<SummaryBaseCard>
				<SummaryBaseCard.Body>
					{'Summary Card with Body'}
				</SummaryBaseCard.Body>
			</SummaryBaseCard>
		);

		expect(component.find('Body').length).toBe(1);
		expect(component.find('Body').props().children).toEqual(
			'Summary Card with Body'
		);
		expect(component.children().render()).toMatchSnapshot();
	});
});

describe('SummaryBaseCard.Footer', () => {
	it('should render component with Footer', () => {
		const component = shallow(
			<SummaryBaseCard>
				<SummaryBaseCard.Footer>
					{'Summary Card with Footer'}
				</SummaryBaseCard.Footer>
			</SummaryBaseCard>
		);

		expect(component.find('Footer').length).toBe(1);
		expect(component.find('Footer').props().children).toEqual(
			'Summary Card with Footer'
		);
		expect(component.children().render()).toMatchSnapshot();
	});
});
