import Promise from 'metal-promise';
import useDeepEqualEffect from './useDeepEqualEffect';
import {useRef, useState} from 'react';

const useRequest = (
	dataSourceFn: (params: {[key: string]: any}) => Promise<any>,
	variables: {[key: string]: any},
	normalize = val => val
) => {
	const requestRef = useRef<Promise>();
	const getData = () => {
		setState({...state, loading: true});

		requestRef.current = dataSourceFn(variables)
			.then(result => {
				setState({...state, data: normalize(result), loading: false});
			})
			.catch(
				err =>
					!err.IS_CANCELLATION_ERROR &&
					setState({...state, error: true, loading: false})
			);
	};

	const [state, setState] = useState({
		data: null,
		error: false,
		loading: true,
		refetch: getData
	});

	useDeepEqualEffect(() => {
		getData();

		return () => requestRef.current && requestRef.current.cancel();
	}, [variables]);

	return state;
};

export default useRequest;
