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

package com.liferay.commerce.bom.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.bom.exception.NoSuchBOMFolderApplicationRelException;
import com.liferay.commerce.bom.model.CommerceBOMFolderApplicationRel;

import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * The persistence interface for the commerce bom folder application rel service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Luca Pellizzon
 * @see com.liferay.commerce.bom.service.persistence.impl.CommerceBOMFolderApplicationRelPersistenceImpl
 * @see CommerceBOMFolderApplicationRelUtil
 * @generated
 */
@ProviderType
public interface CommerceBOMFolderApplicationRelPersistence
	extends BasePersistence<CommerceBOMFolderApplicationRel> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link CommerceBOMFolderApplicationRelUtil} to access the commerce bom folder application rel persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Caches the commerce bom folder application rel in the entity cache if it is enabled.
	*
	* @param commerceBOMFolderApplicationRel the commerce bom folder application rel
	*/
	public void cacheResult(
		CommerceBOMFolderApplicationRel commerceBOMFolderApplicationRel);

	/**
	* Caches the commerce bom folder application rels in the entity cache if it is enabled.
	*
	* @param commerceBOMFolderApplicationRels the commerce bom folder application rels
	*/
	public void cacheResult(
		java.util.List<CommerceBOMFolderApplicationRel> commerceBOMFolderApplicationRels);

	/**
	* Creates a new commerce bom folder application rel with the primary key. Does not add the commerce bom folder application rel to the database.
	*
	* @param commerceBOMFolderApplicationRelId the primary key for the new commerce bom folder application rel
	* @return the new commerce bom folder application rel
	*/
	public CommerceBOMFolderApplicationRel create(
		long commerceBOMFolderApplicationRelId);

	/**
	* Removes the commerce bom folder application rel with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param commerceBOMFolderApplicationRelId the primary key of the commerce bom folder application rel
	* @return the commerce bom folder application rel that was removed
	* @throws NoSuchBOMFolderApplicationRelException if a commerce bom folder application rel with the primary key could not be found
	*/
	public CommerceBOMFolderApplicationRel remove(
		long commerceBOMFolderApplicationRelId)
		throws NoSuchBOMFolderApplicationRelException;

	public CommerceBOMFolderApplicationRel updateImpl(
		CommerceBOMFolderApplicationRel commerceBOMFolderApplicationRel);

	/**
	* Returns the commerce bom folder application rel with the primary key or throws a {@link NoSuchBOMFolderApplicationRelException} if it could not be found.
	*
	* @param commerceBOMFolderApplicationRelId the primary key of the commerce bom folder application rel
	* @return the commerce bom folder application rel
	* @throws NoSuchBOMFolderApplicationRelException if a commerce bom folder application rel with the primary key could not be found
	*/
	public CommerceBOMFolderApplicationRel findByPrimaryKey(
		long commerceBOMFolderApplicationRelId)
		throws NoSuchBOMFolderApplicationRelException;

	/**
	* Returns the commerce bom folder application rel with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param commerceBOMFolderApplicationRelId the primary key of the commerce bom folder application rel
	* @return the commerce bom folder application rel, or <code>null</code> if a commerce bom folder application rel with the primary key could not be found
	*/
	public CommerceBOMFolderApplicationRel fetchByPrimaryKey(
		long commerceBOMFolderApplicationRelId);

	@Override
	public java.util.Map<java.io.Serializable, CommerceBOMFolderApplicationRel> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the commerce bom folder application rels.
	*
	* @return the commerce bom folder application rels
	*/
	public java.util.List<CommerceBOMFolderApplicationRel> findAll();

	/**
	* Returns a range of all the commerce bom folder application rels.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderApplicationRelModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce bom folder application rels
	* @param end the upper bound of the range of commerce bom folder application rels (not inclusive)
	* @return the range of commerce bom folder application rels
	*/
	public java.util.List<CommerceBOMFolderApplicationRel> findAll(int start,
		int end);

	/**
	* Returns an ordered range of all the commerce bom folder application rels.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderApplicationRelModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce bom folder application rels
	* @param end the upper bound of the range of commerce bom folder application rels (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of commerce bom folder application rels
	*/
	public java.util.List<CommerceBOMFolderApplicationRel> findAll(int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolderApplicationRel> orderByComparator);

	/**
	* Returns an ordered range of all the commerce bom folder application rels.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderApplicationRelModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce bom folder application rels
	* @param end the upper bound of the range of commerce bom folder application rels (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of commerce bom folder application rels
	*/
	public java.util.List<CommerceBOMFolderApplicationRel> findAll(int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolderApplicationRel> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Removes all the commerce bom folder application rels from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of commerce bom folder application rels.
	*
	* @return the number of commerce bom folder application rels
	*/
	public int countAll();

	@Override
	public java.util.Set<String> getBadColumnNames();
}