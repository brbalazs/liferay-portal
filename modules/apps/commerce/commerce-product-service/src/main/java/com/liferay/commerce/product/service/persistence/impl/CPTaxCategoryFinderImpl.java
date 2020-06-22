package com.liferay.commerce.product.service.persistence.impl;

import com.liferay.commerce.product.model.CPTaxCategory;
import com.liferay.commerce.product.model.impl.CPInstanceImpl;
import com.liferay.commerce.product.model.impl.CPTaxCategoryImpl;
import com.liferay.commerce.product.service.persistence.CPTaxCategoryFinder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.util.List;

public class CPTaxCategoryFinderImpl extends CPTaxCategoryFinderBaseImpl implements
	CPTaxCategoryFinder {

	public static final String FIND_CP_TAX_CATEGORIES_BY_COMPANY_ID =
		CPTaxCategoryFinder.class.getName() + ".findCPTaxCategoriesByCompanyId";

	@Override
	public List<CPTaxCategory> findCPTaxCategoriesByCompanyId(
		long companyId, String keyword, int start, int end) {

		Session session = null;

		try {
			session = openSession();

			String[] keywords = _customSQL.keywords(keyword, true);

			String sql = _customSQL.get(getClass(), FIND_CP_TAX_CATEGORIES_BY_COMPANY_ID);

			sql = StringUtil.replace(
				sql, new String[] {"[$COMPANY_ID$]"},
				new String[] {String.valueOf(companyId)});

			if (Validator.isNotNull(keyword)) {
				sql = _customSQL.replaceKeywords(
					sql, "LOWER(CPTaxCategory.name)", StringPool.LIKE, true,
					keywords);
				sql = _customSQL.replaceAndOperator(sql, false);
			}
			else {
				sql = StringUtil.replace(
					sql,
					" AND (LOWER(CPTaxCategory.name) LIKE ? " +
					"[$AND_OR_NULL_CHECK$])",
					StringPool.BLANK);
			}

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addEntity(CPTaxCategoryImpl.TABLE_NAME, CPTaxCategoryImpl.class);

			if (Validator.isNotNull(keyword)) {
				
				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(keywords, 2);
			}

			return (List<CPTaxCategory>) QueryUtil.list(q, getDialect(), start, end);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}


	@ServiceReference(type = CustomSQL.class)
	private CustomSQL _customSQL;
}
