export enum AttributeTypes {
	Account = 'account',
	Event = 'event',
	Individual = 'individual',
	Session = 'session'
}

export enum DataTypes {
	Boolean = 'boolean',
	Date = 'date',
	Duration = 'duration',
	Number = 'number',
	String = 'string'
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

export enum Type { // TODO: Make this enum more specific in name  make this plural too
	Average = 'average',
	Total = 'total',
	Unique = 'unique'
}

export enum DateGroupings {
	Dates = 'dates',
	Months = 'months',
	Years = 'years'
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
	bin?: any; // TODO: May have to make this more specific
	type: AttributeTypes;
	// name?: string;
};

export type Event = {
	description?: string;
	displayName?: string;
	id: string;
	name: string;
	type: string; // TODO: Default or Custom
};

export type Filter = {
	attributeId: string;
	// dataType: DataTypes; // TODO: update this to be on of the specified data types
	operator: Operators; // TODO: Make this more specific when operators have been defined
	value: (boolean | string | number)[];
};

// TODO Create type for Operators
