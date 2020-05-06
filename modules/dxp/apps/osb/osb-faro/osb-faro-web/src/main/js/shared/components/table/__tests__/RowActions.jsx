import React from 'react';
import RowActions from '../RowActions';
import {shallow} from 'enzyme';

class WrappedRowActions extends React.Component {
	render() {
		return (
			<td
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				<RowActions {...this.props} />
			</td>
		);
	}
}

describe('RowActions', () => {
	it('should render', () => {
		const component = shallow(
			<WrappedRowActions actions={[{label: 'foo'}]} />
		);
		expect(component).toMatchSnapshot();
	});

	it('should render with quick actions', () => {
		const component = shallow(
			<WrappedRowActions
				actions={[{label: 'foo'}]}
				quickActions={[{iconSymbol: 'pencil', label: 'foo'}]}
			/>
		);

		expect(component.find(RowActions).shallow()).toMatchSnapshot();
	});
});
