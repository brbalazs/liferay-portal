/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.ext.seo.rest.controller.api.data.source.v1;

import com.liferay.osb.asah.backend.ext.seo.model.CountrySearchKeywords;
import com.liferay.osb.asah.backend.ext.seo.model.SearchKeyword;
import com.liferay.osb.asah.backend.ext.seo.model.TrafficSource;
import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.spring.annotation.CacheEvict;
import com.liferay.osb.asah.common.spring.annotation.Cacheable;
import com.liferay.osb.asah.common.spring.http.Http;
import com.liferay.osb.asah.common.util.StringUtil;
import com.liferay.osb.asah.common.util.URLUtil;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.ByteArrayInputStream;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.charset.StandardCharsets;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author David Arques
 */
@EnableScheduling
@RequestMapping("/api/seo/1.0")
@RestController(
	"com.liferay.osb.asah.backend.ext.seo.rest.controller.api.data.source.v1.RootRestController"
)
public class RootRestController {

	@CacheEvict(allProjects = true, value = "getTrafficSources")
	@DeleteMapping("/cache")
	@Scheduled(cron = "0 0 2 ? * MON")
	public void clearCache() {
	}

	@Cacheable
	@GetMapping("/traffic-sources")
	public List<TrafficSource> getTrafficSources(@RequestParam String url) {
		if (StringUtils.isEmpty(url)) {
			throw new IllegalArgumentException("URL is null");
		}

		Set<String> databases = _getDatabases(url);

		List<CountrySearchKeywords> organicCountrySearchKeywordsList =
			_getCountrySearchKeywordsList(
				databases, _urlOrganicDisplayLimit, "url_organic", url);

		long organicSearchKeywordsTotalTraffic =
			_getOrganicSearchKeywordsTotalTraffic(databases, url);

		List<CountrySearchKeywords> paidCountrySearchKeywordsList =
			_getCountrySearchKeywordsList(
				databases, _urlAdwordsDisplayLimit, "url_adwords", url);

		long paidSearchKeywordsTotalTraffic = _getSearchKeywordsTotalTraffic(
			paidCountrySearchKeywordsList);

		return Arrays.asList(
			new TrafficSource(
				organicCountrySearchKeywordsList, "organic",
				organicSearchKeywordsTotalTraffic,
				_calculatePercentage(
					organicSearchKeywordsTotalTraffic,
					organicSearchKeywordsTotalTraffic +
						paidSearchKeywordsTotalTraffic)),
			new TrafficSource(
				paidCountrySearchKeywordsList, "paid",
				paidSearchKeywordsTotalTraffic,
				_calculatePercentage(
					paidSearchKeywordsTotalTraffic,
					organicSearchKeywordsTotalTraffic +
						paidSearchKeywordsTotalTraffic)));
	}

	private double _calculatePercentage(long value, long total) {
		double percentage = 0;

		if (total != 0) {
			percentage = (double)value * 100 / total;
		}

		BigDecimal bigDecimal = BigDecimal.valueOf(percentage);

		bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);

		return bigDecimal.doubleValue();
	}

	private List<CountrySearchKeywords> _getCountrySearchKeywordsList(
		Set<String> databases, int displayLimit, String type, String url) {

		Stream<String> stream = databases.stream();

		return stream.map(
			database -> new CountrySearchKeywords(
				database, _getSearchKeywords(database, displayLimit, type, url))
		).collect(
			Collectors.toList()
		);
	}

	private Set<String> _getDatabases(String url) {
		Set<String> databases = new LinkedHashSet<>();

		JSONArray jsonArray = new JSONArray(
			_http.exchange(
				ServiceConstants.URL_BACKEND_INTERNAL,
				"/api/1.0/pages/geolocations?canonicalURL=" + url,
				HttpMethod.GET, null));

		for (int i = 0; (i < jsonArray.length()) && (i < _databasesLimit);
			 i++) {

			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String database = _databases.get(jsonObject.getString("valueKey"));

			if (database != null) {
				databases.add(database);
			}
		}

		return databases;
	}

	private String _getDomain(String url) {
		try {
			URI uri = URLUtil.toURI(url);

			String host = uri.getHost();

			if (StringUtil.isNull(host)) {
				throw new IllegalArgumentException("Invalid URL " + url);
			}

			if (host.startsWith("www.")) {
				return host.substring(4);
			}

			return host;
		}
		catch (URISyntaxException uriSyntaxException) {
			throw new IllegalArgumentException(
				"Invalid URL " + url, uriSyntaxException);
		}
	}

	private long _getOrganicSearchKeywordsTotalTraffic(
		Set<String> databases, String url) {

		Stream<String> stream = databases.stream();

		String domain = _getDomain(url);

		return stream.mapToLong(
			database -> _getOrganicSearchKeywordsTotalTraffic(
				database, domain, url)
		).sum();
	}

	private long _getOrganicSearchKeywordsTotalTraffic(
		String database, String domain, String url) {

		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(
			"https://api.semrush.com/"
		).encode(
		).queryParam(
			"database", database
		).queryParam(
			"display_filter", "+|Ur|Eq|" + url
		).queryParam(
			"display_limit", 1
		).queryParam(
			"domain", domain
		).queryParam(
			"export_columns", "Tg"
		).queryParam(
			"key", _environment.getProperty("SEMRUSH_API_KEY", _semrushAPIKey)
		).queryParam(
			"type", "domain_organic_unique"
		).build();

		ResponseEntity<String> responseEntity = _http.exchangeResponseEntity(
			uriComponents.toUriString(), null, HttpMethod.GET, null);

		if (!Objects.equals(responseEntity.getStatusCode(), HttpStatus.OK)) {
			throw new HttpClientErrorException(responseEntity.getStatusCode());
		}

		return Optional.ofNullable(
			responseEntity.getBody()
		).map(
			this::_readFirstCell
		).map(
			Long::parseLong
		).orElse(
			0L
		);
	}

	private List<SearchKeyword> _getSearchKeywords(
		String database, int displayLimit, String type, String url) {

		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(
			"https://api.semrush.com/"
		).encode(
		).queryParam(
			"database", database
		).queryParam(
			"display_filter", "+|Tg|Gt|0"
		).queryParam(
			"display_limit", displayLimit
		).queryParam(
			"display_sort", "tg_desc"
		).queryParam(
			"export_columns", "Ph,Po,Nq,Tg"
		).queryParam(
			"key", _environment.getProperty("SEMRUSH_API_KEY", _semrushAPIKey)
		).queryParam(
			"type", type
		).queryParam(
			"url", url
		).build();

		ResponseEntity<String> responseEntity = _http.exchangeResponseEntity(
			uriComponents.toUriString(), null, HttpMethod.GET, null);

		if (!Objects.equals(responseEntity.getStatusCode(), HttpStatus.OK)) {
			throw new HttpClientErrorException(responseEntity.getStatusCode());
		}

		return _toSearchKeywords(responseEntity.getBody());
	}

	private long _getSearchKeywordsTotalTraffic(
		List<CountrySearchKeywords> countrySearchKeywordsList) {

		Stream<CountrySearchKeywords> stream =
			countrySearchKeywordsList.stream();

		return stream.map(
			CountrySearchKeywords::getSearchKeywords
		).flatMap(
			Collection::stream
		).mapToLong(
			SearchKeyword::getTraffic
		).sum();
	}

	private String _readFirstCell(String body) {
		CsvParserSettings csvParserSettings = new CsvParserSettings();

		csvParserSettings.setHeaderExtractionEnabled(true);

		CsvParser csvParser = new CsvParser(csvParserSettings);

		List<String[]> rows = csvParser.parseAll(
			new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));

		Stream<String[]> stream = rows.stream();

		return stream.findFirst(
		).map(
			row -> row[0]
		).orElse(
			"0"
		);
	}

	private List<SearchKeyword> _toSearchKeywords(String body) {
		CsvParserSettings csvParserSettings = new CsvParserSettings();

		CsvFormat csvFormat = csvParserSettings.getFormat();

		csvFormat.setDelimiter(';');

		csvParserSettings.setHeaderExtractionEnabled(true);

		BeanListProcessor<SearchKeyword> beanListProcessor =
			new BeanListProcessor<>(SearchKeyword.class);

		csvParserSettings.setProcessor(beanListProcessor);

		if (body != null) {
			CsvParser csvParser = new CsvParser(csvParserSettings);

			csvParser.parse(
				new ByteArrayInputStream(
					body.getBytes(StandardCharsets.UTF_8)));
		}

		return beanListProcessor.getBeans();
	}

	private static final Map<String, String> _databases =
		new HashMap<String, String>() {
			{
				put("Afghanistan", "af");
				put("Albania", "al");
				put("Algeria", "dz");
				put("Angola", "ao");
				put("Argentina", "ar");
				put("Armenia", "am");
				put("Australia", "au");
				put("Austria", "at");
				put("Azerbaijan", "az");
				put("Bahamas", "bs");
				put("Bahrain", "bh");
				put("Bangladesh", "bd");
				put("Belarus", "by");
				put("Belgium", "be");
				put("Belize", "bz");
				put("Bolivia", "bo");
				put("Bosnia and Herzegovina", "ba");
				put("Botswana", "bw");
				put("Brazil", "br");
				put("Brunei", "bn");
				put("Bulgaria", "bg");
				put("Cabo Verde", "cv");
				put("Cambodia", "kh");
				put("Cameroon", "cm");
				put("Canada", "ca");
				put("Chile", "cl");
				put("Colombia", "co");
				put("Congo", "cg");
				put("Costa Rica", "cr");
				put("Croatia", "hr");
				put("Cyprus", "cy");
				put("Czech Republic", "cz");
				put("Denmark", "dk");
				put("Dominican Republic", "do");
				put("Ecuador", "ec");
				put("Egypt", "eg");
				put("El Salvador", "sv");
				put("Estonia", "ee");
				put("Ethiopia", "et");
				put("Finland", "fi");
				put("France", "fr");
				put("Georgia", "ge");
				put("Germany", "de");
				put("Ghana", "gh");
				put("Greece", "gr");
				put("Guatemala", "gt");
				put("Guyana", "gy");
				put("Haiti", "ht");
				put("Honduras", "hn");
				put("Hong Kong", "hk");
				put("Hungary", "hu");
				put("Iceland", "is");
				put("India", "in");
				put("Indonesia", "id");
				put("Ireland", "ie");
				put("Israel", "il");
				put("Italy", "it");
				put("Jamaica", "jm");
				put("Japan", "jp");
				put("Jordan", "jo");
				put("Kazakhstan", "kz");
				put("Kuwait", "kw");
				put("Latvia", "lv");
				put("Lebanon", "lb");
				put("Libya", "ly");
				put("Lithuania", "lt");
				put("Luxembourg", "lu");
				put("Madagascar", "mg");
				put("Malaysia", "my");
				put("Malta", "mt");
				put("Mauritius", "mu");
				put("Mexico", "mx");
				put("Moldova", "md");
				put("Mongolia", "mn");
				put("Montenegro", "me");
				put("Morocco", "ma");
				put("Mozambique", "mz");
				put("Namibia", "na");
				put("Nepal", "np");
				put("Netherlands", "nl");
				put("New Zealand", "nz");
				put("Nicaragua", "ni");
				put("Nigeria", "ng");
				put("Norway", "no");
				put("Oman", "om");
				put("Paraguay", "py");
				put("Peru", "pe");
				put("Philippines", "ph");
				put("Poland", "pl");
				put("Portugal", "pt");
				put("Romania", "ro");
				put("Russia", "ru");
				put("Saudi Arabia", "sa");
				put("Senegal", "sn");
				put("Serbia", "rs");
				put("Singapore", "sg");
				put("Slovakia", "sk");
				put("Slovenia", "si");
				put("South Africa", "za");
				put("South Korea", "kr");
				put("Spain", "es");
				put("Sri Lanka", "lk");
				put("Sweden", "se");
				put("Thailand", "th");
				put("Trinidad and Tobago", "tt");
				put("Tunisia", "tn");
				put("Turkey", "tr");
				put("Ukraine", "ua");
				put("United Arab Emirates", "ae");
				put("United Kingdom", "uk");
				put("United States", "us");
				put("Unknown", "us");
				put("Uruguay", "uy");
				put("Venezuela", "ve");
				put("Vietnam", "vn");
				put("Zambia", "zm");
				put("Zimbabwe", "zw");
			}
		};

	@Value("${osb.asah.seo.semrush.databases.limit:5}")
	private int _databasesLimit;

	@Autowired
	private Environment _environment;

	@Autowired
	private Http _http;

	@Value("${osb.asah.seo.semrush.api.key}")
	private String _semrushAPIKey;

	@Value("${osb.asah.seo.semrush.url.adwords.display.limit:10}")
	private int _urlAdwordsDisplayLimit;

	@Value("${osb.asah.seo.semrush.url.organic.display.limit:25}")
	private int _urlOrganicDisplayLimit;

}