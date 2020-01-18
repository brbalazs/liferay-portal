import ClayDropDown from '@clayui/drop-down';
import Icon from '@clayui/icon';
import ClayPanel from '@clayui/panel';
import React, { useState } from 'react';
import classNames from 'classnames'
import { ClayButtonWithIcon } from '@clayui/button';
import PropTypes from 'prop-types';
import ClayLink from '@clayui/link';
import ClayIcon from '@clayui/icon';

function ActiveViewSelector(props) {
	const [active, setActive] = useState(false);

	return (
		<ClayDropDown
			active={active}
			onActiveChange={setActive}
			trigger={
				<ClayButtonWithIcon
					displayType="secondary"
					symbol={props.views.find(view => view.id === props.activeViewId).icon}
				/>
			}
		>
			<ClayDropDown.ItemList>
				{props.views.map(view => (
					<ClayDropDown.Item
						href="#"
						key={view.id}
						onClick={(e) => {
							e.preventDefault();
							props.setActiveView(view.id);
						}}
					>
						<ClayIcon symbol={view.icon} className="mr-3" />
						{view.label}
					</ClayDropDown.Item>
				))}
			</ClayDropDown.ItemList>
		</ClayDropDown>
	);
};

ActiveViewSelector.propTypes = {
	activeViewId: PropTypes.string.isRequired,
	setActiveView: PropTypes.string.isRequired,
	views: PropTypes.arrayOf(PropTypes.shape({
		icon: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		label: PropTypes.string.isRequired,
	}))
}

export default ActiveViewSelector;
