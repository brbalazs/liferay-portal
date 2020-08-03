import BasePage from 'shared/components/base-page';
import React, {useContext, useState} from 'react';
import SankeyTouchpointItem from '../hocs/PathTouchpoint';
import TouchpointPathSankey from '../hocs/TouchpointPathSankey';
import {PropTypes} from 'prop-types';

const SANKEY_OFFSET = 71;
const SANKEY_DEFAULT_SIZE = 720;
const SANKEY_STARTER_SIZE = 410;

/**
 * Render Touchpoint Component
 * @param {object} props
 */
const renderTouchpointComponent = props => <SankeyTouchpointItem {...props} />;

/**
 * Touchpoint Path page
 * @class
 */
export default function TouchpointPathPage({pathRangeSelectors}) {
	const [sankeyHeight, setSankeyHeight] = useState(SANKEY_STARTER_SIZE);
	const [isExpandedState, setIsExpanded] = useState(null);

	const {filters, router} = useContext(BasePage.Context);

	const {touchpoint} = router.params;

	const handleResizeSankey = ({isExpanded, sankeyElement}) => {
		let currentSankeyHeight = 0;

		if (sankeyElement) {
			currentSankeyHeight =
				sankeyElement.getBoundingClientRect().height + SANKEY_OFFSET;
		} else if (typeof isExpanded != 'number') {
			currentSankeyHeight = SANKEY_DEFAULT_SIZE;
		}

		if (isExpandedState !== isExpanded) {
			setSankeyHeight(currentSankeyHeight);
			setIsExpanded(isExpanded);
		}
	};

	return (
		<div className='row'>
			<div className='analytics-sankey-column col-sm-12'>
				<TouchpointPathSankey
					data={{
						links: [],
						nodes: []
					}}
					filters={filters}
					height={sankeyHeight}
					onHeightChange={handleResizeSankey}
					rangeSelectors={pathRangeSelectors}
					renderTouchpointComponent={renderTouchpointComponent}
					router={router}
					touchpoint={touchpoint}
					width='100%'
				/>
			</div>
		</div>
	);
}
TouchpointPathPage.propTypes = {
	pathRangeKey: PropTypes.string
};
