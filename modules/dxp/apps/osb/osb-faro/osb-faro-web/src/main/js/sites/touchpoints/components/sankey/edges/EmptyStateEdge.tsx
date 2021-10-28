import HTMLBox from './HTMLBox';
import React from 'react';
import {SANKEY_COLORS} from '../utils/sankey';
import {SankeyNode} from '../utils/types';

export const CLASSNAME = 'analytics-sankey';
export const CLASSNAME_BOX = `${CLASSNAME}-parent`;

interface IEmptyStateEdgeProps extends React.HTMLAttributes<HTMLElement> {
	node: SankeyNode;
}

const EmptyStateEdge: React.FC<IEmptyStateEdgeProps> = ({node}) => (
	<div className={CLASSNAME_BOX}>
		<HTMLBox color={SANKEY_COLORS.bgInactive} node={node} />

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
