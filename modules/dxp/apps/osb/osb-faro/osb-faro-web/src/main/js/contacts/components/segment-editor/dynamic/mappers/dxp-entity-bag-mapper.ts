import {getSortFromOrderIOMap} from 'shared/util/pagination';
import {IPagination} from 'shared/types';

export const mapPropsToOptions = ({
	channelId,
	delta,
	orderIOMap,
	page,
	query
}: IPagination & {channelId: string}) => ({
	variables: {
		channelId,
		keywords: query,
		size: delta,
		sort: getSortFromOrderIOMap(orderIOMap),
		start: (page - 1) * delta
	}
});

export const getMapResultToProps = graphqlEntityType => ({
	[graphqlEntityType]: {dxpEntities, total}
}: {
	[key: string]: {
		dxpEntities: {id: string; name: string}[];
		total: number;
	};
}) => ({
	empty: !total,
	items: dxpEntities,
	total
});
