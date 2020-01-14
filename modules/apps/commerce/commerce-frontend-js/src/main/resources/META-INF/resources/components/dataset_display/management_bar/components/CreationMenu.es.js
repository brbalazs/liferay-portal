import { ClayButtonWithIcon } from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import PropTypes from 'prop-types';
import React, { useState, useContext } from 'react';

import { OPEN_MODAL } from '../../../../utilities/eventsDefinitions.es';
import DatasetDisplayContext from '../../DatasetDisplayContext.es'

function CreationMenu(props) {
	const [active, setActive] = useState(false);
	const datasetContext = useContext(DatasetDisplayContext);

	function executeAction(i) {
		const clickedItem = props.items[i];

		switch (clickedItem.type) {
			case 'modal':
				Liferay.fire(OPEN_MODAL, {
					id: datasetContext.modalId,
					onClose: datasetContext.loadData,
					url: clickedItem.url,
				})
				break;
			case 'inline':
				break;
			default:
				window.location.href = clickedItem.href;
				break;
		}
	}

	if(!props.items || !props.items.length) return;

	return (
		<ul className="navbar-nav">
			<li className="nav-item">
				{props.items.length > 1 ? (
					<ClayDropDown
						active={active}
						onActiveChange={setActive}
						trigger={<ClayButtonWithIcon symbol="plus" />}
					>
						<ClayDropDown.ItemList>
							{props.items.map((item, i) => (
								<ClayDropDown.Item href={item.href || '#'} key={i} onClick={(e) => {
									e.preventDefault();
									setActive(false);
									executeAction(i);
								}}>
									{item.label}
								</ClayDropDown.Item>
							))}
						</ClayDropDown.ItemList>
					</ClayDropDown>
				) : (
					<ClayButtonWithIcon onClick={() => executeAction(0)} symbol="plus" />
				)}
			</li>
		</ul>
	);
};

CreationMenu.propTypes = {
	items: PropTypes.arrayOf(PropTypes.oneOfType([
		PropTypes.shape({
			href: PropTypes.string.isRequired,
			label: PropTypes.string.isRequired,
		}),
		PropTypes.shape({
			label: PropTypes.string.isRequired,
			type: PropTypes.oneOf(['modal']).isRequired,
			url: PropTypes.string.isRequired,
		}),
		PropTypes.shape({
			apiUrl: PropTypes.string.isRequired,
			editableFields: PropTypes.arrayOf(PropTypes.string).isRequired,
			label: PropTypes.string.isRequired,
			type: PropTypes.oneOf(['inline']).isRequired,
		})
	])).isRequired
}

export default CreationMenu;
