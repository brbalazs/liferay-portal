import ClayDropdown from '@clayui/drop-down';
import ListItem from './ListItem';
import React from 'react';
import {Attribute, Breakdown, Event, Filter} from '../../types';
import {spritemap} from 'shared/util/constants';

interface ISearchableListProps {
	activeId?: string;
	disabledIds?: string[];
	items: (Attribute | Event)[];
	onItemClick: (item: Attribute | Event, breakdown?: Breakdown, filter?: Filter) => void;
	onItemFilterClick?: (item: Attribute) => void;
	onQueryChange: (query: string) => void;
	query: string;
	selectedId?: string;
}

const SearchableList: React.FC<ISearchableListProps> = ({
	activeId,
	disabledIds,
	items,
	onItemClick,
	onItemFilterClick,
	onQueryChange,
	query
}) => {
	const filteredItems = items.filter(({displayName, name}) =>
		(displayName || name)
			.toString()
			.toLowerCase()
			.includes(query.toLowerCase())
	);

	return (
		<>
			<ClayDropdown.Search
				formProps={{onSubmit: e => e.preventDefault()}}
				onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
					onQueryChange(event.target.value)
				}
				placeholder={Liferay.Language.get('search')}
				spritemap={spritemap}
				value={query}
			/>

			<ClayDropdown.ItemList>
				{filteredItems.map(item => (
					<ListItem
						active={activeId === item.id}
						disabled={
							disabledIds &&
							disabledIds.some(id => id === item.id)
						}
						item={item}
						key={item.id}
						onClick={() => onItemClick(item)}
						onFilterClick={
							onItemFilterClick
								? () => onItemFilterClick(item as Attribute)
								: null
						}
					/>
				))}
			</ClayDropdown.ItemList>
		</>
	);
};

export default SearchableList;
