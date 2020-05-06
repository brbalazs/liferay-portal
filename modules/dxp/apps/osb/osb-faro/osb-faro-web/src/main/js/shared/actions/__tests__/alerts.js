import {
	actionTypes,
	addAlert,
	alertTypes,
	removeAlert,
	updateAlert
} from '../alerts';

import {isFSA} from 'flux-standard-action';

describe('alerts', () => {
	const alertType = alertTypes.ALERT;
	const id = 123;
	const timeout = false;

	describe('addAlert', () => {
		it('should return an addAlert action', () => {
			const action = addAlert({alertType, timeout});

			expect(isFSA(action)).toBe(true);
			expect(action.type).toBe(actionTypes.ADD_ALERT);
		});

		it('should apply a default timeout', () => {
			const action = addAlert({alertType});

			expect(typeof action).toBe('function');
		});
	});

	describe('updateAlert', () => {
		it('should return a updateAlert action', () => {
			const action = updateAlert({alertType, id, timeout});

			expect(isFSA(action)).toBe(true);
			expect(action.type).toBe(actionTypes.UPDATE_ALERT);
		});

		it('should apply a default timeout', () => {
			const action = updateAlert({alertType});

			expect(typeof action).toBe('function');
		});
	});

	describe('removeAlert', () => {
		it('should return a removeAlert action', () => {
			const action = removeAlert(id);

			expect(isFSA(action)).toBe(true);
			expect(action.type).toBe(actionTypes.REMOVE_ALERT);
		});
	});

	it('should dispatch the action after a timeout', () => {
		const action = addAlert({alertType});

		const dispatchSpy = jest.fn();

		action(dispatchSpy);

		expect(dispatchSpy).toHaveBeenCalledTimes(1);

		jest.runAllTimers();

		expect(dispatchSpy).toHaveBeenCalledTimes(2);
	});

	it('should allow for a custom timeout', () => {
		const action = addAlert({alertType, timeout: 300});

		expect(typeof action).toBe('function');
	});
});
