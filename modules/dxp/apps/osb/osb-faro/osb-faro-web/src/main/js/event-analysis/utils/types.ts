export enum AttributeOwnerTypes {
	Account = 'account',
	Event = 'event',
	Individual = 'individual',
	Session = 'session'
}

export enum CalculationTypes {
	Average = 'average',
	Total = 'total',
	Unique = 'unique'
}

export enum DataTypes {
	Boolean = 'BOOLEAN',
	Date = 'DATE',
	Duration = 'DURATION',
	Number = 'NUMBER',
	String = 'STRING'
}

export enum DateGroupings {
	Dates = 'dates',
	Months = 'months',
	Years = 'years'
}

export enum EventTypes {
	All = 'ALL',
	Custom = 'CUSTOM',
	Default = 'DEFAULT'
}

export enum Operators {
	Between = 'between',
	Contains = 'contains',
	NotContains = 'not-contains',
	EQ = 'eq',
	GT = 'gt',
	LT = 'lt',
	NE = 'ne'
}

export type Attribute = {
	dataType: DataTypes;
	description?: string;
	displayName?: string;
	id: string;
	name: string;
	recentValues?: {
		lastSeenDate: string;
		value: string;
	}[];
	sampleValue?: string;
};

export type Attributes = {[key: string]: Attribute};

export type BlockedCustomEvent = {
	id: string;
	name: string;
	lastSeenDate: string;
	lastSeenURL: string;
};

export type Breakdown = {
	attributeId: string;
	bin?: number;
	dataType: DataTypes;
	dateGrouping?: DateGroupings;
	type: AttributeOwnerTypes;
};

export type Breakdowns = {[key: string]: Breakdown};

export type Event = {
	description?: string;
	displayName?: string;
	eventAttributeDefinitions?: Attribute[];
	id: string;
	name: string;
	type: EventTypes;
};

export type Filter = {
	attributeId: string;
	operator: Operators;
	value: (boolean | string | number)[];
};

export type Filters = {[key: string]: Filter};

export interface IFilterProps {
	attributeId: string;
	attributeOwnerType: AttributeOwnerTypes;
	breakdown?: Breakdown;
	filter?: Filter;
	onFilterSubmit: (params: {breakdown: Breakdown; filter: Filter}) => void;
}

export type BreakdownDataItem = {
	name: string;
	previousValue?: number;
	value: number;
	isLeafNode: boolean;
	breakdownItems?: BreakdownDataItem[];
};

export type BreakdownData = {
	count: number;
	totalEvents: number;
	breakdownItems: BreakdownDataItem[];
};

export type ParsedBreakdownItem = {
	events: BreakdownDataItem[];
	index: string;
	breakdown0: BreakdownDataItem & {rowSpan: number};
	[key: string]:
		| (BreakdownDataItem & {rowSpan: number})
		| BreakdownDataItem[]
		| string;
};

export type ParsedBreakdownData = ParsedBreakdownItem[];
