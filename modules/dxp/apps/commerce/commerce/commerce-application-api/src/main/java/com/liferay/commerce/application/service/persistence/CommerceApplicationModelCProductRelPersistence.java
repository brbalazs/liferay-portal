/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.application.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.application.exception.NoSuchApplicationModelCProductRelException;
import com.liferay.commerce.application.model.CommerceApplicationModelCProductRel;

import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * The persistence interface for the commerce application model c product rel service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Luca Pellizzon
 * @see com.liferay.commerce.application.service.persistence.impl.CommerceApplicationModelCProductRelPersistenceImpl
 * @see CommerceApplicationModelCProductRelUtil
 * @generated
 */
@ProviderType
public interface CommerceApplicationModelCProductRelPersistence
	extends BasePersistence<CommerceApplicationModelCProductRel> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link CommerceApplicationModelCProductRelUtil} to access the commerce application model c product rel persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Caches the commerce application model c product rel in the entity cache if it is enabled.
	*
	* @param commerceApplicationModelCProductRel the commerce application model c product rel
	*/
	public void cacheResult(
		CommerceApplicationModelCProductRel commerceApplicationModelCProductRel);

	/**
	* Caches the commerce application model c product rels in the entity cache if it is enabled.
	*
	* @param commerceApplicationModelCProductRels the commerce application model c product rels
	*/
	public void cacheResult(
		java.util.List<CommerceApplicationModelCProductRel> commerceApplicationModelCProductRels);

	/**
	* Creates a new commerce application model c product rel with the primary key. Does not add the commerce application model c product rel to the database.
	*
	* @param commerceApplicationModelCProductRelId the primary key for the new commerce application model c product rel
	* @return the new commerce application model c product rel
	*/
	public CommerceApplicationModelCProductRel create(
		long commerceApplicationModelCProductRelId);

	/**
	* Removes the commerce application model c product rel with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param commerceApplicationModelCProductRelId the primary key of the commerce application model c product rel
	* @return the commerce application model c product rel that was removed
	* @throws NoSuchApplicationModelCProductRelException if a commerce application model c product rel with the primary key could not be found
	*/
	public CommerceApplicationModelCProductRel remove(
		long commerceApplicationModelCProductRelId)
		throws NoSuchApplicationModelCProductRelException;

	public CommerceApplicationModelCProductRel updateImpl(
		CommerceApplicationModelCProductRel commerceApplicationModelCProductRel);

	/**
	* Returns the commerce application model c product rel with the primary key or throws a {@link NoSuchApplicationModelCProductRelException} if it could not be found.
	*
	* @param commerceApplicationModelCProductRelId the primary key of the commerce application model c product rel
	* @return the commerce application model c product rel
	* @throws NoSuchApplicationModelCProductRelException if a commerce application model c product rel with the primary key could not be found
	*/
	public CommerceApplicationModelCProductRel findByPrimaryKey(
		long commerceApplicationModelCProductRelId)
		throws NoSuchApplicationModelCProductRelException;

	/**
	* Returns the commerce application model c product rel with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param commerceApplicationModelCProductRelId the primary key of the commerce application model c product rel
	* @return the commerce application model c product rel, or <code>null</code> if a commerce application model c product rel with the primary key could not be found
	*/
	public CommerceApplicationModelCProductRel fetchByPrimaryKey(
		long commerceApplicationModelCProductRelId);

	@Override
	public java.util.Map<java.io.Serializable, CommerceApplicationModelCProductRel> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the commerce application model c product rels.
	*
	* @return the commerce application model c product rels
	*/
	public java.util.List<CommerceApplicationModelCProductRel> findAll();

	/**
	* Returns a range of all the commerce application model c product rels.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceApplicationModelCProductRelModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce application model c product rels
	* @param end the upper bound of the range of commerce application model c product rels (not inclusive)
	* @return the range of commerce application model c product rels
	*/
	public java.util.List<CommerceApplicationModelCProductRel> findAll(
		int start, int end);

	/**
	* Returns an ordered range of all the commerce application model c product rels.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceApplicationModelCProductRelModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce application model c product rels
	* @param end the upper bound of the range of commerce application model c product rels (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of commerce application model c product rels
	*/
	public java.util.List<CommerceApplicationModelCProductRel> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceApplicationModelCProductRel> orderByComparator);

	/**
	* Returns an ordered range of all the commerce application model c product rels.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceApplicationModelCProductRelModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce application model c product rels
	* @param end the upper bound of the range of commerce application model c product rels (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of commerce application model c product rels
	*/
	public java.util.List<CommerceApplicationModelCProductRel> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceApplicationModelCProductRel> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Removes all the commerce application model c product rels from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of commerce application model c product rels.
	*
	* @return the number of commerce application model c product rels
	*/
	public int countAll();

	@Override
	public java.util.Set<String> getBadColumnNames();
}