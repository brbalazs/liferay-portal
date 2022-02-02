import * as API from 'shared/api';
import {createOrderIOMap} from 'shared/util/pagination';
import {DataSourceStatuses, DataSourceTypes} from 'shared/util/constants';
import {IStatesRendererContextProps} from 'shared/components/states-renderer/StatesRenderer';
import {NAME} from 'shared/util/pagination';
import {useParams} from 'react-router-dom';
import {useQueryPagination, useRequest} from 'shared/hooks';

interface IDataSourceProps {
	contactsSelected: boolean;
	dateCreated: string;
	name: string;
	providerType: DataSourceTypes;
	sitesSelected: boolean;
	status: DataSourceStatuses;
}

interface IUseDataSourceProps extends IStatesRendererContextProps {
	items: IDataSourceProps[];
}

export const useDataSource: () => IStatesRendererContextProps &
	IUseDataSourceProps = () => {
	const {groupId} = useParams();
	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(NAME)
	});

	const {data = {items: []}, error, loading} = useRequest({
		dataSourceFn: API.dataSource.search,
		variables: {
			delta,
			groupId,
			orderIOMap,
			page,
			query
		}
	});

	return {
		empty: !data?.items.length && !error && !loading,
		error,
		items: data?.items,
		loading
	};
};
