/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import classnames from 'classnames';
import PropTypes from 'prop-types';
import React from 'react';

const stateToCssClassesMap = {
	active: 'text-primary',
	completed: 'text-success',
	inactive: 'text-muted'
};

function mapStateToCssClass(state) {
	return stateToCssClassesMap[state];
}

function Step(props) {
	return (
		<div
			className={classnames(
				`step`,
				mapStateToCssClass(props.state || 'inactive')
			)}
		>
			<span className="step-label">{props.label}</span>
		</div>
	);
}

Step.propTypes = {
	label: PropTypes.string.isRequired,
	state: PropTypes.oneOf(['completed', 'active', 'inactive'])
};

function StepTracker(props) {
	return (
		<div className="step-tracker rounded">
			{props.steps.map(step => (
				<Step key={step.id} {...step} />
			))}
		</div>
	);
}

StepTracker.propTypes = {
	steps: PropTypes.array.isRequired
};

export default StepTracker;
