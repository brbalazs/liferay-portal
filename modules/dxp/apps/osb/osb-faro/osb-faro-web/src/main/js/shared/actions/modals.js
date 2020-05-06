import {Modal} from 'shared/types';

const actionTypes = Modal.actionTypes;
const modalTypes = Modal.modalTypes;
export {actionTypes, modalTypes};

export function close() {
	return {
		type: actionTypes.CLOSE_MODAL
	};
}

export function closeAll() {
	return {
		type: actionTypes.CLOSE_ALL_MODALS
	};
}

export function open(type, props = {}, options = {}) {
	const {closeOnBlur = true} = options;

	return {
		payload: {
			closeOnBlur,
			props,
			type
		},
		type: actionTypes.OPEN_MODAL
	};
}
