import HTMLBox from './HTMLBox';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';
import URLConstants from 'shared/util/url-constants';
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

		<NoResultsDisplay
			description={
				<>
					<span className='mr-1'>
						{Liferay.Language.get(
							'you-can-come-back-later-and-check-if-there-is-any-data-received-from-your-data-sources'
						)}
					</span>

					<a
						href={URLConstants.SitesDashboardPagesPath}
						key='DOCUMENTATION'
						target='_blank'
					>
						{Liferay.Language.get('learn-more-about-path')}
					</a>
				</>
			}
			title={Liferay.Language.get('there-are-no-data-found')}
		/>
	</div>
);

export default EmptyStateEdge;
