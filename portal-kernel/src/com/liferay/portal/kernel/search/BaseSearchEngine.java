/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.search;

import com.liferay.portal.kernel.search.dummy.DummyIndexSearcher;
import com.liferay.portal.kernel.search.dummy.DummyIndexWriter;
import com.liferay.portal.kernel.search.generic.BooleanClauseFactoryImpl;
import com.liferay.portal.kernel.search.generic.BooleanQueryFactoryImpl;
import com.liferay.portal.kernel.search.generic.TermQueryFactoryImpl;
import com.liferay.portal.kernel.search.generic.TermRangeQueryFactoryImpl;

/**
 * @author Bruno Farache
 * @author Carlos Sierra Andrés
 * @author Marcellus Tavares
 */
public class BaseSearchEngine implements SearchEngine {

	/**
	 * @throws SearchException
	 */
	@Override
	public String backup(long companyId, String backupName)
		throws SearchException {

		return null;
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	@Override
	public BooleanClauseFactory getBooleanClauseFactory() {
		if (_booleanClauseFactory == null) {
			_booleanClauseFactory = new BooleanClauseFactoryImpl();
		}

		return _booleanClauseFactory;
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	@Override
	public BooleanQueryFactory getBooleanQueryFactory() {
		if (_booleanQueryFactory == null) {
			_booleanQueryFactory = new BooleanQueryFactoryImpl();
		}

		return _booleanQueryFactory;
	}

	@Override
	public IndexSearcher getIndexSearcher() {
		return _indexSearcher;
	}

	@Override
	public IndexWriter getIndexWriter() {
		return _indexWriter;
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	@Override
	public TermQueryFactory getTermQueryFactory() {
		if (_termQueryFactory == null) {
			_termQueryFactory = new TermQueryFactoryImpl();
		}

		return _termQueryFactory;
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	@Override
	public TermRangeQueryFactory getTermRangeQueryFactory() {
		if (_termRangeQueryFactory == null) {
			_termRangeQueryFactory = new TermRangeQueryFactoryImpl();
		}

		return _termRangeQueryFactory;
	}

	@Override
	public String getVendor() {
		return _vendor;
	}

	@Override
	public void initialize(long companyId) {
	}

	/**
	 * @throws SearchException
	 */
	@Override
	public void removeBackup(long companyId, String backupName)
		throws SearchException {
	}

	@Override
	public void removeCompany(long companyId) {
	}

	/**
	 * @throws SearchException
	 */
	@Override
	public void restore(long companyId, String backupName)
		throws SearchException {
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	public void setBooleanClauseFactory(
		BooleanClauseFactory booleanClauseFactory) {

		_booleanClauseFactory = booleanClauseFactory;
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	public void setBooleanQueryFactory(
		BooleanQueryFactory booleanQueryFactory) {

		_booleanQueryFactory = booleanQueryFactory;
	}

	public void setIndexSearcher(IndexSearcher indexSearcher) {
		_indexSearcher = indexSearcher;
	}

	public void setIndexWriter(IndexWriter indexWriter) {
		_indexWriter = indexWriter;
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	public void setTermQueryFactory(TermQueryFactory termQueryFactory) {
		_termQueryFactory = termQueryFactory;
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	public void setTermRangeQueryFactory(
		TermRangeQueryFactory termRangeQueryFactory) {

		_termRangeQueryFactory = termRangeQueryFactory;
	}

	public void setVendor(String vendor) {
		_vendor = vendor;
	}

	private BooleanClauseFactory _booleanClauseFactory;

	@SuppressWarnings("deprecation")
	private BooleanQueryFactory _booleanQueryFactory;

	private IndexSearcher _indexSearcher = new DummyIndexSearcher();
	private IndexWriter _indexWriter = new DummyIndexWriter();

	@SuppressWarnings("deprecation")
	private TermQueryFactory _termQueryFactory;

	@SuppressWarnings("deprecation")
	private TermRangeQueryFactory _termRangeQueryFactory;

	private String _vendor;

}