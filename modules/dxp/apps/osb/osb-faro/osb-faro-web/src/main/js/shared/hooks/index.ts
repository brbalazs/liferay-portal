import {useEffect, useState} from 'react';

export {default as useDeepEqualEffect} from './useDeepEqualEffect';
export {default as useRequest} from './useRequest';

export const useDebounce = (value: any, delay: number) => {
	const [debouncedValue, setDebouncedValue] = useState(value);

	useEffect(
		() => {
			const handler = setTimeout(() => {
				setDebouncedValue(value);
			}, delay);

			return () => {
				clearTimeout(handler);
			};
		},
		// This is required when the `object` has lost the
		// reference plus the values are the same, `useEffect`
		// uses `Object.is` or equivalent under the covers.
		// For some reason the reference is being lost.
		typeof value === 'object' && value !== null
			? [...Object.keys(value), ...Object.values(value)]
			: [value]
	);

	return debouncedValue;
};

export const useFakeLoading = (data: any, time: number = 125) => {
	const [loading, setLoading] = useState(false);

	useEffect(() => {
		setLoading(true);

		setTimeout(() => setLoading(false), time);
	}, [data]);

	return loading;
};
