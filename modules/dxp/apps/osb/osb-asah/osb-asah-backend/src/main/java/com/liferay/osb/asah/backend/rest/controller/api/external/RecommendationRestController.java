/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.api.external;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.backend.rest.controller.BaseRestController;
import com.liferay.osb.asah.common.dog.AssetDog;
import com.liferay.osb.asah.common.dog.JobDog;
import com.liferay.osb.asah.common.dog.RecommendationDog;
import com.liferay.osb.asah.common.entity.Asset;
import com.liferay.osb.asah.common.entity.AssetKeyword;
import com.liferay.osb.asah.common.entity.ItemRecommendation;
import com.liferay.osb.asah.common.entity.Job;
import com.liferay.osb.asah.common.model.JobStatus;
import com.liferay.osb.asah.common.model.PropertyFilter;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.ListUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcellus Tavares
 */
@RequestMapping(produces = "application/json", value = "/api/recommendations")
@RestController
public class RecommendationRestController extends BaseRestController {

	@GetMapping
	public RepresentationModel getLinksRepresentationModel() {
		return new RepresentationModel() {
			{
				add(
					Arrays.asList(
						WebMvcLinkBuilder.linkTo(
							_getModelResultBagEntityModel(null, null)
						).withRel(
							"models"
						)));
			}
		};
	}

	@GetMapping("/models/{modelId}")
	public EntityModel<Model> getModelEntityModel(
		@PathVariable String modelId) {

		return _toModelEntityModel(_jobDog.getJob(Long.valueOf(modelId)));
	}

	@GetMapping("/models")
	public ResultBagEntityModel<Model> getModelResultBagEntityModel(
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "") String keywords) {

		Page<Job> jobPage = _jobDog.getJobPage(
			keywords, page, _PAGE_SIZE, Sort.desc("id"));

		return _toResultBagEntityModel(
			_getModelResultBagEntityModel(page + 1, keywords), page,
			_getModelResultBagEntityModel(page - 1, keywords),
			new ResultBag<>(jobPage.getContent(), jobPage.getTotalElements()),
			this::_toModelEntityModel);
	}

	@PostMapping("/page-recommendations")
	public EntityModel<PageRecommendation> getPageRecommendationEntityModel(
		@RequestBody String json) {

		JSONObject jsonObject = new JSONObject(json);

		return getPageRecommendationEntityModel(
			jsonObject.getString("modelId"),
			DigestUtils.sha1Hex(
				jsonObject.getString("modelId") + jsonObject.getString("url")));
	}

	@GetMapping("/models/{modelId}/page-recommendations/{recommendationId}")
	public EntityModel<PageRecommendation> getPageRecommendationEntityModel(
		@PathVariable String modelId, @PathVariable String recommendationId) {

		_checkJobStatus(modelId);

		return _toPageRecommendationEntityModel(
			_recommendationDog.getItemRecommendation(recommendationId));
	}

	@GetMapping("/models/{modelId}/page-recommendations")
	public ResultBagEntityModel<PageRecommendation>
		getPageRecommendationResultBagEntityModel(
			@PathVariable String modelId,
			@RequestParam(defaultValue = "0") Integer page) {

		_checkJobStatus(modelId);

		Page<ItemRecommendation> itemRecommendationPage =
			_recommendationDog.getItemRecommendationPage(
				Long.valueOf(modelId), page, _PAGE_SIZE, Sort.asc("itemId"));

		return _toResultBagEntityModel(
			_getPageRecommendationResultBagEntityModel(modelId, page + 1), page,
			_getPageRecommendationResultBagEntityModel(modelId, page - 1),
			new ResultBag<>(
				itemRecommendationPage.getContent(),
				itemRecommendationPage.getTotalElements()),
			this::_toPageRecommendationEntityModel);
	}

	private void _checkJobStatus(String jobId) {
		JobStatus jobStatus = _jobDog.getJobStatus(Long.valueOf(jobId));

		if (jobStatus != JobStatus.READY) {
			throw new OSBAsahException(
				HttpStatus.CONFLICT,
				"Please try again later when the model status is ready");
		}
	}

	private void _expandPageRecommendationAttributes(
		PageRecommendation pageRecommendation) {

		Asset asset = _fetchAsset(pageRecommendation.getURL());

		if (asset == null) {
			return;
		}

		pageRecommendation.setDescription(asset.getDescription());
		pageRecommendation.setTitle(asset.getTitle());
		pageRecommendation.setKeywords(
			ListUtil.map(asset.getAssetKeywords(), AssetKeyword::getKeyword));
	}

	private Asset _fetchAsset(String canonicalUrl) {
		if (StringUtils.isBlank(canonicalUrl)) {
			return null;
		}

		PropertyFilter propertyFilter = new PropertyFilter(
			"canonicalUrl = " + canonicalUrl, false);

		Page<Asset> assetPage = _assetDog.getAssetPage(
			"Page", propertyFilter.toFilterString(), null, 0, 1,
			Sort.desc("id"));

		if (assetPage.getTotalElements() == 1) {
			List<Asset> assets = assetPage.getContent();

			return assets.get(0);
		}

		return null;
	}

	private ResultBagEntityModel<Model> _getModelResultBagEntityModel(
		Integer page, String keywords) {

		return WebMvcLinkBuilder.methodOn(
			RecommendationRestController.class
		).getModelResultBagEntityModel(
			page, keywords
		);
	}

	private ResultBagEntityModel<PageRecommendation>
		_getPageRecommendationResultBagEntityModel(
			String modelId, Integer page) {

		return WebMvcLinkBuilder.methodOn(
			RecommendationRestController.class
		).getPageRecommendationResultBagEntityModel(
			modelId, page
		);
	}

	private <T, R> List<EntityModel<R>> _toListEntityModel(
		List<T> results,
		Function<T, EntityModel<R>> resultEntityModelMapperFunction) {

		return ListUtil.map(results, resultEntityModelMapperFunction);
	}

	private EntityModel<Model> _toModelEntityModel(Job job) {
		return EntityModel.of(
			new Model(job, _jobDog.getJobStatus(job.getId())),
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					RecommendationRestController.class
				).getModelEntityModel(
					String.valueOf(job.getId())
				)
			).withSelfRel());
	}

	private PageRecommendation _toPageRecommendation(
		ItemRecommendation itemRecommendation) {

		PageRecommendation pageRecommendation = new PageRecommendation();

		pageRecommendation.setId(itemRecommendation.getId());
		pageRecommendation.setJobId(
			String.valueOf(itemRecommendation.getJobId()));
		pageRecommendation.setURL(itemRecommendation.getItemId());
		pageRecommendation.setPageRecommendations(
			ListUtil.map(
				itemRecommendation.getRecommendedItemIds(),
				recommendedItemId -> _toPageRecommendation(
					String.valueOf(itemRecommendation.getJobId()),
					recommendedItemId)));

		_expandPageRecommendationAttributes(pageRecommendation);

		return pageRecommendation;
	}

	private PageRecommendation _toPageRecommendation(String jobId, String url) {
		PageRecommendation pageRecommendation = new PageRecommendation();

		pageRecommendation.setId(DigestUtils.sha1Hex(jobId + url));
		pageRecommendation.setJobId(jobId);
		pageRecommendation.setURL(url);

		_expandPageRecommendationAttributes(pageRecommendation);

		return pageRecommendation;
	}

	private EntityModel<PageRecommendation> _toPageRecommendationEntityModel(
		ItemRecommendation itemRecommendation) {

		return EntityModel.of(
			_toPageRecommendation(itemRecommendation),
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					RecommendationRestController.class
				).getPageRecommendationEntityModel(
					String.valueOf(itemRecommendation.getJobId()),
					itemRecommendation.getId()
				)
			).withSelfRel());
	}

	private <T, R> ResultBagEntityModel<R> _toResultBagEntityModel(
		Object nextPageMethodInvocation, int page,
		Object prevPageMethodInvocation, ResultBag<T> resultBag,
		Function<T, EntityModel<R>> resultEntityModelMapperFunction) {

		ResultBagEntityModel<R> resultBagResource = new ResultBagEntityModel<>(
			new ResultBag<>(
				_toListEntityModel(
					resultBag.getResults(), resultEntityModelMapperFunction),
				resultBag.getTotal()));

		if (((page + 1L) * _PAGE_SIZE) < resultBag.getTotal()) {
			resultBagResource.add(
				WebMvcLinkBuilder.linkTo(
					nextPageMethodInvocation
				).withRel(
					"next"
				));
		}

		if (page > 0) {
			resultBagResource.add(
				WebMvcLinkBuilder.linkTo(
					prevPageMethodInvocation
				).withRel(
					"prev"
				));
		}

		return resultBagResource;
	}

	private static final int _PAGE_SIZE = 20;

	@Autowired
	private AssetDog _assetDog;

	@Autowired
	private JobDog _jobDog;

	@Autowired
	private RecommendationDog _recommendationDog;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class Model extends Job {

		public Model(Job job, JobStatus jobStatus) {
			setId(job.getId());
			setJobParameters(job.getJobParameters());
			setJobStatus(jobStatus);
			setJobRunFrequency(job.getJobRunFrequency());
			setJobRunDataPeriod(job.getJobRunDataPeriod());
			setJobType(job.getJobType());
			setName(job.getName());
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!super.equals(obj) || !(obj instanceof Model)) {
				return false;
			}

			Model model = (Model)obj;

			if (super.equalsJob(model) &&
				Objects.equals(_jobStatus, model._jobStatus)) {

				return true;
			}

			return false;
		}

		@JsonProperty("status")
		public JobStatus getStatus() {
			return _jobStatus;
		}

		@Override
		public int hashCode() {
			return super.hashCode() ^ Objects.hash(_jobStatus);
		}

		public void setJobStatus(JobStatus jobStatus) {
			_jobStatus = jobStatus;
		}

		private JobStatus _jobStatus;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class PageRecommendation {

		public String getDescription() {
			return _description;
		}

		@JsonIgnore
		public String getId() {
			return _id;
		}

		@JsonIgnore
		public String getJobId() {
			return _jobId;
		}

		public List<String> getKeywords() {
			return _keywords;
		}

		@JsonProperty("recommendedPages")
		public List<EntityModel<PageRecommendation>>
			getPageRecommendationEntityModels() {

			if (_pageRecommendations == null) {
				return null;
			}

			return ListUtil.map(
				_pageRecommendations, this::_toPageRecommendationEntityModel);
		}

		@JsonIgnore
		public List<PageRecommendation> getPageRecommendations() {
			return _pageRecommendations;
		}

		public String getTitle() {
			return _title;
		}

		public String getURL() {
			return _url;
		}

		public void setDescription(String description) {
			_description = description;
		}

		public void setId(String id) {
			_id = id;
		}

		public void setJobId(String jobId) {
			_jobId = jobId;
		}

		public void setKeywords(List<String> keywords) {
			_keywords = keywords;
		}

		public void setPageRecommendations(
			List<PageRecommendation> pageRecommendations) {

			_pageRecommendations = pageRecommendations;
		}

		public void setTitle(String title) {
			_title = title;
		}

		public void setURL(String url) {
			_url = url;
		}

		private EntityModel<PageRecommendation>
			_toPageRecommendationEntityModel(
				PageRecommendation pageRecommendation) {

			return EntityModel.of(
				pageRecommendation,
				WebMvcLinkBuilder.linkTo(
					WebMvcLinkBuilder.methodOn(
						RecommendationRestController.class
					).getPageRecommendationEntityModel(
						pageRecommendation.getJobId(),
						pageRecommendation.getId()
					)
				).withSelfRel());
		}

		private String _description;
		private String _id;
		private String _jobId;
		private List<String> _keywords;
		private List<PageRecommendation> _pageRecommendations;
		private String _title;
		private String _url;

	}

	private static class ResultBagEntityModel<T>
		extends EntityModel<ResultBag<EntityModel<T>>> {

		public ResultBagEntityModel(
			ResultBag<EntityModel<T>> content, Link... links) {

			super.of(content, links);
		}

	}

}