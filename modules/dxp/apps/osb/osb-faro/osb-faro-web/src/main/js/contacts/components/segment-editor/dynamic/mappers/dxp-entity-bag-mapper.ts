import {IPagination} from 'shared/types';

export const mapPropsToOptions = ({
	channelId,
	delta,
	orderBy,
	orderByField,
	page,
	query
}: IPagination & {channelId: string}) => ({
	variables: {
		channelId,
		keywords: query,
		size: delta,
		sort: {
			column: orderByField,
			type: orderBy.toUpperCase()
		},
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
