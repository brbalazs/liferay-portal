export enum AttributeTypes {
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
	Boolean = 'boolean',
	Date = 'date',
	Duration = 'duration',
	Number = 'number',
	String = 'string'
}

export enum DateGroupings {
	Dates = 'dates',
	Months = 'months',
	Years = 'years'
}

export enum EventTypes {
	Custom = 'custom',
	Default = 'default'
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
	defaultDataType: DataTypes;
	description?: string;
	displayName?: string;
	id: string;
	name: string;
	sampleValue?: string;
};

export type Breakdown = {
	attributeId: string;
	dataType: DataTypes;
	dateGrouping?: DateGroupings;
	bin?: number;
	type: AttributeTypes;
};

export type Event = {
	description?: string;
	displayName?: string;
	id: string;
	name: string;
	type: EventTypes;
};

export type Filter = {
	attributeId: string;
	operator: Operators;
	value: (boolean | string | number)[];
};
