/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.backend.dto.ProjectDTO;
import com.liferay.osb.asah.backend.dto.ProjectDetailDTO;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.dog.PreferenceDog;
import com.liferay.osb.asah.common.dog.ProjectDog;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.Preference;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author André Miranda
 */
@RequestMapping(produces = "application/json", value = "/projects")
@RestController(
	"com.liferay.osb.asah.backend.rest.controller.ProjectsRestController"
)
public class ProjectsRestController extends BaseRestController {

	@DeleteMapping("/{id}")
	public void deleteProject(
		@RequestParam(defaultValue = "false") boolean deleteData,
		@PathVariable String id) {

		_projectDog.deleteProject(deleteData, id);
	}

	@GetMapping("/details")
	public List<ProjectDetailDTO> getProjectDetailDTOs() {
		List<ProjectDetailDTO> projectDetailDTOs = new ArrayList<>();

		ProjectIdThreadLocal.forProjects(
			_projectDog.getProjects(),
			() -> {
				boolean accountsSelected = false;
				boolean commerceChannelsSelected = false;
				boolean contactsSelected = false;
				List<Long> dataSourceIds = new ArrayList<>();
				boolean sitesSelected = false;

				try {
					for (DataSource dataSource :
							_dataSourceDog.getDataSources()) {

						if (Objects.equals(
								dataSource.getState(), "DISCONNECTED") &&
							Objects.equals(
								dataSource.getStatus(), "INACTIVE")) {

							continue;
						}

						if (BooleanUtils.isTrue(
								dataSource.getAccountsSelected())) {

							accountsSelected = true;
						}

						if (BooleanUtils.isTrue(
								dataSource.getCommerceChannelsSelected())) {

							commerceChannelsSelected = true;
						}

						if (BooleanUtils.isTrue(
								dataSource.getContactsSelected())) {

							contactsSelected = true;
						}

						dataSourceIds.add(dataSource.getId());

						if (BooleanUtils.isTrue(
								dataSource.getSitesSelected())) {

							sitesSelected = true;
						}
					}
				}
				catch (Exception exception) {
					_log.error(exception, exception);
				}

				Preference preference = _preferenceDog.getPreference(
					"time-zone-id");

				projectDetailDTOs.add(
					new ProjectDetailDTO(
						accountsSelected, commerceChannelsSelected,
						contactsSelected, dataSourceIds,
						ProjectIdThreadLocal.getProjectId(), sitesSelected,
						preference.getValue()));
			});

		return projectDetailDTOs;
	}

	@GetMapping
	public List<ProjectDTO> getProjectDTOs() {
		return ListUtil.map(_projectDog.getProjects(), ProjectDTO::new);
	}

	@PostMapping
	public void postProject(@RequestBody ProjectDTO projectDTO) {
		_projectDog.addProject(projectDTO.getId());
	}

	private static final Log _log = LogFactory.getLog(
		ProjectsRestController.class);

	@Autowired
	private DataSourceDog _dataSourceDog;

	@Autowired
	private PreferenceDog _preferenceDog;

	@Autowired
	private ProjectDog _projectDog;

}