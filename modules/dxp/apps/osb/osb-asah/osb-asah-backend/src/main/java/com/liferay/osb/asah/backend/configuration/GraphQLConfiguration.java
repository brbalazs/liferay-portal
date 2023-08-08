/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.configuration;

import com.liferay.osb.asah.backend.dto.DXPOrganizationDTO;
import com.liferay.osb.asah.backend.dto.DXPUserDTO;
import com.liferay.osb.asah.backend.model.BlogMetric;
import com.liferay.osb.asah.backend.model.DocumentLibraryMetric;
import com.liferay.osb.asah.backend.model.FormMetric;
import com.liferay.osb.asah.backend.model.JournalMetric;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.GraphQL;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.PropertyDataFetcher;
import graphql.schema.TypeResolver;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.visibility.NoIntrospectionGraphqlFieldVisibility;

import java.io.InputStreamReader;

import java.lang.reflect.Method;

import java.nio.charset.Charset;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * @author André Miranda
 */
@Configuration
public class GraphQLConfiguration {

	@Bean
	public GraphQL graphQL(Environment environment) {
		SchemaGenerator schemaGenerator = new SchemaGenerator();

		RuntimeWiring.Builder runtimeWiringBuilder = _getRuntimeWiringBuilder();

		if (environment.acceptsProfiles("prod")) {
			runtimeWiringBuilder.fieldVisibility(
				NoIntrospectionGraphqlFieldVisibility.
					NO_INTROSPECTION_FIELD_VISIBILITY);
		}

		GraphQL.Builder builder = GraphQL.newGraphQL(
			schemaGenerator.makeExecutableSchema(
				_buildTypeDefinitionRegistry(), runtimeWiringBuilder.build()));

		return builder.build();
	}

	private TypeResolver _buildAssetTypeRevolver() {
		return typeResolutionEnvironment -> {
			Object object = typeResolutionEnvironment.getObject();

			GraphQLSchema graphQLSchema = typeResolutionEnvironment.getSchema();

			if (object instanceof BlogMetric) {
				return (GraphQLObjectType)graphQLSchema.getType("BlogMetric");
			}

			if (object instanceof DocumentLibraryMetric) {
				return (GraphQLObjectType)graphQLSchema.getType(
					"DocumentMetric");
			}

			if (object instanceof FormMetric) {
				return (GraphQLObjectType)graphQLSchema.getType("FormMetric");
			}

			if (object instanceof JournalMetric) {
				return (GraphQLObjectType)graphQLSchema.getType(
					"JournalMetric");
			}

			return (GraphQLObjectType)graphQLSchema.getType("PageMetric");
		};
	}

	private TypeResolver _buildDXPEntityTypeResolver() {
		return typeResolutionEnvironment -> {
			GraphQLSchema graphQLSchema = typeResolutionEnvironment.getSchema();

			if (typeResolutionEnvironment.getObject() instanceof
					DXPOrganizationDTO) {

				return (GraphQLObjectType)graphQLSchema.getType("Organization");
			}

			if (typeResolutionEnvironment.getObject() instanceof DXPUserDTO) {
				return (GraphQLObjectType)graphQLSchema.getType("User");
			}

			return (GraphQLObjectType)graphQLSchema.getType("BaseDXPEntity");
		};
	}

	private TypeDefinitionRegistry _buildTypeDefinitionRegistry() {
		SchemaParser schemaParser = new SchemaParser();

		Class<?> clazz = getClass();

		return schemaParser.parse(
			new InputStreamReader(
				clazz.getResourceAsStream("asah.graphqls"),
				Charset.defaultCharset()));
	}

	private RuntimeWiring.Builder _getRuntimeWiringBuilder() {
		RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();

		builder.type(
			"AssetMetric",
			typeWiring -> typeWiring.typeResolver(_buildAssetTypeRevolver()));
		builder.type(
			"DXPEntity",
			typeWiring -> typeWiring.typeResolver(
				_buildDXPEntityTypeResolver()));

		_wireGraphQLTypeProperty(
			builder, "activities", "results", "ActivityBag");
		_wireGraphQLTypeProperty(
			builder, "assetMetrics", "results", "AssetMetricBag");
		_wireGraphQLTypeProperty(
			builder, "blockedCustomEventDefinitions", "results",
			"BlockedCustomEventDefinitionBag");
		_wireGraphQLTypeProperty(
			builder, "compositions", "results", "CompositionBag");
		_wireGraphQLTypeProperty(
			builder, "confidenceInterval", "confidenceIntervalArray",
			"VariantMetrics");
		_wireGraphQLTypeProperty(
			builder, "dashboards", "results", "DashboardBag");
		_wireGraphQLTypeProperty(
			builder, "dataControlTasks", "results", "DataControlTaskBag");
		_wireGraphQLTypeProperty(
			builder, "dxpEntities", "results", "DXPEntityBag");
		_wireGraphQLTypeProperty(
			builder, "dxpVariantId", "DXPVariantId", "VariantMetrics");
		_wireGraphQLTypeProperty(
			builder, "eventAnalyses", "results", "EventAnalysisBag");
		_wireGraphQLTypeProperty(
			builder, "eventAttributeDefinitions", "results",
			"EventAttributeDefinitionBag");
		_wireGraphQLTypeProperty(
			builder, "eventAttributeValues", "results",
			"EventAttributeValueBag");
		_wireGraphQLTypeProperty(
			builder, "eventDefinitions", "results", "EventDefinitionBag");
		_wireGraphQLTypeProperty(
			builder, "experiments", "results", "ExperimentBag");
		_wireGraphQLTypeProperty(
			builder, "individuals", "results", "IndividualBag");
		_wireGraphQLTypeProperty(builder, "jobRuns", "results", "JobRunBag");
		_wireGraphQLTypeProperty(builder, "jobs", "results", "JobBag");
		_wireGraphQLTypeProperty(builder, "metrics", "results", "MetricBag");
		_wireGraphQLTypeProperty(
			builder, "pageAssets", "results", "PageAssetBag");
		_wireGraphQLTypeProperty(
			builder, "status", "dataExportTaskStatus", "DataExportTask");
		_wireGraphQLTypeProperty(
			builder, "suppressions", "results", "SuppressionBag");
		_wireGraphQLTypeProperty(
			builder, "type", "dataExportTaskType", "DataExportTask");
		_wireGraphQLTypeProperty(
			builder, "variantMetrics", "variantMetricsList",
			"ExperimentMetrics");

		_wireGraphQLTypesProperties(
			builder,
			_scanAnnotatedGraphQLTypes("com.liferay.osb.asah.backend"));

		for (DataFetcher<?> dataFetcher : _dataFetchers) {
			Class<?> clazz = dataFetcher.getClass();

			Set<GraphQLTypeWiring> graphQLTypeWirings =
				AnnotatedElementUtils.findMergedRepeatableAnnotations(
					clazz, GraphQLTypeWiring.class);

			for (GraphQLTypeWiring graphQLTypeWiring : graphQLTypeWirings) {
				builder.type(
					graphQLTypeWiring.typeName(),
					typeWiring -> typeWiring.dataFetcher(
						graphQLTypeWiring.fieldName(), dataFetcher));

				if (_log.isDebugEnabled()) {
					_log.debug(
						String.format(
							"GraphQL type wiring: %s#%s -> %s",
							graphQLTypeWiring.typeName(),
							graphQLTypeWiring.fieldName(),
							clazz.getSimpleName()));
				}
			}
		}

		return builder;
	}

	private Set<Class<?>> _scanAnnotatedGraphQLTypes(String basePackage) {
		ClassPathScanningCandidateComponentProvider
			classPathScanningCandidateComponentProvider =
				new ClassPathScanningCandidateComponentProvider(false);

		classPathScanningCandidateComponentProvider.addIncludeFilter(
			new AnnotationTypeFilter(GraphQLType.class));

		Set<BeanDefinition> beanDefinitions =
			classPathScanningCandidateComponentProvider.findCandidateComponents(
				basePackage);

		Stream<BeanDefinition> stream = beanDefinitions.stream();

		return stream.map(
			beanDefinition -> {
				try {
					return Class.forName(beanDefinition.getBeanClassName());
				}
				catch (ClassNotFoundException classNotFoundException) {
					throw new IllegalStateException(
						"Unable to load class " +
							beanDefinition.getBeanClassName(),
						classNotFoundException);
				}
			}
		).collect(
			Collectors.toSet()
		);
	}

	private void _wireGraphQLTypeProperties(
		RuntimeWiring.Builder builder, Method[] methods, String typeName) {

		for (Method method : methods) {
			GraphQLProperty graphQLProperty = AnnotationUtils.getAnnotation(
				method, GraphQLProperty.class);

			if (graphQLProperty == null) {
				continue;
			}

			_wireGraphQLTypeProperty(
				builder, graphQLProperty.value(),
				StringUtils.removeStart(method.getName(), "get"), typeName);
		}
	}

	private void _wireGraphQLTypeProperty(
		RuntimeWiring.Builder builder, String fieldName, String propertyName,
		String typeName) {

		builder.type(
			typeName,
			typeWiring -> typeWiring.dataFetcher(
				fieldName, new PropertyDataFetcher(propertyName)));
	}

	private void _wireGraphQLTypesProperties(
		RuntimeWiring.Builder builder, Set<Class<?>> graphQLTypesClasses) {

		for (Class<?> graphQLTypesClass : graphQLTypesClasses) {
			GraphQLType graphQLType = AnnotationUtils.getAnnotation(
				graphQLTypesClass, GraphQLType.class);

			if (graphQLType != null) {
				String typeName = graphQLType.value();

				if (StringUtils.isBlank(typeName)) {
					typeName = StringUtils.substringAfterLast(
						graphQLTypesClass.getName(), ".");
				}

				_wireGraphQLTypeProperties(
					builder, graphQLTypesClass.getMethods(), typeName);
			}
		}
	}

	private static final Log _log = LogFactory.getLog(
		GraphQLConfiguration.class);

	@Autowired
	private List<DataFetcher<?>> _dataFetchers;

}