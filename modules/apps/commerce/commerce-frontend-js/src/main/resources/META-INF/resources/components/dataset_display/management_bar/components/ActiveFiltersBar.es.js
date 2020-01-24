import ClayButton from '@clayui/button';
import React from 'react';

import getAppContext from './Context.es';
import FilterResume from './filters/Resume.es';

function ActiveFiltersBar(props) {
	const {actions, state} = getAppContext();

	const filtersActive = state.filters.reduce(
		(acc, filter) =>
			filter.value && !filter.invisible && !filter.main
				? acc.concat(filter.id)
				: acc,
		[]
	);

	return filtersActive.length ? (
		<div className="management-bar management-bar-light navbar navbar-expand-md border-bottom">
			<div className="container-fluid container-fluid-max-xl">
				<nav className="mb-0 py-3 subnav-tbar subnav-tbar-light subnav-tbar-primary w-100">
					<ul className="tbar-nav">
						<li className="p-0 tbar-item tbar-item-expand">
							<div className="tbar-section">
								{filtersActive.map(id => {
									const filter = state.filters.reduce(
										(found, filter) =>
											found ||
											(filter.id === id ? filter : null),
										null
									);

									if (!filter) {
										throw new Error(
											`Filter "${id}" not found.`
										);
									}

									return (
										<FilterResume
											disabled={props.disabled}
											key={filter.id}
											{...filter}
										/>
									);
								})}
							</div>
						</li>
						<li className="tbar-item">
							<div className="tbar-section">
								<ClayButton
									className=" tbar-link btn-sm"
									disabled={props.disabled}
									displayType="link"
									onClick={actions.resetFiltersValue}
								>
									{Liferay.Language.get('reset-filters')}
								</ClayButton>
							</div>
						</li>
					</ul>
				</nav>
			</div>
		</div>
	) : null;
}

export default ActiveFiltersBar;
