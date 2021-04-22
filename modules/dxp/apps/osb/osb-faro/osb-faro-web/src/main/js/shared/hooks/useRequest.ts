import Promise from 'metal-promise';
import useDeepEqualEffect from './useDeepEqualEffect';
import {debounce} from 'lodash/fp';
import {useCallback, useRef, useState} from 'react';

const useRequest = (
	dataSourceFn: (params: {[key: string]: any}) => Promise<any>,
	variables: {[key: string]: any},
	normalize = val => val,
	debounceDelay: number = 0
) => {
	const requestRef = useRef<Promise>();
	const debounceRef = useRef<any>();

	const debouncedDataSourceFn = useCallback<any>(
		debounce(debounceDelay)(vars => {
			requestRef.current = dataSourceFn(vars)
				.then(result => {
					setState({
						...state,
						data: normalize(result),
						loading: false
					});
				})
				.catch(
					err =>
						!err.IS_CANCELLATION_ERROR &&
						setState({...state, error: true, loading: false})
				);
		}),
		[]
	);

	const getData = () => {
		setState({...state, loading: true});

		debounceRef.current = debouncedDataSourceFn(variables);
	};

	const [state, setState] = useState({
		data: null,
		error: false,
		loading: true,
		refetch: getData
	});

	useDeepEqualEffect(() => {
		getData();

		return () => {
			debounceRef.current?.cancel();
			requestRef.current?.cancel();
		};
	}, [variables]);

	return state;
};

export default useRequest;
