import reducer from '../project-states';
import {fromJS, OrderedMap} from 'immutable';
import {mockProjectState} from 'test/data';
import {actionTypes as projectsActionTypes} from '../../actions/projects';
import {RemoteData} from 'shared/util/records';

describe('Project States Reducer', () => {
	it('should be a function', () => {
		expect(typeof reducer).toBe('function');
	});

	it('should update the projectState key on UPDATE_PROJECT_SUCCESS', () => {
		const newId = 'bar';
		const prevId = 'foo';

		const action = {
			meta: {newId, prevId},
			payload: {entities: {projects: {[prevId]: {data: {}}}}},
			type: projectsActionTypes.UPDATE_PROJECT_SUCCESS
		};

		const prevState = new OrderedMap({
			[prevId]: new RemoteData({
				data: fromJS(mockProjectState(1))
			})
		});

		expect(prevState.get(newId)).toBeFalsy();
		expect(prevState.get(prevId)).toBeTruthy();

		const newState = reducer(prevState, action);

		expect(newState.get(newId)).toBeTruthy();
		expect(newState.get(prevId)).toBeFalsy();
	});
});
