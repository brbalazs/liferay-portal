import BaseDropdown from './base-dropdown';
import EVENT_DEFINITION_QUERY from 'event-analysis/queries/EventDefinitionQuery';
import EVENT_DEFINITIONS_QUERY from 'event-analysis/queries/EventDefinitionsQuery';
import React, {useState} from 'react';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {Event, EventTypes} from 'event-analysis/utils/types';
import {Modal} from 'shared/types';
import {SafeResults} from 'shared/hoc/util';
import {useQuery} from '@apollo/react-hooks';

interface IAnalysisDropdownProps {
	close: Modal.close;
	eventId?: string;
	onEventChange: (event: Event) => void;
	open: Modal.open;
	trigger: React.ReactElement;
}

const AnalysisDropdown: React.FC<IAnalysisDropdownProps> = ({
	close,
	eventId,
	onEventChange,
	open,
	trigger
}) => {
	const [query, setQuery] = useState('');
	const [eventType, setEventType] = useState<EventTypes | 'all'>('all');

	const result = useQuery(EVENT_DEFINITIONS_QUERY, {
		variables: {
			eventType,
			keyword: '',
			page: 0,
			size: 200
		}
	});

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

					<SafeResults {...result} page={false} pageDisplay={false}>
						{({
							eventDefinitions: {eventDefinitions}
						}: {
							eventDefinitions: {eventDefinitions: Event[]};
						}) => (
							<BaseDropdown.SearchableList
								activeId={eventId}
								items={eventDefinitions}
								onEditClick={(event: Event) => {
									open(
										modalTypes.EDIT_ATTRIBUTE_EVENT_MODAL,
										{
											id: event.id,
											onCancel: close,
											query: EVENT_DEFINITION_QUERY
										}
									);

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
						)}
					</SafeResults>
				</>
			)}
		</BaseDropdown>
	);
};

export default connect(
	null,
	{close, open}
)(AnalysisDropdown);
