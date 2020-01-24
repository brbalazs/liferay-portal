import {ClayButtonWithIcon} from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import Icon from '@clayui/icon';
import ClayLink from '@clayui/link';
import ClayPanel from '@clayui/panel';
import classNames from 'classnames';

import PropTypes from 'prop-types';
import React, {useState} from 'react';

function ActiveViewSelector(props) {
	const [active, setActive] = useState(false);

	return (
		<ClayDropDown
			active={active}
			onActiveChange={setActive}
			trigger={
				<ClayButtonWithIcon
					displayType="secondary"
					symbol={props.views[props.activeView || 0].icon}
				/>
			}
		>
			<ClayDropDown.ItemList>
				{props.views.map((view, i) => (
					<ClayDropDown.Item
						href="#"
						key={i}
						onClick={e => {
							e.preventDefault();
							props.setActiveView(i);
						}}
					>
						<ClayIcon className="mr-3" symbol={view.icon} />
						{view.label}
					</ClayDropDown.Item>
				))}
			</ClayDropDown.ItemList>
		</ClayDropDown>
	);
}

ActiveViewSelector.propTypes = {
	activeView: PropTypes.number.isRequired,
	setActiveView: PropTypes.func.isRequired,
	views: PropTypes.arrayOf(
		PropTypes.shape({
			icon: PropTypes.string.isRequired,
			label: PropTypes.string.isRequired
		})
	)
};

export default ActiveViewSelector;
