import Button from 'shared/components/Button';
import CardTabs, {CardTabSizes} from 'shared/components/CardTabs';
import ClayDropdown, {Align} from '@clayui/drop-down';
import getCN from 'classnames';
import Promise from 'metal-promise';
import React, {useEffect, useState} from 'react';
import {Event, EventTypes} from '../types';
import {spritemap} from 'shared/util/constants';

// TODO: Replace Mock Data
const MOCKED_DEFAULT_EVENTS_LIST = [
	{
		displayName: 'Abandoned Form',
		id: '0',
		name: 'abandonedForm',
		type: EventTypes.Default
	},
	{
		displayName: 'Add credit card ',
		id: '1',
		name: 'addCreditCard',
		type: EventTypes.Default
	},
	{
		displayName: 'Download',
		id: '2',
		name: 'download',

		type: EventTypes.Default
	}
];

// TODO: Replace Mock Data
const MOCKED_CUSTOM_EVENTS_LIST = [
	{
		displayName: 'Form Submit',
		id: '3',
		name: 'formSubmit',
		type: EventTypes.Custom
	},
	{
		displayName: 'Filed Ticket',
		id: '4',
		name: 'filedTicket',
		type: EventTypes.Custom
	},
	{
		displayName: 'Read Article',
		id: '5',
		name: 'readArticle',
		type: EventTypes.Custom
	}
];

const MOCKED_MAP = {
	all: [...MOCKED_DEFAULT_EVENTS_LIST, ...MOCKED_CUSTOM_EVENTS_LIST],
	[EventTypes.Custom]: MOCKED_CUSTOM_EVENTS_LIST,
	[EventTypes.Default]: MOCKED_DEFAULT_EVENTS_LIST
};

interface IAnalysisDropdownProps {
	eventId?: string;
	onEventChange: (event: Event) => void;
	trigger: React.ReactElement;
}

const AnalysisDropdown: React.FC<IAnalysisDropdownProps> = ({
	eventId,
	onEventChange,
	trigger
}) => {
	const [active, setActive] = useState(false);
	const [query, setQuery] = useState('');
	const [eventType, setEventType] = useState<EventTypes | 'all'>('all');
	const [events, setEvents] = useState<Event[]>([]);

	useEffect(() => {
		Promise.resolve(MOCKED_MAP[eventType]).then(response =>
			setEvents(response)
		);
	}, [eventType]);

	const filteredEvents = events.filter(({displayName, name}) =>
		(displayName || name)
			.toString()
			.toLowerCase()
			.includes(query.toLowerCase())
	);

	return (
		<ClayDropdown
			active={active}
			alignmentPosition={Align.RightTop}
			menuElementAttrs={{className: 'event-analysis-dropdown-menu-root'}}
			onActiveChange={setActive}
			trigger={trigger}
		>
			<div className='event-analysis-dropdown-header'>
				{Liferay.Language.get('events')}
			</div>

			<CardTabs
				activeTabId={eventType}
				className='event-type-selector'
				size={CardTabSizes.Small}
				tabs={[
					{
						onClick: () => setEventType('all'),
						tabId: 'all',
						title: Liferay.Language.get('all')
					},
					{
						onClick: () => setEventType(EventTypes.Default),
						tabId: EventTypes.Default,
						title: Liferay.Language.get('default')
					},
					{
						onClick: () => setEventType(EventTypes.Custom),
						tabId: EventTypes.Custom,
						title: Liferay.Language.get('custom')
					}
				]}
			/>

			<ClayDropdown.Search
				formProps={{onSubmit: e => e.preventDefault()}}
				onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
					setQuery(event.target.value)
				}
				placeholder={Liferay.Language.get('search')}
				spritemap={spritemap}
				value={query}
			/>

			<ClayDropdown.ItemList>
				{filteredEvents.map((event: Event) => (
					<ClayDropdown.Item
						className={getCN({active: event.id === eventId})}
						key={event.id}
					>
						<Button
							className='dropdown-item-primary-button'
							display='unstyled'
							onClick={() => {
								if (event.id !== eventId) {
									onEventChange(event);

									setActive(false);
									setEventType('all');
									setQuery('');
								}
							}}
						>
							{event.displayName || event.name}
						</Button>
					</ClayDropdown.Item>
				))}
			</ClayDropdown.ItemList>
		</ClayDropdown>
	);
};

export default AnalysisDropdown;
