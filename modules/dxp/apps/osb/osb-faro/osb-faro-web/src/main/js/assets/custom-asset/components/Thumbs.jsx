import getSVG from 'shared/util/svg';
import React, {useEffect, useState} from 'react';

const CLASSNAME = 'analytics-add-report';

const Thumbs = ({items, onSelectThumb}) => {
	const [newItems, setNewItems] = useState(items);

	useEffect(() => {
		const selectedItem = newItems.find(({selected}) => selected);

		onSelectThumb(selectedItem);
	}, [newItems]);

	const selectThumb = id => {
		const updatedItems = newItems.map((item, index) => ({
			...item,
			selected: parseInt(id) === index
		}));

		setNewItems(updatedItems);
	};

	const handleClickSelectThumb = ({target}) => {
		selectThumb(target.parentNode.dataset.id);
	};

	const handleKeyPress = ({target}) => {
		selectThumb(target.parentNode.dataset.id);
	};

	function handleNotUsedEvents() {
		return;
	}

	return (
		<ul className={`${CLASSNAME}-thumbs`}>
			{newItems.map(({selected, svg, text}, index) => {
				const {id, viewBox} = getSVG(svg);
				const refSelected = selected ? 'selected' : '';

				return (
					<li
						className={refSelected}
						data-id={index}
						data-tooltip
						key={index}
						title={text}
					>
						<button
							onBlur={handleNotUsedEvents}
							onClick={handleClickSelectThumb}
							onFocus={handleNotUsedEvents}
							onKeyPress={handleKeyPress}
						>
							<svg className={svg} viewBox={viewBox}>
								<use
									xlinkHref={`/o/osb-faro-web/dist/sprite.svg#${id}`}
								/>
							</svg>
						</button>
					</li>
				);
			})}
		</ul>
	);
};

export default Thumbs;
