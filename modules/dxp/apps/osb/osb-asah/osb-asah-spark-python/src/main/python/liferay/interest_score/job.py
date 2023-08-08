#
# Copyright (c) 2000-present Liferay, Inc. All rights reserved.
#
# The contents of this file are subject to the terms of the Liferay Enterprise
# Subscription License ("License"). You may not use this file except in
# compliance with the License. You can obtain a copy of the License by
# contacting Liferay, Inc. See the License for the specific language governing
# permissions and limitations under the License, including but not limited to
# distribution rights of the Software.
#

from abc import ABCMeta, \
	abstractmethod

from liferay.common.spark import BaseSparkJob

from pyspark.ml import Pipeline
from pyspark.sql import Window, \
	functions as F

from sparknlp.annotator import Chunker, \
	LanguageDetectorDL, \
	NerConverter, \
	NerDLModel, \
	Normalizer, \
	PerceptronModel, \
	RoBertaEmbeddings, \
	SentenceDetector, \
	Tokenizer
from sparknlp.base import DocumentAssembler, \
	Finisher

import json
import logging

class BaseBigQueryDataFrameWriterSparkJob(BaseSparkJob):

	def __init__(self, spark_application, bigquery_dataset, mode='append', table_name=None):
		BaseSparkJob.__init__(self, spark_application)

		self.bigquery_dataset = bigquery_dataset
		self.mode = mode
		self.table_name = table_name

	def _pre_process(self, data_frame):
		return data_frame

	def run(self):
		data_frame = self.spark_session.table(self.table_name)

		data_frame = self._pre_process(data_frame)

		data_frame_writer = data_frame.write

		data_frame_writer.format(
			'bigquery'
		).mode(
			self.mode
		).option(
			'createDisposition',
			'CREATE_NEVER'
		).save(
			'{}.{}'.format(
				self.spark_application_args.ac_project_id,
				self.bigquery_dataset
			)
		)

class BaseSQLCommandSparkJob(BaseSparkJob, metaclass=ABCMeta):

	def __init__(self, spark_application, temp_view):
		BaseSparkJob.__init__(self, spark_application)

		self._temp_view = temp_view

	def _get_job_parameter(self, parameter_name, default_value=None):
		job_parameters = json.loads(self.spark_application_args.job_parameters)

		for job_parameter in job_parameters:
			if job_parameter.get('name') == parameter_name:
				return job_parameter.get('value')

		return default_value

	@abstractmethod
	def get_sql_command(self, end_date, start_date, time_zone=None):
		pass

	def run(self):
		end_date = self._get_job_parameter('endDate')
		start_date = self._get_job_parameter('startDate')
		time_zone = self._get_job_parameter('timeZone', None)

		sql_command = self.get_sql_command(end_date, start_date, time_zone)

		data_frame = self.spark_session.sql(sql_command)

		data_frame.createOrReplaceTempView(self._temp_view)

		self.spark_session.catalog.cacheTable(self._temp_view)

class DLKeywordsExtractionSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(DLKeywordsExtractionSparkJob, self).__init__(spark_application)

		self._log = logging.getLogger(self.__class__.__name__)

		interest_models_path = spark_application.configuration.get(
			'interest.models.path')

		embeddings_lang = spark_application.configuration.get(
			'interest.models.embeddings.lang')

		embeddings_name = spark_application.configuration.get(
			'interest.models.embeddings.name')

		try:
			self._embeddings = RoBertaEmbeddings.load(
				'{}/{}_{}'.format(
					interest_models_path,
					embeddings_name,
					embeddings_lang
				)
			)
		except Exception as exception:
			self._log.warning(
				'Unable to load embeddings {}'.format(exception))

			self._embeddings = RoBertaEmbeddings.pretrained(
				lang=embeddings_lang,
				name=embeddings_name
			)

		language_detector_lang = spark_application.configuration.get(
			'interest.models.language_detector.lang')

		language_detector_name = spark_application.configuration.get(
			'interest.models.language_detector.name')

		try:
			self._language_detector = LanguageDetectorDL.load(
				'{}/{}_{}'.format(
					interest_models_path,
					language_detector_name,
					language_detector_lang)
			)
		except Exception as exception:
			self._log.warning(
				'Unable to load Language Detector {}'.format(exception))

			self._language_detector = LanguageDetectorDL.pretrained(
				lang=language_detector_lang,
				name=language_detector_name
			)

		ner_lang = spark_application.configuration.get(
			'interest.models.ner.lang')

		ner_name = spark_application.configuration.get(
			'interest.models.ner.name')

		try:
			self._ner_model = NerDLModel.load(
				'{}/{}_{}'.format(
					interest_models_path,
					ner_name,
					ner_lang
				)
			)
		except Exception as exception:
			self._log.warning(
				'Unable to load NER model {}'.format(exception))

			self._ner_model = NerDLModel.pretrained(
				lang=ner_lang,
				name=ner_name
			)

		pos_tagger_lang = spark_application.configuration.get(
			'interest.models.pos_tagger.lang')

		pos_tagger_name = spark_application.configuration.get(
			'interest.models.pos_tagger.name')

		try:
			self._part_of_speech_tagger = PerceptronModel.load(
				'{}/{}_{}'.format(
					interest_models_path,
					pos_tagger_name,
					pos_tagger_lang
				)
			)
		except Exception as exception:
			self._log.warning(
				'Unable to load Part of Speech Tagger {}'.format(exception))

			self._part_of_speech_tagger = PerceptronModel.pretrained(
				lang=pos_tagger_lang,
				name=pos_tagger_name
			)

	def _create_chunker_stage(self, column_names, output_column_name):
		chunker = Chunker()

		chunker.setInputCols(column_names)
		chunker.setOutputCol(output_column_name)
		chunker.setRegexParsers([
			'<JJ>*<NN[PS]*>+',
		])

		return chunker

	def _create_document_assembler_stage(self, column_name):
		document_assembler = DocumentAssembler()

		document_assembler.setInputCol(column_name)
		document_assembler.setOutputCol(f'{column_name}_document')

		return document_assembler

	def _create_embeddings_stage(self, column_names, output_column_name):
		embeddings = self._embeddings

		embeddings.setInputCols(column_names)
		embeddings.setOutputCol(output_column_name)

		return embeddings

	def _create_finisher_stage(
			self, column_names, output_column_name, includeMetadata=False
		):

		finisher = Finisher()

		finisher.setCleanAnnotations(False)
		finisher.setIncludeMetadata(includeMetadata)
		finisher.setInputCols(column_names)
		finisher.setOutputCols(output_column_name)

		return finisher

	def _create_language_detector_stage(self, column_name):
		language_detector = self._language_detector

		language_detector.setInputCols(column_name)
		language_detector.setThreshold(
			self.spark_application.configuration.get(
				'interest.models.language_detector.threshold'))

		return language_detector

	def _create_ner_converter(self, column_names, output_column_name):
		ner_converter = NerConverter()

		ner_converter.setInputCols(column_names)
		ner_converter.setOutputCol(output_column_name)

		return ner_converter

	def _create_ner_model(self, column_names, output_column_name):
		ner_model = self._ner_model

		ner_model.setInputCols(column_names)
		ner_model.setOutputCol(output_column_name)

		return ner_model

	def _create_normalizer_stage(self, column_names, output_column_name):
		normalizer = Normalizer()

		normalizer.setCleanupPatterns(['(?U)[^\w\d\s]'])
		normalizer.setInputCols(column_names)
		normalizer.setOutputCol(output_column_name)

		return normalizer

	def _create_pos_tagger_stage(self, column_names, output_column_name):
		pos_tagger = self._part_of_speech_tagger

		pos_tagger.setInputCols(column_names)
		pos_tagger.setOutputCol(output_column_name)

		return pos_tagger

	def _create_sentence_detector_stage(self, column_names, output_column_name):
		sentence_detector = SentenceDetector()

		sentence_detector.setInputCols(column_names)
		sentence_detector.setOutputCol(output_column_name)

		return sentence_detector

	def _create_tokenizer_stage(self, column_names, output_column_name):
		tokenizer = Tokenizer()

		tokenizer.setInputCols(column_names)
		tokenizer.setOutputCol(output_column_name)
		tokenizer.setSplitChars(['|', '(', ')'])

		return tokenizer

	def _generate_language_detector_pipeline(self):
		pipeline = Pipeline()

		document_assembler = self._create_document_assembler_stage(
			'title_and_description')

		language_detector = self._create_language_detector_stage([
			document_assembler.getOutputCol()
		])

		finisher_stage = self._create_finisher_stage(
			language_detector.getOutputCol(), 'detected_language')

		pipeline.setStages([
			document_assembler,
			language_detector,
			finisher_stage
		])

		return pipeline

	def _generate_ner_pipeline(self, column_name):
		pipeline = Pipeline()

		document_assembler = self._create_document_assembler_stage(
			column_name)

		tokenizer = self._create_tokenizer_stage(
			document_assembler.getOutputCol(),
			f'{column_name}_tokens'
		)

		embeddings = self._create_embeddings_stage([
			tokenizer.getOutputCol(),
			document_assembler.getOutputCol()],
			f'{column_name}_embeddings'
		)

		ner_model = self._create_ner_model([
			document_assembler.getOutputCol(),
			tokenizer.getOutputCol(),
			embeddings.getOutputCol()],
			f'{column_name}_ner'
		)

		ner_converter = self._create_ner_converter([
			document_assembler.getOutputCol(),
			tokenizer.getOutputCol(),
			ner_model.getOutputCol()],
			f'{column_name}_ner_converter'
		)

		finisher = self._create_finisher_stage(
			ner_converter.getOutputCol(),
			f'{column_name}_entities',
			includeMetadata=True
		)

		pipeline.setStages([
			document_assembler,
			tokenizer,
			embeddings,
			ner_model,
			ner_converter,
			finisher
		])

		return pipeline

	def _generate_pos_tagger_pipeline(self, column_name):
		pipeline = Pipeline()

		document_assembler = self._create_document_assembler_stage(column_name)

		sentence_detector = self._create_sentence_detector_stage(
			[document_assembler.getOutputCol()],
			f'{column_name}_sentences'
		)

		tokenizer = self._create_tokenizer_stage(
			sentence_detector.getOutputCol(), f'{column_name}_tokens'
		)

		normalizer = self._create_normalizer_stage(
			tokenizer.getOutputCol(), f'{column_name}_normalized'
		)

		part_of_speech_tagger = self._create_pos_tagger_stage([
			sentence_detector.getOutputCol(),
			tokenizer.getOutputCol()],
			f'{column_name}_pos'
		)

		chunker = self._create_chunker_stage([
			sentence_detector.getOutputCol(),
			part_of_speech_tagger.getOutputCol()],
			f'{column_name}_chunks'
		)

		finisher = self._create_finisher_stage(
			chunker.getOutputCol(),
			f'{column_name}_entities'
		)

		pipeline.setStages([
			document_assembler,
			sentence_detector,
			tokenizer,
			normalizer,
			part_of_speech_tagger,
			chunker,
			finisher
		])

		return pipeline

	def _get_sample_analytics_event_by_canonical_url(self, analytics_events_data_frame):
		window = Window.partitionBy('canonicalUrl')

		sample_data_frame = analytics_events_data_frame.withColumn(
			'window_count', F.count('*').over(window)
		)

		sample_data_frame = sample_data_frame.withColumn(
			'row_number',
			F.row_number().over(window.orderBy(F.desc('window_count')))
		)

		return sample_data_frame.filter('row_number = 1')

	def run(self):
		analytics_events_data_frame = self.spark_session.table(
			'analytics_events'
		)

		nlp_data_frame = self._get_sample_analytics_event_by_canonical_url(
			analytics_events_data_frame
		)

		nlp_data_frame = nlp_data_frame.withColumn(
			'title', F.col('title')
		).fillna('')

		nlp_data_frame = nlp_data_frame.withColumn(
			'description', F.col('description')
		).fillna('')

		nlp_data_frame = nlp_data_frame.withColumn(
			'title_and_description',
			F.concat_ws('.\n', F.col('title'), F.col('description'))
		)

		language_detection_pipeline = \
			self._generate_language_detector_pipeline()

		nlp_data_frame = language_detection_pipeline.fit(
			nlp_data_frame
		).transform(
			nlp_data_frame
		)

		nlp_data_frame = nlp_data_frame.filter(
			F.array_contains(
				F.col('detected_language'), 'en')
		)

		title_pos_tagger_pipeline = self._generate_pos_tagger_pipeline(
			column_name='title'
		)

		nlp_data_frame = title_pos_tagger_pipeline.fit(
			nlp_data_frame
		).transform(
			nlp_data_frame
		)

		description_ner_pipeline = self._generate_ner_pipeline(
			column_name='description'
		)

		nlp_data_frame = description_ner_pipeline.fit(
			nlp_data_frame
		).transform(
			nlp_data_frame
		)

		filter_entity_type = self.spark_application_configuration.get(
			'interest.filter.entity.type')

		if filter_entity_type:
			nlp_data_frame = nlp_data_frame.withColumn(
				'temp',
				F.explode(
					F.arrays_zip(
						F.col('description_entities'),
						F.col('description_entities_metadata')
					)
				)
			).withColumn(
				'description_entity',
				F.col(
					'temp'
				).getItem(
					'description_entities'
				)
			).withColumn(
				'key',
				F.col(
					'temp'
				).getItem(
					'description_entities_metadata'
				).getItem(
					'_1'
				)
			).withColumn(
				'value',
				F.col(
					'temp'
				).getItem(
					'description_entities_metadata'
				).getItem(
					'_2'
				)
			).filter(
				'key = "entity"'
			).filter(
				'value NOT IN ("CARDINAL", "DATE", "ORDINAL", "PERCENT",'
				'"QUANTITY", "TIME")'
			).groupBy(
				'description', 'canonicalUrl', 'description', 'title_entities',
				'title', 'title_and_description', 'keywords'
			).agg(
				F.collect_list(
					'description_entity'
				).alias(
					'extracted_description_keywords'
				)
			).withColumn(
				'extracted_description_keywords',
				F.array_distinct(
					'extracted_description_keywords'
				)
			).withColumnRenamed(
				'title_entities',
				'extracted_title_keywords'
			)
		else:
			nlp_data_frame = nlp_data_frame.withColumnRenamed(
				'title_entities',
				'extracted_title_keywords'
			).withColumnRenamed(
				'description_entities',
				'extracted_description_keywords'
			)

		extracted_keywords_data_frame = nlp_data_frame.withColumn(
			'extracted_keywords',
			F.array(
				F.col('extracted_title_keywords'),
				F.col('extracted_description_keywords'),
				F.split(F.col('keywords'), ",")
			)
		).withColumn(
			'extracted_keywords', F.flatten(F.col('extracted_keywords'))
		).withColumn(
			'extracted_keywords', F.array_distinct('extracted_keywords')
		).withColumn(
			'extracted_keywords', F.array_remove('extracted_keywords', '')
		).withColumn(
			'extracted_keywords',
			F.transform(
				F.col('extracted_keywords'),
				lambda x: F.lower(F.trim(x))
			)
		).select(
			'canonicalUrl', 'description', 'extracted_keywords', 'title',
			'title_and_description'
		)

		extracted_keywords_data_frame.createOrReplaceTempView(
			'extracted_keywords'
		)

		self.spark_session.catalog.cacheTable('extracted_keywords')

class IdentityInterestScoreBigQueryDataFrameWriterSparkJob(BaseBigQueryDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		BaseBigQueryDataFrameWriterSparkJob.__init__(
			self,
			spark_application,
			'identityinterestscore',
			'append',
			'identityinterestscore'
		)


class IdentityInterestScorePageBigQueryDataFrameWriterSparkJob(BaseBigQueryDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		BaseBigQueryDataFrameWriterSparkJob.__init__(
			self,
			spark_application,
			'identityinterestpage',
			'overwrite',
			'identity_interest_pages'
		)

class IdentityInterestScorePageSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		BaseSparkJob.__init__(self, spark_application)

	def _get_identity_interest_pages_data_frame(
			self, analytics_events_data_frame, extracted_keywords_data_frame):

		return analytics_events_data_frame.groupby(
			'channelId', 'canonicalUrl', 'userId'
		).agg(
			F.count('*').alias('views')
		).join(
			extracted_keywords_data_frame,
			how='inner',
			on=['canonicalUrl']
		).withColumn(
			'keyword', F.explode(F.col('extracted_keywords'))
		).select(
			F.col('canonicalUrl'),
			F.col('channelId'),
			F.col('userId').alias('identityId'),
			F.col('keyword'),
			F.col('title'),
			F.col('views')
		)

	def run(self):
		analytics_events_data_frame = self.spark_session.table(
			'analytics_events'
		)

		extracted_keywords_data_frame = self.spark_session.table(
			'extracted_keywords'
		)

		identity_interest_pages_data_frame = self._get_identity_interest_pages_data_frame(
			analytics_events_data_frame,
			extracted_keywords_data_frame)

		identity_interest_pages_data_frame.createOrReplaceTempView(
			'identity_interest_pages'
		)

class IdentityInterestScorePrepareAnalyticsEventsWithKeywordsSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		BaseSparkJob.__init__(self, spark_application)

	def run(self):
		analytics_events_data_frame = self.spark_session.table(
			'analytics_events'
		)

		extracted_keywords_data_frame = self.spark_session.table(
			'extracted_keywords'
		)

		analytics_events_with_keywords_data_frame = \
			analytics_events_data_frame.drop(
				'description', 'keywords', 'title'
			).join(
				extracted_keywords_data_frame,
				how='inner',
				on=['canonicalUrl']
			)

		analytics_events_with_keywords_data_frame.createOrReplaceTempView(
			'analytics_events_with_keywords')

		self.spark_session.catalog.cacheTable('analytics_events_with_keywords')

class IdentityInterestScoreSQLCommandSparkJob(BaseSQLCommandSparkJob):

	def __init__(self, spark_application):
		BaseSQLCommandSparkJob.__init__(self, spark_application, 'identityinterestscore')

		self._initial_run_day_range = 7

	def get_sql_command(self, end_date, start_date, time_zone=None):
		if end_date and start_date:
			end_date_sql_string = f'"{end_date}"'
			start_date_sql_string = f'"{start_date}"'
		else:
			end_date_sql_string = f'''
				FROM_UTC_TIMESTAMP(CURRENT_DATE(), "{time_zone}")
			'''
			start_date_sql_string = f'''
				DATE_SUB(
					FROM_UTC_TIMESTAMP(CURRENT_DATE(), "{time_zone}"),
					{self._initial_run_day_range}
				)
			'''

		return f"""
			SELECT
				channelId,
				userId as identityId,
				interested,
				MAX(interest_score) as interestScore,
				keyword,
				event_date as recordedDate
			FROM
				individual_interest_score
			WHERE
				event_date >= {start_date_sql_string} AND
				event_date < {end_date_sql_string}
			GROUP BY
				channelId,
				identityId,
				interested,
				keyword,
				recordedDate
		"""


class IndividualInterestScoreSparkJob(BaseSparkJob):

	def _30_day_sum(self, column, window):
		values = list()

		offsets = [i for i in range(0, 30)]

		for i in offsets:
			values.append(
				F.coalesce(F.lag(column, i).over(window), F.lit(0))
			)

		return sum(values, F.lit(0))

	def __init__(self, spark_application):
		BaseSparkJob.__init__(self, spark_application)

		self._global_keyword_weight = 1.0
		self._initial_run_day_range = 7
		self._interest_score_decay_rate = 0.9
		self._interest_score_minimum_threshold = 0.1
		self._max_days_delta = 60
		self._minimum_logscore_threshold = 0.01
		self._user_keyword_weight = 2.0

		self._decay_threshold_weight = self._interest_score_decay_rate ** 14

	def _get_date_range_sql_command(self, end_date=None, start_date=None, time_zone=None):
		if end_date and start_date:
			end_date_sql_string = f"""TO_DATE('{end_date}')"""
			start_date_sql_string = f"""
				DATE_ADD(TO_DATE('{start_date}'), -{self._max_days_delta})
			"""
		else:
			end_date_sql_string = f"""
				TO_DATE(FROM_UTC_TIMESTAMP(CURRENT_DATE(), '{time_zone}'))
			"""
			start_date_sql_string = f"""
				TO_DATE(
					DATE_ADD(
						FROM_UTC_TIMESTAMP(CURRENT_DATE(), '{time_zone}'),
						-{self._max_days_delta + self._initial_run_day_range}
					)
				)
			"""

		return f"""
			SELECT SEQUENCE(
				{start_date_sql_string}, {end_date_sql_string}, INTERVAL 1 DAY
			) AS event_date
		"""


	def _get_job_parameter(self, parameter_name, default_value=None):
		job_parameters = json.loads(self.spark_application_args.job_parameters)

		for job_parameter in job_parameters:
			if job_parameter.get('name') == parameter_name:
				return job_parameter.get('value')

		return default_value

	def _get_keyword_count_with_totals_data_frame(
		self, analytics_events_with_keywords_data_frame):

		keyword_count_data_frame = \
			analytics_events_with_keywords_data_frame.withColumn(
				'keyword', F.explode(F.col('extracted_keywords'))
			).groupby(
				'event_date', 'keyword'
			).count(
			).withColumnRenamed(
				'count', 'keyword_count'
			)

		total_keywords_count_data_frame = keyword_count_data_frame.groupby(
			'event_date'
		).sum(
		).withColumnRenamed(
			'sum(keyword_count)', 'total_keywords_count'
		)

		return keyword_count_data_frame.join(
			total_keywords_count_data_frame,
			how='left',
			on=['event_date']
		)

	def _get_user_keyword_count_with_totals_data_frame(
		self, analytics_events_with_keywords_data_frame):

		user_keyword_count_data_frame = \
			analytics_events_with_keywords_data_frame.withColumn(
				'keyword', F.explode(F.col('extracted_keywords'))
			).groupBy(
				'channelId', 'userId', 'event_date', 'keyword'
			).count(
			).withColumnRenamed(
				'count', 'user_keyword_count'
			)

		user_total_keyword_count_data_frame = \
			user_keyword_count_data_frame.groupby(
				'channelId', 'userId', 'event_date'
			).sum(
				'user_keyword_count'
			).withColumnRenamed(
				'sum(user_keyword_count)', 'user_total_keyword_count'
			)

		return user_keyword_count_data_frame.join(
			user_total_keyword_count_data_frame,
			how='inner',
			on=['channelId', 'userId', 'event_date']
		)

	def _weighted_average(self, column, offsets, weights, window):
		values = list()

		for i, weight in zip(offsets, weights):
			values.append(
				F.coalesce(F.lag(column, i).over(window) * weight, F.lit(0))
			)

		return sum(values, F.lit(0))

	def run(self):
		analytics_events_with_keywords_data_frame = self.spark_session.table(
			'analytics_events_with_keywords'
		)

		keyword_count_with_totals_data_frame = \
			self._get_keyword_count_with_totals_data_frame(
				analytics_events_with_keywords_data_frame
			)

		user_keyword_counts_with_totals_data_frame = \
			self._get_user_keyword_count_with_totals_data_frame(
				analytics_events_with_keywords_data_frame
			)

		daily_logscores_data_frame = \
			user_keyword_counts_with_totals_data_frame.join(
				keyword_count_with_totals_data_frame,
				how='left',
				on=['event_date', 'keyword']
			)

		daily_logscores_data_frame = daily_logscores_data_frame.withColumn(
			'logscore',
			F.log10(
				(self._global_keyword_weight * F.col('total_keywords_count') +
				 self._user_keyword_weight * F.col('user_total_keyword_count')) /
				(self._global_keyword_weight * F.col('keyword_count') +
				 self._user_keyword_weight * F.col('user_keyword_count'))
			)
		)

		threshold_data_frame = keyword_count_with_totals_data_frame.select(
			'event_date', 'keyword', 'keyword_count', 'total_keywords_count'
		).fillna(
			{'keyword_count': 0,
			 'total_keywords_count': 0}
		).withColumn(
			'keyword_view_count_30_days',
			self._30_day_sum(
				F.col('keyword_count'),
				Window.partitionBy('keyword').orderBy('event_date')
			)
		).withColumn(
			'all_keyword_views_30_days',
			self._30_day_sum(
				F.col('total_keywords_count'),
				Window.partitionBy('keyword').orderBy('event_date')
			)
		).withColumn(
			'threshold_score',
			F.log(
				10.0,
				(
					F.col('all_keyword_views_30_days') /
					F.col('keyword_view_count_30_days')
				)
			) * self._decay_threshold_weight
		)

		sql_command = self._get_date_range_sql_command(
			end_date=self._get_job_parameter('endDate'),
			start_date=self._get_job_parameter('startDate'),
			time_zone=self._get_job_parameter('timeZone')
		)

		date_range_data_frame = self.spark_session.sql(
			sql_command
		).withColumn(
			'event_date', F.explode(F.col('event_date'))
		)

		date_user_id_keyword_data_frame = date_range_data_frame.crossJoin(
			daily_logscores_data_frame.select(
				F.col('channelId'),
				F.col('userId'),
				F.col('keyword')
			).distinct()
		)

		expanded_dates_logscore_data_frame = \
			date_user_id_keyword_data_frame.join(
				daily_logscores_data_frame,
				['channelId', 'event_date', 'keyword', 'userId'],
				how='left'
			)

		offsets = [i for i in range(0, self._max_days_delta)]
		weights = [
			self._interest_score_decay_rate ** i
			for i in range(0, self._max_days_delta)
		]

		interest_score_data_frame = expanded_dates_logscore_data_frame.select(
			'channelId', 'event_date', 'keyword', 'logscore', 'userId'
		).fillna(
			{'logscore': 0}
		).withColumn(
			'interest_score',
			self._weighted_average(
				F.col('logscore'), offsets, weights,
				Window.partitionBy('channelId', 'userId', 'keyword').orderBy(
					'event_date'
				)
			)
		).withColumn(
			'interest_score',
			F.when(
				F.col('interest_score') < self._minimum_logscore_threshold, 0
			).otherwise(
				F.col('interest_score')
			)
		)

		interest_score_data_frame = interest_score_data_frame.join(
			threshold_data_frame,
			['event_date', 'keyword'],
			how='left'
		).fillna(
			{
				'threshold_score': self._interest_score_minimum_threshold
			}
		)

		interest_score_data_frame = interest_score_data_frame.withColumn(
			'interested',
			F.col('interest_score') >= F.col('threshold_score')
		)

		session_data_frame = analytics_events_with_keywords_data_frame.select(
			F.col('channelId'),
			F.col('event_date'),
			F.col('userId'),
			F.col('sessionId')
		).distinct()

		interest_score_data_frame = interest_score_data_frame.join(
			session_data_frame,
			['channelId', 'event_date', 'userId'],
			how='left'
		)

		interest_score_data_frame.createOrReplaceTempView(
			'individual_interest_score')

class KeywordsExtractionSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		self._log = logging.getLogger(self.__class__.__name__)

		interest_models_path = spark_application.configuration.get(
			'interest.models.path')

		language_detector_lang = spark_application.configuration.get(
			'interest.models.language_detector.lang')

		language_detector_name = spark_application.configuration.get(
			'interest.models.language_detector.name')

		try:
			self._language_detector = LanguageDetectorDL.load(
				'{}/{}_{}'.format(
					interest_models_path,
					language_detector_name,
					language_detector_lang)
			)
		except Exception as exception:
			self._log.warning(
				'Unable to load Language Detector {}'.format(exception))

			self._language_detector = LanguageDetectorDL.pretrained(
				lang=language_detector_lang,
				name=language_detector_name
			)

		pos_tagger_lang = spark_application.configuration.get(
			'interest.models.pos_tagger.lang')

		pos_tagger_name = spark_application.configuration.get(
			'interest.models.pos_tagger.name')

		try:
			self._part_of_speech_tagger = PerceptronModel.load(
				'{}/{}_{}'.format(
					interest_models_path,
					pos_tagger_name,
					pos_tagger_lang
				)
			)
		except Exception as exception:
			self._log.warning(
				'Unable to load Part of Speech Tagger {}'.format(exception))

			self._part_of_speech_tagger = PerceptronModel.pretrained(
				lang=pos_tagger_lang,
				name=pos_tagger_name
			)

		super(KeywordsExtractionSparkJob, self).__init__(spark_application)

	def _create_chunker_stage(self, column_names, output_column_name):
		chunker = Chunker()

		chunker.setInputCols(column_names)
		chunker.setOutputCol(output_column_name)
		chunker.setRegexParsers([
			'<JJ>*<NN[PS]*>+',
		])

		return chunker

	def _create_document_assembler_stage(self, column_name):
		document_assembler = DocumentAssembler()

		document_assembler.setInputCol(column_name)
		document_assembler.setOutputCol(f'{column_name}_document')

		return document_assembler

	def _create_finisher_stage(self, column_names, output_column_name):
		finisher = Finisher()

		finisher.setCleanAnnotations(False)
		finisher.setInputCols(column_names)
		finisher.setOutputCols(output_column_name)

		return finisher

	def _create_language_detector_stage(self, column_name):
		language_detector = self._language_detector

		language_detector.setInputCols(column_name)
		language_detector.setThreshold(
			self.spark_application.configuration.get(
				'interest.models.language_detector.threshold'))

		return language_detector

	def _create_normalizer_stage(self, column_names, output_column_name):
		normalizer = Normalizer()

		normalizer.setCleanupPatterns(['(?U)[^\w\d\s]'])
		normalizer.setInputCols(column_names)
		normalizer.setOutputCol(output_column_name)

		return normalizer

	def _create_pos_tagger_stage(self, column_names, output_column_name):
		pos_tagger = self._part_of_speech_tagger

		pos_tagger.setInputCols(column_names)
		pos_tagger.setOutputCol(output_column_name)

		return pos_tagger

	def _create_sentence_detector_stage(self, column_names, output_column_name):
		sentence_detector = SentenceDetector()

		sentence_detector.setInputCols(column_names)
		sentence_detector.setOutputCol(output_column_name)

		return sentence_detector

	def _create_tokenizer_stage(self, column_names, output_column_name):
		tokenizer = Tokenizer()

		tokenizer.setInputCols(column_names)
		tokenizer.setOutputCol(output_column_name)
		tokenizer.setSplitChars(['|', '(', ')'])

		return tokenizer

	def _generate_language_detector_pipeline(self):
		pipeline = Pipeline()

		document_assembler = self._create_document_assembler_stage(
			'title_and_description')

		language_detector = self._create_language_detector_stage([
			document_assembler.getOutputCol()
		])

		finisher_stage = self._create_finisher_stage(
			language_detector.getOutputCol(), 'detected_language')

		pipeline.setStages([
			document_assembler,
			language_detector,
			finisher_stage
		])

		return pipeline

	def _generate_nlp_pipeline(self):
		pipeline = Pipeline()

		description_document_assembler = self._create_document_assembler_stage(
			'description')

		sentence_detector = self._create_sentence_detector_stage(
			[description_document_assembler.getOutputCol()],
			'description_sentences')

		tokenizer = self._create_tokenizer_stage(
			sentence_detector.getOutputCol(), 'description_tokens')

		normalizer = self._create_normalizer_stage(
			tokenizer.getOutputCol(), 'description_normalized')

		part_of_speech_tagger = self._create_pos_tagger_stage([
			sentence_detector.getOutputCol(),
			tokenizer.getOutputCol()],
			'description_pos')

		chunker = self._create_chunker_stage([
			sentence_detector.getOutputCol(),
			part_of_speech_tagger.getOutputCol()],
			'description_chunks')

		finisher = self._create_finisher_stage(
			chunker.getOutputCol(),
			'extracted_keywords')

		pipeline.setStages([
			description_document_assembler,
			sentence_detector,
			tokenizer,
			normalizer,
			part_of_speech_tagger,
			chunker,
			finisher
		])

		return pipeline

	def _get_sample_analytics_event_by_canonical_url(
		self, analytics_events_data_frame):

		window = Window.partitionBy('canonicalUrl')

		sample_data_frame = analytics_events_data_frame.withColumn(
			'window_count', F.count('*').over(window)
		)

		sample_data_frame = sample_data_frame.withColumn(
			'row_number',
			F.row_number().over(window.orderBy(F.desc('window_count')))
		)

		return sample_data_frame.filter('row_number = 1')

	def run(self):
		analytics_events_data_frame = self.spark_session.table(
			'analytics_events'
		)

		nlp_data_frame = self._get_sample_analytics_event_by_canonical_url(
			analytics_events_data_frame
		)

		nlp_data_frame = nlp_data_frame.withColumn(
			'title', F.col('title')
		).fillna('')

		nlp_data_frame = nlp_data_frame.withColumn(
			'description', F.col('description')
		).fillna('')

		nlp_data_frame = nlp_data_frame.withColumn(
			'title_and_description',
			F.concat_ws('.\n', F.col('title'), F.col('description')))

		language_detection_pipeline = \
			self._generate_language_detector_pipeline()

		nlp_data_frame = language_detection_pipeline.fit(
			nlp_data_frame
		).transform(
			nlp_data_frame
		)

		nlp_data_frame = nlp_data_frame.filter(
			F.array_contains(
				F.col('detected_language'), 'en'))

		keyword_pipeline = self._generate_nlp_pipeline()

		nlp_data_frame = keyword_pipeline.fit(
			nlp_data_frame
		).transform(
			nlp_data_frame
		)

		extracted_keywords_data_frame = nlp_data_frame.withColumn(
			'extracted_keywords',
			F.array(
				F.col('extracted_keywords'),
				F.split(F.col('keywords'), ","))
		).withColumn(
			'extracted_keywords', F.flatten(F.col('extracted_keywords'))
		).withColumn(
			'extracted_keywords', F.array_distinct('extracted_keywords')
		).withColumn(
			'extracted_keywords', F.array_remove('extracted_keywords', '')
		).withColumn(
			'extracted_keywords',
			F.transform(
				F.col('extracted_keywords'),
				lambda x: F.lower(F.trim(x)))
		).select(
			'canonicalUrl', 'description', 'extracted_keywords', 'title',
			'title_and_description'
		)

		extracted_keywords_data_frame.createOrReplaceTempView(
			'extracted_keywords')

		self.spark_session.catalog.cacheTable('extracted_keywords')

class ReadAnalyticsEventsSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		BaseSparkJob.__init__(self, spark_application)

		self._initial_run_day_range = 7
		self._log = logging.getLogger(self.__class__.__name__)
		self._max_days_delta = 60
		self._minimum_interactions_threshold = 5
		self._minimum_view_duration_threshold = 5000

	def _get_event_data_sql_command(self, end_date=None, start_date=None, time_zone=None):
		if end_date and start_date:
			sql_end_date_string = f'"{end_date}"'
			sql_start_date_string = \
				f'DATE_SUB("{start_date}", INTERVAL {self._max_days_delta} DAY)'
		else:
			sql_end_date_string = f'CURRENT_DATE("{time_zone}")'
			sql_start_date_string = \
				'DATE_SUB(CURRENT_DATE("{}"), INTERVAL {} DAY)'.format(
					time_zone,
					self._max_days_delta + self._initial_run_day_range
				)

		sql_command = f"""
			WITH FilteredEvent AS ( 
				SELECT 
					event.canonicalUrl,
					event.channelId,
					DATE_DIFF(CURRENT_DATE("{time_zone}"), DATE(event.eventDate, "{time_zone}"), DAY) as days_delta,
					TRIM(event.description) as description, 
					DATE(event.eventDate, "{time_zone}") as event_date,
					event.id,
					COUNT(event.userId) OVER (PARTITION BY event.userId) AS interactions,  
					TRIM(event.keywords) as keywords,
					event.sessionId,
					TRIM(event.title) as title, 
					event.userId, 
					eventproperty.name, 
					eventproperty.value
				FROM 
					`{self.spark_application_args.ac_project_id}`.event 
				JOIN `{self.spark_application_args.ac_project_id}`.eventproperty ON 
					event.id = eventproperty.id
				WHERE 
					DATE(event.eventDate, "{time_zone}") >= {sql_start_date_string} AND
					DATE(event.eventDate, "{time_zone}") < {sql_end_date_string} AND
					DATE(eventproperty.eventDate, "{time_zone}") >= {sql_start_date_string} AND
					DATE(eventproperty.eventDate, "{time_zone}") < {sql_end_date_string} AND
					eventproperty.name = "viewDuration" AND
					STARTS_WITH(event.contentLanguageId, 'en') AND
					CAST(eventproperty.value AS INTEGER) >= {self._minimum_view_duration_threshold}
				)
				SELECT
					canonicalUrl,
					channelId,
					days_delta,
					description, 
					event_date,
					id,
					keywords,  
					name, 
					sessionId,
					title, 
					userId, 
					value
				FROM 
					FilteredEvent
				WHERE 
					interactions >= {self._minimum_interactions_threshold}
			"""

		self._log.info(sql_command)

		return sql_command

	def _get_job_parameter(self, parameter_name, default_value=None):
		job_parameters = json.loads(self.spark_application_args.job_parameters)

		for job_parameter in job_parameters:
			if job_parameter.get('name') == parameter_name:
				return job_parameter.get('value')

		return default_value

	def run(self):
		end_date = self._get_job_parameter('endDate', None)
		start_date = self._get_job_parameter('startDate', None)
		time_zone = self._get_job_parameter('timeZone', None)

		sql_command = self._get_event_data_sql_command(end_date, start_date, time_zone)

		event_data_frame = self.spark_session.read.format(
			"bigquery"
		).load(sql_command)

		event_data_frame.createOrReplaceTempView(
			'analytics_events')

		self.spark_session.catalog.cacheTable('analytics_events')

class SessionInterestScoreBigQuerydataFrameWriterSparkJob(BaseBigQueryDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		BaseBigQueryDataFrameWriterSparkJob.__init__(
			self,
			spark_application,
			'sessioninterestscore',
			'append',
			'sessioninterestscore'
		)

class SessionInterestScoreSQLCommandSparkJob(BaseSQLCommandSparkJob):

	def __init__(self, spark_application):
		BaseSQLCommandSparkJob.__init__(self, spark_application, 'sessioninterestscore')

		self._initial_run_day_range = 7

	def get_sql_command(self, end_date, start_date, time_zone=None):
		if end_date and start_date:
			end_date_sql_string = f'"{end_date}"'
			start_date_sql_string = f'"{start_date}"'
		else:
			end_date_sql_string = f'''
				FROM_UTC_TIMESTAMP(CURRENT_DATE(), "{time_zone}")
			'''
			start_date_sql_string = f'''
				DATE_SUB(
					FROM_UTC_TIMESTAMP(CURRENT_DATE(), "{time_zone}"),
					{self._initial_run_day_range}
				)
			'''

		return f"""
			SELECT
				channelId,
				userId as identityId,
				interested,
				interest_score as interestScore,
				keyword,
				event_date as recordedDate,
				sessionId
			FROM
				individual_interest_score
			WHERE
				event_date >= {start_date_sql_string} AND
				event_date < {end_date_sql_string} AND
				sessionId IS NOT NULL
		"""