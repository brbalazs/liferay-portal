import Soy from 'metal-soy';
import template from './MiniumActionsMenu.soy';
import Component from 'metal-component';

class MiniumActionsMenu extends Component {

	_handleToggle(e) {
		const actions = this.refs.actions;
		const row = e.target.closest('tr');
		const width = actions.getBoundingClientRect().width;
		row.style.setProperty('--translate-space', `${width - 10}px`);
		row.classList.toggle('is-active');
	}
}

Soy.register(MiniumActionsMenu, template);

export {MiniumActionsMenu};
export default MiniumActionsMenu;