import {Criterion} from 'contacts/components/segment-editor/dynamic/utils/types';
import {Map} from 'immutable';
import {Property} from 'shared/util/records';

export interface IDisplayComponentProps {
	criterion: Criterion;
	property: Property;
}

export interface ICustomDisplayComponentProps extends IDisplayComponentProps {
	criterion: Criterion & {value: Map<string, any>};
}
