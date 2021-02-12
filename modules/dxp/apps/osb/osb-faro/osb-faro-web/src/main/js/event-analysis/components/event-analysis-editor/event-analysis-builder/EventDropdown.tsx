import BaseDropdown from './base-dropdown';
import Promise from 'metal-promise';
import React, {useEffect, useState} from 'react';
import {Event, EventTypes} from '../types';

// TODO: LRAC-7466 Replace Mock Data
const MOCKED_DEFAULT_EVENTS_LIST = [
	{
		description: 'Blah blah blah blah',
		displayName: 'Abandoned Form',
		id: '0',
		name: 'abandonedForm',
		type: EventTypes.Default
	},
	{
		description: 'Blah blah blah blah',
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

// TODO: LRAC-7466 Replace Mock Data
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
	const [query, setQuery] = useState('');
	const [eventType, setEventType] = useState<EventTypes | 'all'>('all');
	const [events, setEvents] = useState<Event[]>([]);

	useEffect(() => {
		Promise.resolve(MOCKED_MAP[eventType]).then(response =>
			setEvents(response)
		);
	}, [eventType]);

	return (
		<BaseDropdown trigger={trigger}>
			{({setActive}) => (
				<>
					<BaseDropdown.Header
						activeTabId={eventType}
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
						title={Liferay.Language.get('events')}
					/>

					<BaseDropdown.SearchableList
						activeId={eventId}
						items={events}
						onEditClick={() => {
							// TODO: LRAC-7407 Connect to edit modal
							setActive(false);
						}}
						onItemClick={(event: Event) => {
							if (event.id !== eventId) {
								onEventChange(event);

								setActive(false);
								setEventType('all');
								setQuery('');
							}
						}}
						onQueryChange={setQuery}
						query={query}
					/>
				</>
			)}
		</BaseDropdown>
	);
};

export default AnalysisDropdown;
