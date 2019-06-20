import React, {Component} from 'react';
import PropTypes from 'prop-types';

import w from 'window';

const COMPONENT_CLASS = 'pane-org-info-actions';

class ContextualOptions extends Component {
	componentDidMount() {
		const {onFocusOut} = this.props,
			doBlur = (e) => {
				!(e.target.classList.contains(COMPONENT_CLASS) ||
					e.target.parentElement.classList.contains(COMPONENT_CLASS)) &&
				onFocusOut();

				w.removeEventListener('click', doBlur);
			};

		setTimeout(() => {
			w.addEventListener('click', doBlur);
		}, 100);
	}

	render() {
		const {
			onCreate,
			onDelete,
			onFocusOut
		} = this.props;

		return (
			<div onBlur={onFocusOut} tabIndex="10" role="button"
				 className={`pane-org-info-actions ${COMPONENT_CLASS}-menu`}>

				<ul className={COMPONENT_CLASS}>
					<li onClick={onCreate} role="button">Create</li>
					<li onClick={onDelete} role="button">Delete</li>
				</ul>
			</div>
		);
	}
}

ContextualOptions.defaultProps = {
	onCreate: () => {
	},
	onDelete: () => {
	},
	onFocusOut: () => {
	}
};

ContextualOptions.propTypes = {
	onCreate: PropTypes.func,
	onDelete: PropTypes.func,
	onFocusOut: PropTypes.func
};

export default ContextualOptions;
