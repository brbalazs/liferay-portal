export enum Type { // TODO: Make this enum more specific in name
	Average = 'average',
	Total = 'total',
	Unique = 'unique'
}

export enum DateGroupings {
	Dates = 'dates',
	Months = 'months',
	Years = 'years'
}

export type Breakdown = {
	attributeId: string;
	attributeType?: string; // TODO: Maybe need this
	dateGrouping?: DateGroupings;
	bin?: any; // TODO: May have to make this more specific
	name?: string;
};

export type Event = {
	id: string;
	name: string;
};

export type Filter = {
	attributeId: string;
	dataType: string; // TODO: update this to be on of the specified data types
	operator: string; // TODO: Make this more specific when operators have been defined
	value: (string | number)[];
};

// TODO Create type for Operators
