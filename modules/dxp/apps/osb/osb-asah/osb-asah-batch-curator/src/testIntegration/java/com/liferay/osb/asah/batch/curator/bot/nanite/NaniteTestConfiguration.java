/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.entity.AsahTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

/**
 * @author Michael Bowerman
 */
@TestConfiguration
public class NaniteTestConfiguration {

	@Autowired
	public NaniteTestConfiguration(@Lazy List<Nanite> nanites) {
		_nanites = nanites;
	}

	@Bean
	@Lazy
	@Primary
	public AsahTaskDog asahTaskDog() {
		AsahTaskDog asahTaskDog = Mockito.spy(AsahTaskDog.class);

		Mockito.doAnswer(
			invocation -> {
				AsahTask asahTask = invocation.getArgument(0, AsahTask.class);

				if (_nanitesMap.isEmpty()) {
					for (Nanite nanite : _nanites) {
						Class<?> clazz = nanite.getClass();

						_nanitesMap.put(clazz.getSimpleName(), nanite);
					}
				}

				Nanite nanite = _nanitesMap.get(asahTask.getClassName());

				if (nanite == null) {
					throw new IllegalArgumentException(
						"Unable to find nanite with class name " +
							asahTask.getClassName());
				}

				nanite.run(asahTask.getContextJSONObject());

				return null;
			}
		).when(
			asahTaskDog
		).scheduleAsahTask(
			ArgumentMatchers.any(AsahTask.class)
		);

		return asahTaskDog;
	}

	private final List<Nanite> _nanites;
	private final Map<String, Nanite> _nanitesMap = new HashMap<>();

}