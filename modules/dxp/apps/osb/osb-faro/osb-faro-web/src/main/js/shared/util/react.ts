import {isEqual} from 'lodash';

export const getDisplayName = WrappedComponent =>
	WrappedComponent.displayName || WrappedComponent.name || 'Component';

/**
 * Compare previous state or props object by provided keys to detect changes.
 */
export function hasChanges<T>(
	prev: T | object,
	next: T | object,
	...keys: string[]
): boolean {
	for (const key of keys) {
		if (key in next) {
			const newVal = next[key];

			const prevVal = prev[key];

			if (!isEqual(newVal, prevVal)) {
				return true;
			}
		}
	}

	return false;
}
