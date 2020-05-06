import RequestListQuery from '../../queries/RequestListQuery';
import {
	FilterByType,
	FilterInputType,
	FilterOptionType,
	RouterType
} from 'shared/types';
import {get, omit} from 'lodash';
import {getMapPropsToOptions} from 'shared/hoc/mappers/metrics';

const getFilterOptionType = (
	filterKey: string,
	filterByOptions: FilterOptionType[]
): FilterInputType =>
	get(
		filterByOptions.find(({key}: {key: string}) => key === filterKey),
		'type',
		'checkbox'
	);

export const mapPropsToOptions = ({
	defaultSort,
	filterBy,
	router,
	toolbarProps,
	...otherProps
}: {
	defaultSort: {field: string; sortOrder: string};
	filterBy: FilterByType;
	router: RouterType;
	toolbarProps: {filterByOptions: FilterOptionType[]};
}) => {
	const {variables, ...otherOptions} = getMapPropsToOptions(RequestListQuery)(
		{defaultSort, router, toolbarProps, ...otherProps}
	);

	const {filterByOptions} = toolbarProps;

	return {
		variables: {
			...omit(variables, 'rangeKey'),
			...filterBy
				.filterNot(val => val.isEmpty())
				.map((val, key) =>
					getFilterOptionType(key, filterByOptions) === 'radio'
						? parseInt(val.first())
						: val
				)
				.toJS()
		},
		...otherOptions
	};
};
