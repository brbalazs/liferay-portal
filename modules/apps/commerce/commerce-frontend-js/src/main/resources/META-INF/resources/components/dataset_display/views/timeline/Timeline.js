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

import ClayList from '@clayui/list';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import React, {useContext} from 'react';

function TimelineEntry(props) {
	return (
		<li className="timeline-item">
			<div className="panel panel-secondary">
				<div className="timeline-increment">
					<span className="timeline-icon"></span>
				</div>
				<div className="panel-body">
					<div className="mb-2 row">
						<div className="col">
							<h4 className="mb-0">
								{props.title}
							</h4>
						</div>
						<div class=" col-auto">
							<small>{props.date}</small>
						</div>
					</div>
					<small>{props.description}</small>
				</div>
			</div>
		</li>
	);
}

Timeline.propTypes = {
	date: PropTypes.string.isRequired,
	description: PropTypes.string.isRequired,
	title: PropTypes.string.isRequired
};

Timeline.defaultProps = {
};

function Timeline(props) {
	const {style} = useContext(props.datasetDisplayContext);

	return (
		<ClayList
			className={classNames(
				'mb-0', 'timeline',
				style === 'default' ? 'border-bottom' : 'border'
			)}
		>
			{props.items.map((item, i) => (
				<TimelineEntry
					key={i}
					{...item}
					borderBottom={i !== props.items.length - 1}
					datasetDisplayContext={props.datasetDisplayContext}
				/>
			))}
		</ClayList>
	);
}

Timeline.propTypes = {
	dataRenderers: PropTypes.object,
	datasetDisplayContext: PropTypes.any,
	items: PropTypes.array
};

Timeline.defaultProps = {
	items: []
};

export default Timeline;
