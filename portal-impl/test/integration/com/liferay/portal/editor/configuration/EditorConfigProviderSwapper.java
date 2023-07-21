/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.editor.configuration;

import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.test.ReflectionTestUtil;

import java.io.Closeable;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Shuyang Zhou
 */
public class EditorConfigProviderSwapper implements Closeable {

	public EditorConfigProviderSwapper(final List<Class<?>> classes) {
		_editorConfigProvider = ReflectionTestUtil.getFieldValue(
			EditorConfigurationFactoryImpl.class, "_editorConfigProvider");

		ReflectionTestUtil.setFieldValue(
			EditorConfigurationFactoryImpl.class, "_editorConfigProvider",
			new EditorConfigProvider() {

				@Override
				protected void visitEditorContributors(
					final Consumer<EditorConfigContributor> consumer,
					String portletName, String editorConfigKey,
					String editorName) {

					super.visitEditorContributors(
						editorConfigContributor -> {
							if (classes.contains(
									editorConfigContributor.getClass())) {

								consumer.accept(editorConfigContributor);
							}
						},
						portletName, editorConfigKey, editorName);
				}

			});
	}

	@Override
	public void close() {
		ReflectionTestUtil.setFieldValue(
			EditorConfigurationFactoryImpl.class, "_editorConfigProvider",
			_editorConfigProvider);
	}

	private final EditorConfigProvider _editorConfigProvider;

}