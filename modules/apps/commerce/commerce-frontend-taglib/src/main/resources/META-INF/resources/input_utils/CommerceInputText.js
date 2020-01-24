'use strict';

import Component from 'metal-component';
import Soy, {Config} from 'metal-soy';

import template from './CommerceInputText.soy';

class CommerceInputText extends Component {}

Soy.register(CommerceInputText, template);

CommerceInputText.STATE = {
	contextName: Config.string(),
	label: Config.string(),
	name: Config.string(),
	pattern: Config.any(),
	required: Config.bool(),
	type: Config.string(),
	value: Config.string(),
	_handleInputKeyUp: Config.func()
};

export {CommerceInputText};
export default CommerceInputText;
