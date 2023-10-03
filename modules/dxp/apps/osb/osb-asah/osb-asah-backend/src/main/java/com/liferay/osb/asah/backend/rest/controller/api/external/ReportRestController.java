/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.api.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.dog.HistogramDog;
import com.liferay.osb.asah.backend.dog.MetricDog;
import com.liferay.osb.asah.backend.dog.MetricTypeDog;
import com.liferay.osb.asah.backend.dog.SegmentMetricDog;
import com.liferay.osb.asah.backend.dog.UserDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.dto.ActivityDTO;
import com.liferay.osb.asah.backend.dto.AudienceReportDTO;
import com.liferay.osb.asah.backend.dto.DataExportTaskDTO;
import com.liferay.osb.asah.backend.dto.ReportIndividualDTO;
import com.liferay.osb.asah.backend.dto.ReportSegmentDTO;
import com.liferay.osb.asah.backend.model.AssetMetric;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.BlogMetric;
import com.liferay.osb.asah.backend.model.BlogMetricType;
import com.liferay.osb.asah.backend.model.DocumentLibraryMetric;
import com.liferay.osb.asah.backend.model.DocumentLibraryMetricType;
import com.liferay.osb.asah.backend.model.FormFieldMetric;
import com.liferay.osb.asah.backend.model.FormMetric;
import com.liferay.osb.asah.backend.model.FormMetricType;
import com.liferay.osb.asah.backend.model.FormPageMetric;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.backend.model.JournalMetric;
import com.liferay.osb.asah.backend.model.JournalMetricType;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.model.PageMetric;
import com.liferay.osb.asah.backend.model.Trend;
import com.liferay.osb.asah.backend.rest.controller.BaseRestController;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.BQEventDog;
import com.liferay.osb.asah.common.dog.BQIdentityDog;
import com.liferay.osb.asah.common.dog.BQIdentityInterestScoreDog;
import com.liferay.osb.asah.common.dog.BQIndividualDog;
import com.liferay.osb.asah.common.dog.BQMembershipChangeDog;
import com.liferay.osb.asah.common.dog.DataExportTaskDog;
import com.liferay.osb.asah.common.dog.SegmentDog;
import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.entity.BQIdentityInterestScore;
import com.liferay.osb.asah.common.entity.BQMembershipChange;
import com.liferay.osb.asah.common.entity.DataExportTask;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcellus Tavares
 */
@RequestMapping(produces = "application/json", value = "/api/reports")
@RestController(
	"com.liferay.osb.asah.backend.rest.controller.api.external.ReportRestController"
)
public class ReportRestController extends BaseRestController {

	@GetMapping("/blogs/{blogId}")
	public EntityModel<AssetReport> getBlogAssetReportEntityModel(
		@RequestParam(defaultValue = "", name = "expand") Set<String> expands,
		@PathVariable String blogId,
		@RequestParam(defaultValue = "") String blogTitle,
		@RequestParam(defaultValue = "30") int rangeKey) {

		SearchQueryContext searchQueryContext = new SearchQueryContext() {
			{
				setAssetId(blogId);
				setAssetType(AssetType.BLOG);
				setIncludePrevious(Boolean.FALSE);
				setTimeRange(TimeRange.of(rangeKey));

				if (StringUtils.isNotEmpty(blogTitle)) {
					setTitle(blogTitle);
				}
			}
		};

		return _toBlogAssetReportEntityModel(
			_toAssetReport(
				_metricDog.getAssetMetric(
					searchQueryContext, _getBlogMetricTypeNames()),
				expands, searchQueryContext),
			rangeKey);
	}

	@GetMapping("/blogs")
	public ResultBagEntityModel<AssetReport>
		getBlogAssetReportResultBagEntityModel(
			@RequestParam(required = false) Long channelId,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "") String keywords,
			@RequestParam(defaultValue = "30") Integer rangeKey,
			@RequestParam(defaultValue = "viewsMetric") String sortMetric,
			@RequestParam(defaultValue = "desc") String sortOrder) {

		ResultBag<BlogMetric> blogMetricResultBag = new ResultBag<>();

		SearchQueryContext searchQueryContext = new SearchQueryContext() {
			{
				setAssetType(AssetType.BLOG);
				setChannelId(channelId);
				setKeywords(keywords);
				setTimeRange(TimeRange.of(rangeKey));
			}
		};

		blogMetricResultBag.setResults(
			_metricDog.getAssetMetrics(
				page, searchQueryContext, _getBlogMetricTypeNames(), _PAGE_SIZE,
				_createSort(AssetType.BLOG, sortMetric, sortOrder)));
		blogMetricResultBag.setTotal(
			_metricDog.getAssetMetricsCount(searchQueryContext));

		return _toResultBagEntityModel(
			_getBlogAssetReportResultBagEntityModel(
				channelId, page + 1, keywords, rangeKey, sortMetric, sortOrder),
			page,
			_getBlogAssetReportResultBagEntityModel(
				channelId, page - 1, keywords, rangeKey, sortMetric, sortOrder),
			blogMetricResultBag,
			blogMetric -> _toBlogAssetReportEntityModel(
				new AssetReport(blogMetric), rangeKey));
	}

	@GetMapping("/export/{type}")
	public ResponseEntity<DataExportTaskDTO> getDataExportTask(
		@RequestParam(value = "fromDate") String fromDate,
		@RequestParam(value = "toDate") String toDate,
		@PathVariable String type) {

		_validateDateRange(fromDate, toDate);

		Date fromUTCDate = DateUtil.toUTCDate(fromDate);
		Date toUTCDate = DateUtil.toUTCDate(toDate);

		DataExportTask dataExportTask =
			_dataExportTaskDog.fetchLastDataExportTaskByRange(
				fromUTCDate, toUTCDate,
				DataExportTask.Type.valueOf(StringUtils.upperCase(type)));

		if (dataExportTask == null) {
			return _addDataExportTask(fromUTCDate, null, toUTCDate, type);
		}

		DataExportTask.Status status = dataExportTask.getStatus();

		if (status == DataExportTask.Status.COMPLETED) {
			Date dayAfterCompletedDate = DateUtil.addDays(
				dataExportTask.getCompletedDate(), 1);

			if (dayAfterCompletedDate.before(new Date())) {
				return _addDataExportTask(fromUTCDate, null, toUTCDate, type);
			}
		}

		if (status == DataExportTask.Status.ERROR) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					String.format(
						"Data export task with ID %s has failed. Submitting " +
							"a new export task.",
						dataExportTask.getId()));
			}

			return _addDataExportTask(fromUTCDate, status, toUTCDate, type);
		}

		DataExportTaskDTO dataExportTaskDTO = new DataExportTaskDTO(
			dataExportTask);

		if (status == DataExportTask.Status.PENDING) {
			dataExportTaskDTO.setPreviousStatus(
				DataExportTask.Status.PENDING.name());
		}

		return _buildAcceptedResponseEntity(dataExportTaskDTO);
	}

	@GetMapping("/export/{type}/file")
	public ResponseEntity<FileSystemResource> getDataExportTaskFile(
		@RequestParam("fromDate") String fromDate,
		@RequestParam("toDate") String toDate, @PathVariable String type) {

		_validateDateRange(fromDate, toDate);

		DataExportTask dataExportTask =
			_dataExportTaskDog.fetchLastDataExportTaskByRange(
				DateUtil.toUTCDate(fromDate), DateUtil.toUTCDate(toDate),
				DataExportTask.Type.valueOf(StringUtils.upperCase(type)));

		if (dataExportTask == null) {
			return _buildBadRequestResponseEntity();
		}

		DataExportTask.Status status = dataExportTask.getStatus();

		if (status != DataExportTask.Status.COMPLETED) {
			return _buildBadRequestResponseEntity();
		}

		ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

		bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);

		String fileName = String.format(
			"%s-data-%s.zip", StringUtils.lowerCase(type),
			DateUtil.toUTCString(dataExportTask.getCompletedDate()));

		bodyBuilder.header(
			HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + fileName + "\"");

		File file = _dataExportTaskDog.getDataExportTaskFile(
			dataExportTask.getId());

		return bodyBuilder.body(new FileSystemResource(file.getAbsolutePath()));
	}

	@GetMapping("/documents-and-media/{documentId}")
	public EntityModel<AssetReport> getDocumentLibraryAssetReportEntityModel(
		@RequestParam(defaultValue = "", name = "expand") Set<String> expands,
		@PathVariable String documentId,
		@RequestParam(defaultValue = "") String documentTitle,
		@RequestParam(defaultValue = "30") int rangeKey) {

		SearchQueryContext searchQueryContext = new SearchQueryContext() {
			{
				setAssetId(documentId);
				setAssetType(AssetType.DOCUMENT);
				setTimeRange(TimeRange.of(rangeKey));

				if (StringUtils.isNotEmpty(documentTitle)) {
					setTitle(documentTitle);
				}
			}
		};

		return _toDocumentLibraryAssetReportEntityModel(
			_toAssetReport(
				_metricDog.getAssetMetric(
					searchQueryContext, _getDocumentLibraryMetricTypeNames()),
				expands, searchQueryContext),
			rangeKey);
	}

	@GetMapping("/documents-and-media")
	public ResultBagEntityModel<AssetReport>
		getDocumentLibraryAssetReportResultBagEntityModel(
			@RequestParam(required = false) Long channelId,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "") String keywords,
			@RequestParam(defaultValue = "30") Integer rangeKey,
			@RequestParam(defaultValue = "downloadsMetric") String sortMetric,
			@RequestParam(defaultValue = "desc") String sortOrder) {

		ResultBag<DocumentLibraryMetric> documentLibraryMetricResultBag =
			new ResultBag<>();

		SearchQueryContext searchQueryContext = new SearchQueryContext() {
			{
				setAssetType(AssetType.DOCUMENT);
				setChannelId(channelId);
				setKeywords(keywords);
				setTimeRange(TimeRange.of(rangeKey));
			}
		};

		documentLibraryMetricResultBag.setResults(
			_metricDog.getAssetMetrics(
				page, searchQueryContext, _getDocumentLibraryMetricTypeNames(),
				_PAGE_SIZE,
				_createSort(AssetType.DOCUMENT, sortMetric, sortOrder)));
		documentLibraryMetricResultBag.setTotal(
			_metricDog.getAssetMetricsCount(searchQueryContext));

		return _toResultBagEntityModel(
			_getDocumentLibraryAssetReportResultBagEntityModel(
				channelId, page + 1, keywords, rangeKey, sortMetric, sortOrder),
			page,
			_getDocumentLibraryAssetReportResultBagEntityModel(
				channelId, page - 1, keywords, rangeKey, sortMetric, sortOrder),
			documentLibraryMetricResultBag,
			documentLibraryMetric -> _toDocumentLibraryAssetReportEntityModel(
				new AssetReport(documentLibraryMetric), rangeKey));
	}

	@GetMapping("/*")
	public ResponseEntity<Map<String, Object>> getFallback() {
		return ResponseEntity.ok(
			new HashMap<String, Object>() {
				{
					put(
						"message",
						"The \"type\" query parameter must be either blogs, " +
							"documents-and-media, forms, individuals, pages, " +
								"segments, or web-contents.");
					put("status", "ERROR");
				}
			});
	}

	@GetMapping("/forms/{formId}")
	public EntityModel<AssetReport> getFormAssetReportEntityModel(
		@RequestParam(defaultValue = "", name = "expand") Set<String> expands,
		@PathVariable String formId,
		@RequestParam(defaultValue = "") String formTitle,
		@RequestParam(defaultValue = "30") int rangeKey) {

		SearchQueryContext searchQueryContext = new SearchQueryContext() {
			{
				setAssetId(formId);
				setAssetType(AssetType.FORM);
				setTimeRange(TimeRange.of(rangeKey));

				if (StringUtils.isNotEmpty(formTitle)) {
					setTitle(formTitle);
				}
			}
		};

		return _toFormAssetReportEntityModel(
			_toAssetReport(
				_metricDog.getAssetMetric(
					searchQueryContext, _getFormMetricTypeNames()),
				expands, searchQueryContext),
			rangeKey);
	}

	@GetMapping("/forms")
	public ResultBagEntityModel<AssetReport>
		getFormAssetReportResultBagEntityModel(
			@RequestParam(required = false) Long channelId,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "") String keywords,
			@RequestParam(defaultValue = "30") Integer rangeKey,
			@RequestParam(defaultValue = "submissionsMetric") String sortMetric,
			@RequestParam(defaultValue = "desc") String sortOrder) {

		ResultBag<FormMetric> formMetricResultBag = new ResultBag<>();

		SearchQueryContext searchQueryContext = new SearchQueryContext() {
			{
				setAssetType(AssetType.FORM);
				setChannelId(channelId);
				setKeywords(keywords);
				setTimeRange(TimeRange.of(rangeKey));
			}
		};

		formMetricResultBag.setResults(
			_metricDog.getAssetMetrics(
				page, searchQueryContext, _getFormMetricTypeNames(), _PAGE_SIZE,
				_createSort(AssetType.FORM, sortMetric, sortOrder)));
		formMetricResultBag.setTotal(
			_metricDog.getAssetMetricsCount(searchQueryContext));

		return _toResultBagEntityModel(
			_getFormAssetReportResultBagEntityModel(
				channelId, page + 1, keywords, rangeKey, sortMetric, sortOrder),
			page,
			_getFormAssetReportResultBagEntityModel(
				channelId, page - 1, keywords, rangeKey, sortMetric, sortOrder),
			formMetricResultBag,
			formMetric -> _toFormAssetReportEntityModel(
				new AssetReport(formMetric), rangeKey));
	}

	@GetMapping("/individuals/{individualId}/activities")
	public ResultBagEntityModel<ActivityDTO>
		getIndividualActivityResultBagEntityModel(
			@RequestParam(required = false) Long channelId,
			@PathVariable String individualId,
			@RequestParam(defaultValue = "0") Integer page) {

		Page<BQEvent> bqEventPage = _bqEventDog.getBQEventPage(
			channelId, null, page, _PAGE_SIZE, TimeRange.LAST_30_DAYS,
			_bqIdentityDog.getBQIdentityIds(individualId));

		Page<ActivityDTO> activityDTOs = bqEventPage.map(
			bqEvent -> {
				try {
					return new ActivityDTO(
						bqEvent, individualId,
						_objectMapper.readValue(
							bqEvent.getContext(),
							new TypeReference<Map<String, String>>() {
							}),
						_objectMapper.readValue(
							bqEvent.getEventProperties(),
							new TypeReference<Map<String, String>>() {
							}));
				}
				catch (JsonProcessingException jsonProcessingException) {
					throw new RuntimeException(jsonProcessingException);
				}
			});

		ResultBag<ActivityDTO> activityResultBag = new ResultBag<>(
			activityDTOs.getContent(), activityDTOs.getTotalElements());

		return _toResultBagEntityModel(
			_getIndividualActivityResultBagEntityModel(
				channelId, individualId, page + 1),
			page,
			_getIndividualActivityResultBagEntityModel(
				channelId, individualId, page - 1),
			activityResultBag,
			activityDTO -> _toChildEntityModel(individualId, activityDTO));
	}

	@GetMapping("/individuals/{individualId}/interests")
	public ResultBagEntityModel<BQIdentityInterestScore>
		getIndividualInterestResultBagEntityModel(
			@RequestParam(required = false) Long channelId,
			@PathVariable String individualId,
			@RequestParam(defaultValue = "0") Integer page) {

		Page<BQIdentityInterestScore> bqIdentityInterestScorePage =
			_bqIdentityInterestScoreDog.getBQIdentityInterestScorePage(
				channelId, individualId, _PAGE_SIZE, page * _PAGE_SIZE);

		return _toResultBagEntityModel(
			_getIndividualInterestResultBagEntityModel(
				channelId, individualId, page + 1),
			page,
			_getIndividualInterestResultBagEntityModel(
				channelId, individualId, page - 1),
			new ResultBag<>(
				bqIdentityInterestScorePage.getContent(),
				bqIdentityInterestScorePage.getTotalElements()),
			interest -> _toChildEntityModel(individualId, interest));
	}

	@GetMapping("/individuals/{individualId}/segments")
	public ResultBagEntityModel<Segment>
		getIndividualSegmentResultBagEntityModel(
			@PathVariable String individualId) {

		List<Segment> segments = _segmentDog.getBQIndividualSegments(
			individualId);

		return _toResultBagEntityModel(
			null, 0, null, new ResultBag<>(segments, segments.size()),
			segment -> _toChildEntityModel(individualId, segment));
	}

	@GetMapping("/web-contents/{webContentId}")
	public EntityModel<AssetReport> getJournalAssetReportEntityModel(
		@RequestParam(defaultValue = "", name = "expand") Set<String> expands,
		@PathVariable String webContentId,
		@RequestParam(defaultValue = "") String webContentTitle,
		@RequestParam(defaultValue = "30") int rangeKey) {

		SearchQueryContext searchQueryContext = new SearchQueryContext() {
			{
				setAssetId(webContentId);
				setAssetType(AssetType.JOURNAL);
				setTimeRange(TimeRange.of(rangeKey));

				if (StringUtils.isNotEmpty(webContentTitle)) {
					setTitle(webContentTitle);
				}
			}
		};

		return _toFormAssetReportEntityModel(
			_toAssetReport(
				_metricDog.getAssetMetric(
					searchQueryContext, _getJournalMetricTypeNames()),
				expands, searchQueryContext),
			rangeKey);
	}

	@GetMapping("/web-contents")
	public ResultBagEntityModel<AssetReport>
		getJournalAssetReportResultBagEntityModel(
			@RequestParam(required = false) Long channelId,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "") String keywords,
			@RequestParam(defaultValue = "30") Integer rangeKey,
			@RequestParam(defaultValue = "viewsMetric") String sortMetric,
			@RequestParam(defaultValue = "desc") String sortOrder) {

		ResultBag<JournalMetric> journalMetricResultBag = new ResultBag<>();

		SearchQueryContext searchQueryContext = new SearchQueryContext() {
			{
				setAssetType(AssetType.JOURNAL);
				setChannelId(channelId);
				setKeywords(keywords);
				setTimeRange(TimeRange.of(rangeKey));
			}
		};

		journalMetricResultBag.setResults(
			_metricDog.getAssetMetrics(
				page, searchQueryContext, _getJournalMetricTypeNames(),
				_PAGE_SIZE,
				_createSort(AssetType.JOURNAL, sortMetric, sortOrder)));
		journalMetricResultBag.setTotal(
			_metricDog.getAssetMetricsCount(searchQueryContext));

		return _toResultBagEntityModel(
			_getJournalAssetReportResultBagEntityModel(
				channelId, page + 1, keywords, rangeKey, sortMetric, sortOrder),
			page,
			_getJournalAssetReportResultBagEntityModel(
				channelId, page - 1, keywords, rangeKey, sortMetric, sortOrder),
			journalMetricResultBag,
			journalMetric -> _toJournalAssetReportEntityModel(
				new AssetReport(journalMetric), rangeKey));
	}

	@GetMapping
	public RepresentationModel getLinksRepresentationModel() {
		return new RepresentationModel() {
			{
				add(
					Arrays.asList(
						WebMvcLinkBuilder.linkTo(
							_getBlogAssetReportResultBagEntityModel(
								null, null, null, null, null, null)
						).withRel(
							"blogs"
						),
						WebMvcLinkBuilder.linkTo(
							_getDocumentLibraryAssetReportResultBagEntityModel(
								null, null, null, null, null, null)
						).withRel(
							"documents-and-media"
						),
						WebMvcLinkBuilder.linkTo(
							_getFormAssetReportResultBagEntityModel(
								null, null, null, null, null, null)
						).withRel(
							"forms"
						),
						WebMvcLinkBuilder.linkTo(
							_getReportIndividualDTOResultBagEntityModel(
								null, null, null)
						).withRel(
							"individuals"
						),
						WebMvcLinkBuilder.linkTo(
							_getPageAssetReportResultBagEntityModel(
								null, null, null, null, null, null)
						).withRel(
							"pages"
						),
						WebMvcLinkBuilder.linkTo(
							_getSegmentResultBagEntityModel(null, null)
						).withRel(
							"segments"
						),
						WebMvcLinkBuilder.linkTo(
							_getJournalAssetReportResultBagEntityModel(
								null, null, null, null, null, null)
						).withRel(
							"web-contents"
						)));
			}
		};
	}

	@GetMapping("/pages/{pageURL}")
	public EntityModel<PageAssetReport> getPageAssetReportEntityModel(
		@RequestParam(defaultValue = "", name = "expand") Set<String> expands,
		@RequestParam(defaultValue = "") String pageTitle,
		@PathVariable String pageURL,
		@RequestParam(defaultValue = "30") int rangeKey) {

		SearchQueryContext searchQueryContext = new SearchQueryContext() {
			{
				setAssetId(_decodeURL(pageURL));
				setAssetType(AssetType.PAGE);
				setTimeRange(TimeRange.of(rangeKey));

				if (StringUtils.isNotEmpty(pageTitle)) {
					setTitle(pageTitle);
				}
			}
		};

		return _toPageAssetReportEntityModel(
			new PageAssetReport(
				_toAssetReport(
					_metricDog.getAssetMetric(
						searchQueryContext, _getPageMetricTypeNames()),
					expands, searchQueryContext)),
			rangeKey);
	}

	@GetMapping("/pages")
	public ResultBagEntityModel<PageAssetReport>
		getPageAssetReportResultBagEntityModel(
			@RequestParam(required = false) Long channelId,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "") String keywords,
			@RequestParam(defaultValue = "30") Integer rangeKey,
			@RequestParam(defaultValue = "viewsMetric") String sortMetric,
			@RequestParam(defaultValue = "desc") String sortOrder) {

		ResultBag<PageMetric> pageMetricResultBag = new ResultBag<>();

		SearchQueryContext searchQueryContext = new SearchQueryContext() {
			{
				setAssetType(AssetType.PAGE);
				setChannelId(channelId);
				setKeywords(keywords);
				setTimeRange(TimeRange.of(rangeKey));
			}
		};

		pageMetricResultBag.setResults(
			_metricDog.getAssetMetrics(
				page, searchQueryContext, _getPageMetricTypeNames(), _PAGE_SIZE,
				_createSort(AssetType.PAGE, sortMetric, sortOrder)));
		pageMetricResultBag.setTotal(
			_metricDog.getAssetMetricsCount(searchQueryContext));

		return _toResultBagEntityModel(
			_getPageAssetReportResultBagEntityModel(
				channelId, page + 1, keywords, rangeKey, sortMetric, sortOrder),
			page,
			_getPageAssetReportResultBagEntityModel(
				channelId, page - 1, keywords, rangeKey, sortMetric, sortOrder),
			pageMetricResultBag,
			pageMetric -> _toPageAssetReportEntityModel(
				new PageAssetReport(new AssetReport(pageMetric)), rangeKey));
	}

	@GetMapping("/individuals/{individualId}")
	public EntityModel<ReportIndividualDTO> getReportIndividualDTOEntityModel(
		@PathVariable String individualId) {

		Individual individual = _bqIndividualDog.fetchBQIndividual(
			individualId);

		return _toReportIndividualDTOEntityModel(
			new ReportIndividualDTO(individual));
	}

	@GetMapping("/individuals")
	public ResultBagEntityModel<ReportIndividualDTO>
		getReportIndividualDTOResultBagEntityModel(
			@RequestParam(required = false) Long channelId,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "") String query) {

		Page<Individual> individualPage =
			_bqIndividualDog.searchBQIndividualPage(
				channelId, page, query, _PAGE_SIZE);

		ResultBag<ReportIndividualDTO> reportIndividualDTOResultBag =
			new ResultBag<>(
				ListUtil.map(
					individualPage.getContent(), ReportIndividualDTO::new),
				individualPage.getTotalElements());

		return _toResultBagEntityModel(
			_getReportIndividualDTOResultBagEntityModel(
				channelId, page + 1, query),
			page,
			_getReportIndividualDTOResultBagEntityModel(
				channelId, page - 1, query),
			reportIndividualDTOResultBag,
			this::_toReportIndividualDTOEntityModel);
	}

	@GetMapping("/segments/{segmentId}")
	public EntityModel<ReportSegmentDTO> getReportSegmentDTOEntityModel(
		@PathVariable Long segmentId) {

		return _toReportSegmentDTOEntityModel(
			_bqMembershipChangeDog.getLastBQMembershipChangeBySegmentId(
				segmentId),
			_segmentDog.getSegment(segmentId));
	}

	@GetMapping("/segments")
	public ResultBagEntityModel<ReportSegmentDTO>
		getReportSegmentDTOResultBagEntityModel(
			@RequestParam(required = false) Long channelId,
			@RequestParam(defaultValue = "0") Integer page) {

		Page<Segment> segmentPage = _segmentDog.getSegmentPage(
			channelId, page, _PAGE_SIZE);

		List<Segment> segments = segmentPage.getContent();

		Map<Long, BQMembershipChange> bqMembershipChanges =
			_bqMembershipChangeDog.getLastBQMembershipChanges(segments);

		return _toResultBagEntityModel(
			_getSegmentResultBagEntityModel(channelId, page + 1), page,
			_getSegmentResultBagEntityModel(channelId, page - 1), segments,
			segmentPage.getTotalElements(),
			segment -> _toReportSegmentDTOEntityModel(
				bqMembershipChanges.getOrDefault(segment.getId(), null),
				segment));
	}

	@GetMapping("/segments/{segmentId}/individuals")
	public ResultBagEntityModel<ReportIndividualDTO>
		getSegmentReportIndividualDTOResultBagEntityModel(
			@RequestParam(required = false) Long channelId,
			@PathVariable Long segmentId,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "") String query) {

		Page<Individual> individualPage =
			_bqIndividualDog.searchBQIndividualPage(
				channelId, segmentId, page, query, _PAGE_SIZE);

		ResultBag<ReportIndividualDTO> reportIndividualDTOResultBag =
			new ResultBag<>(
				ListUtil.map(
					individualPage.getContent(), ReportIndividualDTO::new),
				individualPage.getTotalElements());

		return _toResultBagEntityModel(
			_getSegmentReportIndividualDTOResultBagEntityModel(
				channelId, segmentId, page + 1, query),
			page,
			_getSegmentReportIndividualDTOResultBagEntityModel(
				channelId, segmentId, page - 1, query),
			reportIndividualDTOResultBag,
			this::_toReportIndividualDTOEntityModel);
	}

	private ResponseEntity<DataExportTaskDTO> _addDataExportTask(
		Date fromDate, DataExportTask.Status previousStatus, Date toDate,
		String type) {

		DataExportTaskDTO dataExportTaskDTO = new DataExportTaskDTO(
			_dataExportTaskDog.addDataExportTask(
				fromDate, toDate,
				DataExportTask.Type.valueOf(StringUtils.upperCase(type))));

		dataExportTaskDTO.setPreviousStatus(
			StringUtil.get(previousStatus, null));

		return _buildAcceptedResponseEntity(dataExportTaskDTO);
	}

	private <T> ResponseEntity<T> _buildAcceptedResponseEntity(T body) {
		ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(
			HttpStatus.SC_ACCEPTED);

		return bodyBuilder.body(body);
	}

	private ResponseEntity<FileSystemResource>
		_buildBadRequestResponseEntity() {

		ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(
			HttpStatus.SC_BAD_REQUEST);

		return bodyBuilder.build();
	}

	private Sort _createSort(
		AssetType assetType, String metricTypeString, String sortOrderString) {

		MetricType metricType = _metricTypeDog.getMetricType(
			assetType, metricTypeString);

		return new Sort(metricType.getName(), sortOrderString);
	}

	private String _decodeURL(String url) {
		if (url == null) {
			return null;
		}

		try {
			Base64.Decoder urlDecoder = Base64.getUrlDecoder();

			return new String(
				urlDecoder.decode(url), StandardCharsets.UTF_8.toString());
		}
		catch (UnsupportedEncodingException unsupportedEncodingException) {
			throw new IllegalStateException(unsupportedEncodingException);
		}
	}

	private String _encodeURL(String url) {
		if (url == null) {
			return null;
		}

		try {
			Base64.Encoder urlEncoder = Base64.getUrlEncoder();

			return urlEncoder.encodeToString(
				url.getBytes(StandardCharsets.UTF_8.toString()));
		}
		catch (UnsupportedEncodingException unsupportedEncodingException) {
			throw new IllegalStateException(unsupportedEncodingException);
		}
	}

	private void _expandMetricReport(
		List<Metric> metrics, Consumer<MetricReport> metricReportConsumer) {

		Stream<Metric> stream = metrics.stream();

		stream.map(
			metric -> new MetricReport(
				metric.getValueKey(), metric.getPreviousValue(),
				metric.getValue())
		).forEach(
			metricReportConsumer
		);
	}

	private void _expandMetricReportAudience(
		MetricReport metricReport, MetricType metricType,
		SearchQueryContext searchQueryContext) {

		AudienceReportDTO audienceReportDTO = new AudienceReportDTO(
			_userDog.getAudienceReport(metricType, searchQueryContext));

		ResultBag<Metric> segmentMetricResultBag =
			_segmentMetricDog.getSegmentMetricResultBag(
				metricType, searchQueryContext);

		audienceReportDTO.setSegmentMetricDTOReportResultBag(
			new ResultBag<>(
				ListUtil.map(
					segmentMetricResultBag.getResults(),
					AudienceReportDTO.SegmentMetricDTO::new),
				segmentMetricResultBag.getTotal()));

		metricReport._audienceReportDTO = audienceReportDTO;
	}

	private ResultBagEntityModel<AssetReport>
		_getBlogAssetReportResultBagEntityModel(
			Long channelId, Integer page, String keywords, Integer rangeKey,
			String sortMetric, String sortOrder) {

		return WebMvcLinkBuilder.methodOn(
			ReportRestController.class
		).getBlogAssetReportResultBagEntityModel(
			channelId, page, keywords, rangeKey, sortMetric, sortOrder
		);
	}

	private Set<String> _getBlogMetricTypeNames() {
		return Stream.of(
			BlogMetricType.values()
		).map(
			BlogMetricType::getName
		).collect(
			Collectors.toSet()
		);
	}

	private ResultBagEntityModel<AssetReport>
		_getDocumentLibraryAssetReportResultBagEntityModel(
			Long channelId, Integer page, String keywords, Integer rangeKey,
			String sortMetric, String sortOrder) {

		return WebMvcLinkBuilder.methodOn(
			ReportRestController.class
		).getDocumentLibraryAssetReportResultBagEntityModel(
			channelId, page, keywords, rangeKey, sortMetric, sortOrder
		);
	}

	private Set<String> _getDocumentLibraryMetricTypeNames() {
		return Stream.of(
			DocumentLibraryMetricType.values()
		).map(
			DocumentLibraryMetricType::getName
		).collect(
			Collectors.toSet()
		);
	}

	private ResultBagEntityModel<AssetReport>
		_getFormAssetReportResultBagEntityModel(
			Long channelId, Integer page, String keywords, Integer rangeKey,
			String sortMetric, String sortOrder) {

		return WebMvcLinkBuilder.methodOn(
			ReportRestController.class
		).getFormAssetReportResultBagEntityModel(
			channelId, page, keywords, rangeKey, sortMetric, sortOrder
		);
	}

	private Set<String> _getFormMetricTypeNames() {
		return Stream.of(
			FormMetricType.values()
		).map(
			FormMetricType::getName
		).collect(
			Collectors.toSet()
		);
	}

	private ResultBagEntityModel<ActivityDTO>
		_getIndividualActivityResultBagEntityModel(
			Long channelId, String individualId, Integer page) {

		return WebMvcLinkBuilder.methodOn(
			ReportRestController.class
		).getIndividualActivityResultBagEntityModel(
			channelId, individualId, page
		);
	}

	private ResultBagEntityModel<BQIdentityInterestScore>
		_getIndividualInterestResultBagEntityModel(
			Long channelId, String individualId, Integer page) {

		return WebMvcLinkBuilder.methodOn(
			ReportRestController.class
		).getIndividualInterestResultBagEntityModel(
			channelId, individualId, page
		);
	}

	private ResultBagEntityModel<AssetReport>
		_getJournalAssetReportResultBagEntityModel(
			Long channelId, Integer page, String keywords, Integer rangeKey,
			String sortMetric, String sortOrder) {

		return WebMvcLinkBuilder.methodOn(
			ReportRestController.class
		).getJournalAssetReportResultBagEntityModel(
			channelId, page, keywords, rangeKey, sortMetric, sortOrder
		);
	}

	private Set<String> _getJournalMetricTypeNames() {
		return Stream.of(
			JournalMetricType.values()
		).map(
			JournalMetricType::getName
		).collect(
			Collectors.toSet()
		);
	}

	private ResultBagEntityModel<PageAssetReport>
		_getPageAssetReportResultBagEntityModel(
			Long channelId, Integer page, String keywords, Integer rangeKey,
			String sortMetric, String sortOrder) {

		return WebMvcLinkBuilder.methodOn(
			ReportRestController.class
		).getPageAssetReportResultBagEntityModel(
			channelId, page, keywords, rangeKey, sortMetric, sortOrder
		);
	}

	private Set<String> _getPageMetricTypeNames() {
		return Stream.of(
			PageMetricType.values()
		).map(
			PageMetricType::getName
		).collect(
			Collectors.toSet()
		);
	}

	private ResultBagEntityModel<ReportIndividualDTO>
		_getReportIndividualDTOResultBagEntityModel(
			Long channelId, Integer page, String query) {

		return WebMvcLinkBuilder.methodOn(
			ReportRestController.class
		).getReportIndividualDTOResultBagEntityModel(
			channelId, page, query
		);
	}

	private ResultBagEntityModel<ReportIndividualDTO>
		_getSegmentReportIndividualDTOResultBagEntityModel(
			Long channelId, Long segmentId, Integer page, String query) {

		return WebMvcLinkBuilder.methodOn(
			ReportRestController.class
		).getSegmentReportIndividualDTOResultBagEntityModel(
			channelId, segmentId, page, query
		);
	}

	private ResultBagEntityModel<ReportSegmentDTO>
		_getSegmentResultBagEntityModel(Long channelId, Integer page) {

		return WebMvcLinkBuilder.methodOn(
			ReportRestController.class
		).getReportSegmentDTOResultBagEntityModel(
			channelId, page
		);
	}

	private AssetReport _toAssetReport(
		AssetMetric assetMetric, Set<String> expands,
		SearchQueryContext searchQueryContext) {

		Map<String, MetricReport> metricReports = new HashMap<>();

		for (Metric metric : assetMetric.getAvailableMetrics()) {
			MetricReport metricReport = new MetricReport(metric);

			if (expands.contains("audience")) {
				_expandMetricReportAudience(
					metricReport, metric.getMetricType(), searchQueryContext);
			}

			if (expands.contains("browser")) {
				_expandMetricReport(
					_metricDog.getBrowserMetrics(
						metric.getMetricType(), searchQueryContext),
					metricReport::_addBrowserMetricReport);
			}

			if (expands.contains("device")) {
				_expandMetricReport(
					_metricDog.getDeviceMetrics(
						metric.getMetricType(), searchQueryContext),
					metricReport::_addDeviceMetricReport);
			}

			if (expands.contains("histogram")) {
				HistogramMetricBag histogramMetricBag =
					_histogramDog.getHistogramMetricBag(
						metric.getMetricType(), searchQueryContext);

				metricReport._histogramReport = new HistogramReport(
					histogramMetricBag.getMetrics());
			}

			if (expands.contains("location")) {
				_expandMetricReport(
					_metricDog.getGeolocationMetrics(
						metric.getMetricType(), searchQueryContext),
					metricReport::_addGeolocationMetricReport);
			}

			MetricType metricType = metric.getMetricType();

			metricReports.put(metricType.getName(), metricReport);
		}

		return new AssetReport(assetMetric, metricReports);
	}

	private EntityModel<AssetReport> _toBlogAssetReportEntityModel(
		AssetReport assetReport, int rangeKey) {

		return EntityModel.of(
			assetReport,
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					ReportRestController.class
				).getBlogAssetReportEntityModel(
					Collections.emptySet(), assetReport.getId(),
					assetReport.getTitle(), rangeKey
				)
			).withSelfRel());
	}

	private <T> EntityModel<T> _toChildEntityModel(String parentId, T t) {
		return EntityModel.of(
			t,
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					ReportRestController.class
				).getReportIndividualDTOEntityModel(
					parentId
				)
			).withRel(
				"parent"
			));
	}

	private EntityModel<AssetReport> _toDocumentLibraryAssetReportEntityModel(
		AssetReport assetReport, int rangeKey) {

		return EntityModel.of(
			assetReport,
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					ReportRestController.class
				).getDocumentLibraryAssetReportEntityModel(
					Collections.emptySet(), assetReport.getId(),
					assetReport.getTitle(), rangeKey
				)
			).withSelfRel());
	}

	private EntityModel<AssetReport> _toFormAssetReportEntityModel(
		AssetReport assetReport, int rangeKey) {

		return EntityModel.of(
			assetReport,
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					ReportRestController.class
				).getFormAssetReportEntityModel(
					Collections.emptySet(), assetReport.getId(),
					assetReport.getTitle(), rangeKey
				)
			).withSelfRel());
	}

	private EntityModel<AssetReport> _toJournalAssetReportEntityModel(
		AssetReport assetReport, int rangeKey) {

		return EntityModel.of(
			assetReport,
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					ReportRestController.class
				).getJournalAssetReportEntityModel(
					Collections.emptySet(), assetReport.getId(),
					assetReport.getTitle(), rangeKey
				)
			).withSelfRel());
	}

	private <T, R> List<EntityModel<R>> _toListEntityModel(
		List<T> results,
		Function<T, EntityModel<R>> resultEntityModelMapperFunction) {

		return ListUtil.map(results, resultEntityModelMapperFunction);
	}

	private EntityModel<PageAssetReport> _toPageAssetReportEntityModel(
		PageAssetReport pageAssetReport, int rangeKey) {

		return EntityModel.of(
			pageAssetReport,
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					ReportRestController.class
				).getPageAssetReportEntityModel(
					Collections.emptySet(), pageAssetReport.getTitle(),
					_encodeURL(pageAssetReport.getURL()), rangeKey
				)
			).withSelfRel());
	}

	private EntityModel<ReportIndividualDTO> _toReportIndividualDTOEntityModel(
		ReportIndividualDTO reportIndividualDTO) {

		return EntityModel.of(
			reportIndividualDTO,
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					ReportRestController.class
				).getReportIndividualDTOEntityModel(
					reportIndividualDTO.getId()
				)
			).withSelfRel(),
			WebMvcLinkBuilder.linkTo(
				_getIndividualActivityResultBagEntityModel(
					null, reportIndividualDTO.getId(), null)
			).withRel(
				"activities"
			),
			WebMvcLinkBuilder.linkTo(
				_getIndividualInterestResultBagEntityModel(
					null, reportIndividualDTO.getId(), null)
			).withRel(
				"interests"
			),
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					ReportRestController.class
				).getIndividualSegmentResultBagEntityModel(
					reportIndividualDTO.getId()
				)
			).withRel(
				"segments"
			));
	}

	private EntityModel<ReportSegmentDTO> _toReportSegmentDTOEntityModel(
		BQMembershipChange bqMembershipChange, Segment segment) {

		return EntityModel.of(
			new ReportSegmentDTO(bqMembershipChange, segment),
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					ReportRestController.class
				).getReportSegmentDTOEntityModel(
					segment.getId()
				)
			).withSelfRel(),
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(
					ReportRestController.class
				).getSegmentReportIndividualDTOResultBagEntityModel(
					null, segment.getId(), 0, null
				)
			).withRel(
				"individuals"
			));
	}

	private <T, R> ResultBagEntityModel<R> _toResultBagEntityModel(
		Object nextPageMethodInvocation, int page,
		Object prevPageMethodInvocation, List<T> results, long total,
		Function<T, EntityModel<R>> resultEntityModelMapperFunction) {

		ResultBagEntityModel<R> resultBagEntityModel =
			new ResultBagEntityModel<>(
				new ResultBag<>(
					_toListEntityModel(
						results, resultEntityModelMapperFunction),
					total));

		if (((page + 1L) * _PAGE_SIZE) < total) {
			resultBagEntityModel.add(
				WebMvcLinkBuilder.linkTo(
					nextPageMethodInvocation
				).withRel(
					"next"
				));
		}

		if (page > 0) {
			resultBagEntityModel.add(
				WebMvcLinkBuilder.linkTo(
					prevPageMethodInvocation
				).withRel(
					"prev"
				));
		}

		return resultBagEntityModel;
	}

	private <T, R> ResultBagEntityModel<R> _toResultBagEntityModel(
		Object nextPageMethodInvocation, int page,
		Object prevPageMethodInvocation, ResultBag<T> resultBag,
		Function<T, EntityModel<R>> resultEntityModelMapperFunction) {

		return _toResultBagEntityModel(
			nextPageMethodInvocation, page, prevPageMethodInvocation,
			resultBag.getResults(), resultBag.getTotal(),
			resultEntityModelMapperFunction);
	}

	private void _validateDateRange(String fromDate, String toDate) {
		if ((fromDate == null) || (toDate == null)) {
			throw new IllegalArgumentException("Date range is mandatory");
		}

		Date fromUTCDate = null;
		Date toUTCDate = null;

		try {
			fromUTCDate = DateUtil.toUTCDate(fromDate);
			toUTCDate = DateUtil.toUTCDate(toDate);
		}
		catch (Exception exception) {
			throw new IllegalArgumentException(
				"Unable to convert to UTC date", exception);
		}

		if (fromUTCDate.after(toUTCDate)) {
			throw new IllegalArgumentException("From date is after to date");
		}
	}

	private static final int _PAGE_SIZE = 20;

	private static final Log _log = LogFactory.getLog(
		ReportRestController.class);

	@Autowired
	private BQEventDog _bqEventDog;

	@Autowired
	private BQIdentityDog _bqIdentityDog;

	@Autowired
	private BQIdentityInterestScoreDog _bqIdentityInterestScoreDog;

	@Autowired
	private BQIndividualDog _bqIndividualDog;

	@Autowired
	private BQMembershipChangeDog _bqMembershipChangeDog;

	@Autowired
	private DataExportTaskDog _dataExportTaskDog;

	@Value("${osb.asah.data.export.task.expiration.minutes:30}")
	private int _dataExportTaskExpirationMinutes;

	@Autowired
	private HistogramDog _histogramDog;

	@Autowired
	private MetricDog _metricDog;

	@Autowired
	private MetricTypeDog _metricTypeDog;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private SegmentDog _segmentDog;

	@Autowired
	private SegmentMetricDog _segmentMetricDog;

	@Autowired
	private UserDog _userDog;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class AssetReport {

		public AssetReport(AssetMetric assetMetric) {
			for (Metric metric : assetMetric.getAvailableMetrics()) {
				MetricType metricType = metric.getMetricType();

				_metricReports.put(
					metricType.getName(), new MetricReport(metric));
			}

			_assetMetric = assetMetric;
		}

		public AssetReport(
			AssetMetric assetMetric, Map<String, MetricReport> metricReports) {

			_assetMetric = assetMetric;
			_metricReports = metricReports;
		}

		public String getId() {
			return _assetMetric.getAssetId();
		}

		@JsonProperty("metrics")
		public Map<String, MetricReport> getMetricReports() {
			return Collections.synchronizedMap(_metricReports);
		}

		public String getTitle() {
			return _assetMetric.getAssetTitle();
		}

		private final AssetMetric _assetMetric;
		private Map<String, MetricReport> _metricReports = new HashMap<>();

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class FormFieldReport {

		public FormFieldReport(FormFieldMetric formFieldMetric) {
			for (Metric metric : formFieldMetric.getAvailableMetrics()) {
				MetricType metricType = metric.getMetricType();

				_metricReports.put(
					metricType.getName(), new MetricReport(metric));
			}
		}

		@JsonProperty("metrics")
		public Map<String, MetricReport> getMetricReports() {
			return Collections.synchronizedMap(_metricReports);
		}

		private final Map<String, MetricReport> _metricReports =
			new HashMap<>();

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class FormPageReport {

		public FormPageReport(FormPageMetric formPageMetric) {
			for (Metric metric : formPageMetric.getAvailableMetrics()) {
				MetricType metricType = metric.getMetricType();

				_metricReports.put(
					metricType.getName(), new MetricReport(metric));
			}

			for (FormFieldMetric formFieldMetric :
					formPageMetric.getFormFieldMetrics()) {

				_formFieldReports.put(
					formFieldMetric.getFieldName(),
					new FormFieldReport(formFieldMetric));
			}

			_formPageMetric = formPageMetric;
		}

		@JsonProperty("fields")
		public Map<String, FormFieldReport> getFormFieldReports() {
			return _formFieldReports;
		}

		public String getId() {
			return _formPageMetric.getPageIndex();
		}

		@JsonProperty("metrics")
		public Map<String, MetricReport> getMetricReports() {
			return Collections.synchronizedMap(_metricReports);
		}

		public String getTitle() {
			return _formPageMetric.getPageName();
		}

		private final Map<String, FormFieldReport> _formFieldReports =
			new HashMap<>();
		private final FormPageMetric _formPageMetric;
		private final Map<String, MetricReport> _metricReports =
			new HashMap<>();

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class FormPagesReport {

		public FormPagesReport(
			String formId, List<FormPageMetric> formPageMetrics,
			String formTitle) {

			_formId = formId;
			_formTitle = formTitle;

			for (FormPageMetric formPageMetric : formPageMetrics) {
				_formPageReports.add(new FormPageReport(formPageMetric));
			}
		}

		public String getFormId() {
			return _formId;
		}

		@JsonProperty("formPages")
		public List<FormPageReport> getFormPageReports() {
			return _formPageReports;
		}

		public String getFormTitle() {
			return _formTitle;
		}

		private final String _formId;
		private final List<FormPageReport> _formPageReports = new ArrayList<>();
		private final String _formTitle;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class HistogramBucketReport {

		public HistogramBucketReport(HistogramMetric histogramMetric) {
			_histogramMetric = histogramMetric;
		}

		public String getKey() {
			return _histogramMetric.getKey();
		}

		public Double getValue() {
			return _histogramMetric.getValue();
		}

		private final HistogramMetric _histogramMetric;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class HistogramReport {

		public HistogramReport(List<HistogramMetric> histogramMetrics) {
			for (HistogramMetric histogramMetric : histogramMetrics) {
				_histogramBucketReports.add(
					new HistogramBucketReport(histogramMetric));
			}
		}

		@JsonProperty("buckets")
		public List<HistogramBucketReport> getHistogramBucketReports() {
			return _histogramBucketReports;
		}

		private final List<HistogramBucketReport> _histogramBucketReports =
			new ArrayList<>();

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class MetricReport {

		public MetricReport(Metric metric) {
			_previousValue = metric.getPreviousValue();
			_trend = metric.getTrend();
			_value = metric.getValue();
			_valueKey = metric.getValueKey();
		}

		public MetricReport(String name, Double previousValue, Double value) {
			_name = name;
			_previousValue = previousValue;
			_value = value;
		}

		@JsonProperty("audience")
		public AudienceReportDTO getAudienceReport() {
			return _audienceReportDTO;
		}

		@JsonProperty("browsers")
		public List<MetricReport> getBrowserMetricReports() {
			return _browserMetricReports;
		}

		@JsonProperty("devices")
		public List<MetricReport> getDeviceMetricReports() {
			return _deviceMetricReports;
		}

		@JsonProperty("locations")
		public List<MetricReport> getGeolocationMetricReports() {
			return _geolocationMetricReports;
		}

		@JsonProperty("histogram")
		public HistogramReport getHistogramReport() {
			return _histogramReport;
		}

		public String getName() {
			return _name;
		}

		public Double getPreviousValue() {
			return _previousValue;
		}

		public Trend getTrend() {
			if ((_trend == null) || (_trend.getPercentage() == null)) {
				return null;
			}

			return _trend;
		}

		public Double getValue() {
			return _value;
		}

		public String getValueKey() {
			return _valueKey;
		}

		private void _addBrowserMetricReport(MetricReport metricReport) {
			if (_browserMetricReports == null) {
				_browserMetricReports = new ArrayList<>();
			}

			_browserMetricReports.add(metricReport);
		}

		private void _addDeviceMetricReport(MetricReport metricReport) {
			if (_deviceMetricReports == null) {
				_deviceMetricReports = new ArrayList<>();
			}

			_deviceMetricReports.add(metricReport);
		}

		private void _addGeolocationMetricReport(MetricReport metricReport) {
			if (_geolocationMetricReports == null) {
				_geolocationMetricReports = new ArrayList<>();
			}

			_geolocationMetricReports.add(metricReport);
		}

		private AudienceReportDTO _audienceReportDTO;
		private List<MetricReport> _browserMetricReports;
		private List<MetricReport> _deviceMetricReports;
		private List<MetricReport> _geolocationMetricReports;
		private HistogramReport _histogramReport;
		private String _name;
		private final Double _previousValue;
		private Trend _trend;
		private final Double _value;
		private String _valueKey;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class PageAssetReport {

		public PageAssetReport(AssetReport assetReport) {
			_assetReport = assetReport;
		}

		@JsonProperty("metrics")
		public Map<String, MetricReport> getMetricReports() {
			return _assetReport.getMetricReports();
		}

		public String getTitle() {
			return _assetReport.getTitle();
		}

		@JsonProperty("url")
		public String getURL() {
			return _assetReport.getId();
		}

		private final AssetReport _assetReport;

	}

	private static class ResultBagEntityModel<T>
		extends EntityModel<ResultBag<EntityModel<T>>> {

		public ResultBagEntityModel(ResultBag<EntityModel<T>> content) {
			super(content);
		}

	}

}