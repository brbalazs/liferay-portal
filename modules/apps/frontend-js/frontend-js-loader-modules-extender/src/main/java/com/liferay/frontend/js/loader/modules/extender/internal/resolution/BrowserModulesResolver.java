/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.loader.modules.extender.internal.resolution;

import com.liferay.frontend.js.loader.modules.extender.internal.Details;
import com.liferay.frontend.js.loader.modules.extender.internal.config.generator.JSConfigGeneratorModule;
import com.liferay.frontend.js.loader.modules.extender.internal.config.generator.JSConfigGeneratorPackage;
import com.liferay.frontend.js.loader.modules.extender.internal.config.generator.JSConfigGeneratorPackagesTracker;
import com.liferay.frontend.js.loader.modules.extender.internal.resolution.adapter.JSBrowserModule;
import com.liferay.frontend.js.loader.modules.extender.internal.resolution.adapter.JSConfigGeneratorBrowserModule;
import com.liferay.frontend.js.loader.modules.extender.npm.JSModule;
import com.liferay.frontend.js.loader.modules.extender.npm.JSModuleAlias;
import com.liferay.frontend.js.loader.modules.extender.npm.JSPackage;
import com.liferay.frontend.js.loader.modules.extender.npm.ModuleNameUtil;
import com.liferay.frontend.js.loader.modules.extender.npm.NPMRegistry;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.url.builder.AbsolutePortalURLBuilder;
import com.liferay.portal.url.builder.AbsolutePortalURLBuilderFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodolfo Roza Miranda
 */
@Component(
	configurationPid = "com.liferay.frontend.js.loader.modules.extender.internal.Details",
	immediate = true, service = BrowserModulesResolver.class
)
public class BrowserModulesResolver {

	public BrowserModulesResolution resolve(
		List<String> moduleNames, HttpServletRequest httpServletRequest) {

		BrowserModulesResolution browserModulesResolution =
			new BrowserModulesResolution(
				_jsonFactory, _details.explainResolutions());

		Map<String, BrowserModule> browserModulesMap = _getBrowserModulesMap();

		for (String moduleName : moduleNames) {
			_resolve(
				browserModulesMap, moduleName, browserModulesResolution,
				httpServletRequest);
		}

		_populateMappedModuleNames(browserModulesResolution);

		return browserModulesResolution;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_details = ConfigurableUtil.createConfigurable(
			Details.class, properties);
	}

	private Map<String, BrowserModule> _getBrowserModulesMap() {
		Map<String, BrowserModule> browserModulesMap = new HashMap<>();

		for (JSConfigGeneratorPackage jsConfigGeneratorPackage :
				_jsConfigGeneratorPackagesTracker.
					getJSConfigGeneratorPackages()) {

			for (JSConfigGeneratorModule jsConfigGeneratorModule :
					jsConfigGeneratorPackage.getJSConfigGeneratorModules()) {

				JSConfigGeneratorBrowserModule jsConfigGeneratorBrowserModule =
					new JSConfigGeneratorBrowserModule(jsConfigGeneratorModule);

				browserModulesMap.put(
					jsConfigGeneratorBrowserModule.getName(),
					jsConfigGeneratorBrowserModule);
			}
		}

		for (JSModule jsModule : _npmRegistry.getResolvedJSModules()) {
			JSBrowserModule jsBrowserModule = new JSBrowserModule(
				jsModule, _npmRegistry);

			browserModulesMap.put(jsBrowserModule.getName(), jsBrowserModule);
		}

		return browserModulesMap;
	}

	private String _getDependencyPath(String moduleName, String dependency) {
		if (!ModuleNameUtil.isLocalModuleName(dependency)) {
			return dependency;
		}

		List<String> moduleDirNameParts = _getDirNameParts(moduleName);

		List<String> dependencyDirNameParts = _getDirNameParts(dependency);

		for (String dependencyDirNamePart : dependencyDirNameParts) {
			if (dependencyDirNamePart.equals(StringPool.PERIOD)) {
				continue;
			}

			if (dependencyDirNamePart.equals(StringPool.DOUBLE_PERIOD)) {
				if (moduleDirNameParts.isEmpty()) {
					throw new IllegalArgumentException(
						"Invalid dependency " + dependency);
				}

				moduleDirNameParts.remove(moduleDirNameParts.size() - 1);
			}
			else {
				moduleDirNameParts.add(dependencyDirNamePart);
			}
		}

		moduleDirNameParts.add(_getFileName(dependency));

		return StringUtil.merge(moduleDirNameParts, StringPool.SLASH);
	}

	private List<String> _getDirNameParts(String modulePath) {
		List<String> modulePathParts = new ArrayList<>(
			Arrays.asList(modulePath.split(StringPool.SLASH)));

		modulePathParts.remove(modulePathParts.size() - 1);

		return modulePathParts;
	}

	private String _getFileName(String dependency) {
		int pos = dependency.lastIndexOf(StringPool.SLASH);

		return dependency.substring(pos + 1);
	}

	private JSPackage _getResolvedJSPackage(String packageName) {
		Collection<JSPackage> resolvedJSPackages =
			_npmRegistry.getResolvedJSPackages();

		for (JSPackage jsPackage : resolvedJSPackages) {
			if (packageName.equals(jsPackage.getResolvedId())) {
				return jsPackage;
			}
		}

		return null;
	}

	private void _populateMappedModuleNames(
		BrowserModulesResolution browserModulesResolution) {

		Set<JSPackage> jsPackages = new HashSet<>();

		for (String moduleName :
				browserModulesResolution.getResolvedModuleNames()) {

			String packageName = ModuleNameUtil.getPackageName(moduleName);

			JSPackage jsPackage = _getResolvedJSPackage(packageName);

			if (jsPackage == null) {
				continue;
			}

			jsPackages.add(jsPackage);
		}

		for (JSPackage jsPackage : jsPackages) {
			for (JSModuleAlias jsModuleAlias : jsPackage.getJSModuleAliases()) {
				browserModulesResolution.putMappedModuleName(
					jsPackage.getResolvedId() + StringPool.SLASH +
						jsModuleAlias.getAlias(),
					jsPackage.getResolvedId() + StringPool.SLASH +
						jsModuleAlias.getModuleName(),
					true);
			}
		}
	}

	private boolean _processBrowserModule(
		Map<String, BrowserModule> browserModulesMap,
		BrowserModule browserModule,
		BrowserModulesResolution browserModulesResolution,
		HttpServletRequest httpServletRequest) {

		String moduleName = browserModule.getName();

		if (browserModulesResolution.isProcessedModuleName(moduleName)) {
			return false;
		}

		browserModulesResolution.addProcessedModuleName(moduleName);

		Map<String, String> dependenciesMap = new HashMap<>();

		browserModulesResolution.indentExplanation();

		for (String dependency : browserModule.getDependencies()) {
			if (ModuleNameUtil.isReservedModuleName(dependency)) {
				continue;
			}

			String dependencyModuleName = _getDependencyPath(
				moduleName, dependency);

			dependencyModuleName = _browserModuleNameMapper.mapModuleName(
				dependencyModuleName, browserModule.getDependenciesMap());

			dependenciesMap.put(dependency, dependencyModuleName);

			BrowserModule dependencyBrowserModule = browserModulesMap.get(
				dependencyModuleName);

			if (dependencyBrowserModule != null) {
				_processBrowserModule(
					browserModulesMap, dependencyBrowserModule,
					browserModulesResolution, httpServletRequest);
			}
		}

		browserModulesResolution.dedentExplanation();

		browserModulesResolution.putDependenciesMap(
			moduleName, dependenciesMap);

		AbsolutePortalURLBuilder absolutePortalURLBuilder =
			_absolutePortalURLBuilderFactory.getAbsolutePortalURLBuilder(
				httpServletRequest);

		absolutePortalURLBuilder.ignoreCDNHost();

		browserModulesResolution.putPath(
			moduleName,
			absolutePortalURLBuilder.forResource(
				browserModule.getPath()
			).build());

		browserModulesResolution.addResolvedModuleName(moduleName);

		return true;
	}

	private void _resolve(
		Map<String, BrowserModule> browserModulesMap, String moduleName,
		BrowserModulesResolution browserModulesResolution,
		HttpServletRequest httpServletRequest) {

		String mappedModuleName = _browserModuleNameMapper.mapModuleName(
			moduleName);

		BrowserModule browserModule = browserModulesMap.get(mappedModuleName);

		if (browserModule == null) {
			browserModulesResolution.addResolvedModuleName(
				":ERROR:Missing required module '" + moduleName + "'");

			return;
		}

		if (!moduleName.equals(mappedModuleName)) {
			browserModulesResolution.putMappedModuleName(
				moduleName, mappedModuleName, false);
		}

		_processBrowserModule(
			browserModulesMap, browserModule, browserModulesResolution,
			httpServletRequest);
	}

	@Reference
	private AbsolutePortalURLBuilderFactory _absolutePortalURLBuilderFactory;

	@Reference
	private BrowserModuleNameMapper _browserModuleNameMapper;

	private Details _details;

	@Reference
	private JSConfigGeneratorPackagesTracker _jsConfigGeneratorPackagesTracker;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private NPMRegistry _npmRegistry;

	@Reference
	private Portal _portal;

}