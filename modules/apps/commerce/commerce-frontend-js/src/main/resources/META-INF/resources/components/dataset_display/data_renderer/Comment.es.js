import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import {ClayTooltipProvider} from '@clayui/tooltip';
import React from 'react';

function Comment(props) {
	return (
		<ClayTooltipProvider>
			<ClayButton
				className="cell-comment text-warning px-1 my-n2 ml-2 inline-item"
				data-tooltip-align="top"
				data-tooltip-delay={0}
				displayType="link"
				title={props.children}
			>
				<ClayIcon symbol="info-circle" />
			</ClayButton>
		</ClayTooltipProvider>
	);
}

export default Comment;
