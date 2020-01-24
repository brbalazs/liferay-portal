AUI.add(
	'liferay-commerce-frontend-management-bar-state',
	A => {
		A.Do.before(
			state => {
				if (state.owner === 'liferay.component') {
					return new A.Do.Halt(null);
				}
			},
			Liferay.ManagementBar,
			'testRestoreTask'
		);
	},
	'',
	{
		requires: ['liferay-management-bar']
	}
);
