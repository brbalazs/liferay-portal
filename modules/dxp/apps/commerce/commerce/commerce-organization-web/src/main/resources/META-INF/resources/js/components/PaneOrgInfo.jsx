import React from 'react';

export default function PaneOrgInfo(props) {
	const { showMenu, orgName, childrenNo } = props;

	return(
		<div className='pane-org-info'>
			<div className='org-colored-identifier'></div>
			<div className='org-data'>
				<p>{orgName}</p>
				<p>{!!childrenNo ? `${childrenNo} sub-organizations` : 'Sub-organization'}</p>
			</div>
			<div role='button' onClick={showMenu} tabIndex='1' className='org-actions'>
				<p style={{display: 'none'}}>&sdot;&sdot;&sdot;</p>
			</div>
		</div>
	);
}
