import DynamicSegment from './edit/Dynamic';
import FaroConstants from 'shared/util/constants';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import StaticSegment from './edit/Static';
import {get} from 'lodash';
import {optional} from 'shared/hoc';
import {PropTypes} from 'prop-types';
import {Segment} from 'shared/util/records';
import {withSegment} from 'shared/hoc/WithSegment';

const {segmentTypes} = FaroConstants;

const PAGE_MAP = {
	[segmentTypes.dynamic]: DynamicSegment,
	[segmentTypes.static]: StaticSegment
};

export class Edit extends React.Component {
	static defaultProps = {
		type: segmentTypes.dynamic
	};

	static propTypes = {
		segment: PropTypes.instanceOf(Segment),
		type: PropTypes.oneOf([segmentTypes.dynamic, segmentTypes.static])
	};

	render() {
		const {segment, type, ...otherProps} = this.props;

		const segmentType = get(segment, 'segmentType') || type;

		const Page = PAGE_MAP[segmentType];

		if (Page) {
			return (
				<Page
					{...omitDefinedProps(otherProps, Edit.propTypes)}
					segment={segment}
					type={segmentType}
				/>
			);
		}
	}
}

export default optional(withSegment(true))(Edit);
