import ClayDropdown from '@clayui/drop-down';
import ListItem from './ListItem';
import React from 'react';
import {Attribute, Event} from '../../types';
import {spritemap} from 'shared/util/constants';

interface ISearchableListProps {
	items: (Attribute | Event)[];
	onItemClick: (item: Attribute | Event) => void;
	onItemFilterClick?: (item: Attribute) => void;
	onQueryChange: (query: string) => void;
	query: string;
	selectedId?: string;
}

const SearchableList: React.FC<ISearchableListProps> = ({
	items,
	onItemClick,
	onItemFilterClick,
	onQueryChange,
	query,
	selectedId
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
