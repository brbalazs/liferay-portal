import Distribution from 'contacts/components/Distribution';
import React from 'react';
import {connect} from 'react-redux';
import {fetchDistribution} from 'shared/actions/distributions';

const SegmentDistribution = ({segment, ...otherProps}) => (
	<div className='segment-distribution-root container-fluid'>
		<div className='row'>
			<div className='col-xl-12'>
				<Distribution
					distributionsKey={segment.id}
					knownIndividualCount={segment.knownIndividualCount}
					{...otherProps}
				/>
			</div>
		</div>
	</div>
);

export default connect(null, {fetchDistribution})(SegmentDistribution);
