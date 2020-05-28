import {Map, Set} from 'immutable';
import {Modal} from './Modal';

export {Alert} from './Alert';
export {Modal} from './Modal';

export interface IDataColumn {
	accessor: string;
	cellRenderer?: React.ComponentType<any>;
	cellRendererProps?: object;
	className?: string;
	dataFormatter?: (dataValue: React.ReactNode, data?: any) => React.ReactNode;
}

export interface IColumn extends IDataColumn {
	label: string;
	sortable?: boolean;
	title?: boolean;
}

export type Columns = IColumn[];

export type Composition = {
	count: number;
	name: string;
};

export type DisplayType = 'primary' | 'secondary' | 'link' | 'unstyled';

/**
 * FilterBy
 */
export type FilterByType = Map<string, Set<string>>;
export type FilterInputType = 'radio' | 'checkbox';
export type FilterOptionType = {
	label: string;
	key: string;
	type?: FilterInputType;
	values: {label: string; value: string}[];
};

export interface ICompositionBag {
	items: Composition[];
	maxCount: number;
	total: number;
	totalCount: number;
}

export interface IBasePageContext {
	filters: object;
	router: {
		params: {
			groupId: string;
		};
		query: object;
	};
}

export interface IPagination {
	delta: number;
	filterBy?: FilterByType;
	orderBy: string;
	orderByField: string;
	page: number;
	query: string;
}

export interface IPaginationUnsorted
	extends Omit<IPagination, 'orderBy' | 'orderByField'> {}

export type RangeSelectors = {
	rangeEnd: string;
	rangeKey: string;
	rangeStart: string;
};

export type SafeRangeSelectors = {
	rangeEnd: string;
	rangeKey: number;
	rangeStart: string;
};

export interface RESTParams {
	delta?: number;
	groupId: string;
	page?: number;
	query?: string;
}

export type RouterType = {
	params: {[key: string]: string};
	query: {[key: string]: string};
};

export interface HasModal {
	close: Modal.close;
	open: Modal.open;
}

export type Interval = 'D' | 'M' | 'W';
