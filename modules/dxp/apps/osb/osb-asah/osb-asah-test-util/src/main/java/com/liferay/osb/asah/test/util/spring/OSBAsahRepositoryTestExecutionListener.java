/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.spring;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.repository.BigQueryRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.util.ResourceTemplateUtil;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.test.context.TestContext;

/**
 * @author Marcos Martins
 */
@TestComponent
public class OSBAsahRepositoryTestExecutionListener
	extends BaseOSBAsahTestExecutionListener {

	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		ProjectIdThreadLocal.setProjectId("test");

		ApplicationContext applicationContext =
			testContext.getApplicationContext();

		Class<?> clazz = testContext.getTestClass();

		RepositoryResource[] repositoryResources = clazz.getAnnotationsByType(
			RepositoryResource.class);

		for (RepositoryResource repositoryResource : repositoryResources) {
			_clearRepository(
				applicationContext, repositoryResource.repositoryClass());
		}

		ProjectIdThreadLocal.remove();
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		ProjectIdThreadLocal.setProjectId("test");

		ApplicationContext applicationContext =
			testContext.getApplicationContext();

		Set<RepositoryResource> repositoryResources =
			AnnotatedElementUtils.findMergedRepeatableAnnotations(
				testContext.getTestMethod(), RepositoryResource.class);

		for (RepositoryResource repositoryResource : repositoryResources) {
			_clearRepository(
				applicationContext, repositoryResource.repositoryClass());
		}

		ProjectIdThreadLocal.remove();
	}

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		super.beforeTestClass(testContext);

		ProjectIdThreadLocal.setProjectId("test");

		Class<?> clazz = testContext.getTestClass();

		RepositoryResource[] repositoryResources = clazz.getAnnotationsByType(
			RepositoryResource.class);

		if (repositoryResources.length == 0) {
			return;
		}

		ApplicationContext applicationContext =
			testContext.getApplicationContext();

		for (RepositoryResource repositoryResource : repositoryResources) {
			_prepareRepository(applicationContext, clazz, repositoryResource);
		}
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		ProjectIdThreadLocal.setProjectId("test");

		ApplicationContext applicationContext =
			testContext.getApplicationContext();

		Set<RepositoryResource> repositoryResources =
			AnnotatedElementUtils.findMergedRepeatableAnnotations(
				testContext.getTestMethod(), RepositoryResource.class);

		for (RepositoryResource repositoryResource : repositoryResources) {
			_prepareRepository(
				applicationContext, testContext.getTestClass(),
				repositoryResource);
		}
	}

	private void _clearRepository(
		ApplicationContext applicationContext, Class<?> repositoryClass) {

		Repository repository = (Repository)applicationContext.getBean(
			repositoryClass);

		if (repository instanceof PagingAndSortingRepository) {
			PagingAndSortingRepository pagingAndSortingRepository =
				(PagingAndSortingRepository)repository;

			pagingAndSortingRepository.deleteAll();
		}
	}

	private Class<?> _getEntityClass(Class<?> repositoryClass) {
		Type[] types = repositoryClass.getGenericInterfaces();

		ParameterizedType parameterizedType = null;

		for (Type type : types) {
			if (type instanceof ParameterizedType) {
				parameterizedType = (ParameterizedType)type;
			}
		}

		if (parameterizedType == null) {
			throw new IllegalArgumentException();
		}

		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

		return (Class<?>)actualTypeArguments[0];
	}

	private void _prepareRepository(
			ApplicationContext applicationContext, Class<?> clazz,
			RepositoryResource repositoryResource)
		throws Exception {

		Repository repository = (Repository)applicationContext.getBean(
			repositoryResource.repositoryClass());

		JSONArray jsonArray = new JSONArray(
			ResourceTemplateUtil.replaceVariables(
				ResourceUtil.readResourceToString(
					"dependencies/" + repositoryResource.resourcePath(),
					clazz)));

		jsonArray.forEach(
			object -> {
				JSONObject jsonObject = (JSONObject)object;

				if (!jsonObject.has("isNew")) {
					jsonObject.put("isNew", true);
				}
			});

		List<Object> jsonObjectList = jsonArray.toList();

		Stream<Object> stream = jsonObjectList.stream();

		Class<?> entityClass = _getEntityClass(
			repositoryResource.repositoryClass());

		List<?> entities = stream.map(
			object -> _objectMapper.convertValue(object, entityClass)
		).collect(
			Collectors.toList()
		);

		if (repository instanceof PagingAndSortingRepository) {
			PagingAndSortingRepository pagingAndSortingRepository =
				(PagingAndSortingRepository)repository;

			pagingAndSortingRepository.saveAll(entities);
		}
		else if (repository instanceof BigQueryRepository) {
			Class<? extends Repository> repositoryClass = repository.getClass();

			Method insertMethod = repositoryClass.getMethod(
				"insert", entityClass);

			entities.forEach(
				entity -> {
					try {
						insertMethod.invoke(repository, entity);
					}
					catch (Exception exception) {
						exception.printStackTrace();
					}
				});
		}
	}

	@Autowired
	private ObjectMapper _objectMapper;

}