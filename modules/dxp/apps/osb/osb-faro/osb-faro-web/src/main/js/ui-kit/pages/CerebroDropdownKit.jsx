import Dropdown from 'cerebro-shared/components/Dropdown';
import React from 'react';
import Row from '../components/Row';
import {range} from 'lodash';

const mockItems = range(3).map(i => ({
	label: `foo label${i}`,
	value: `foo value${i}`
}));

export default class CerebroDropdownKit extends React.Component {
	render() {
		return (
			<div>
				<Row>
					<h3>{'Dropdown'}</h3>

					<Dropdown items={mockItems} />
				</Row>
			</div>
		);
	}
}
