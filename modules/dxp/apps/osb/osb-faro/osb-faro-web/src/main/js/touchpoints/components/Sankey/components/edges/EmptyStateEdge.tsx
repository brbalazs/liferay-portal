import React from 'react';
import SankeyBox from './HTMLBox';
import {NodeSankey} from '../Sankey';
import {SANKEY_COLORS} from '../../utils/sankey';

export const CLASSNAME = 'analytics-sankey';
export const CLASSNAME_BOX = `${CLASSNAME}-parent`;

interface IEmptyStateEdgeProps extends React.HTMLAttributes<HTMLElement> {
	node: NodeSankey;
}

const EmptyStateEdge: React.FC<IEmptyStateEdgeProps> = ({node}) => (
	<div className={CLASSNAME_BOX}>
		<SankeyBox color={SANKEY_COLORS.bgInactive} node={node} />

		<div className={`${CLASSNAME_BOX}-text`}>
			<p className='mb-2'>
				{Liferay.Language.get('no-data-found-for-the-selected-filter')}
			</p>
			<p className='font-size-sm'>
				{Liferay.Language.get(
					'the-selected-filter-did-not-match-any-result'
				)}
			</p>
		</div>
	</div>
);

export default EmptyStateEdge;
