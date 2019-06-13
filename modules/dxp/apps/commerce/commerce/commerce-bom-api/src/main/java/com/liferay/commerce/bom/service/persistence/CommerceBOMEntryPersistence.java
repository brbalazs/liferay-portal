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

import com.liferay.commerce.bom.exception.NoSuchBOMEntryException;
import com.liferay.commerce.bom.model.CommerceBOMEntry;

import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * The persistence interface for the commerce bom entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Luca Pellizzon
 * @see com.liferay.commerce.bom.service.persistence.impl.CommerceBOMEntryPersistenceImpl
 * @see CommerceBOMEntryUtil
 * @generated
 */
@ProviderType
public interface CommerceBOMEntryPersistence extends BasePersistence<CommerceBOMEntry> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link CommerceBOMEntryUtil} to access the commerce bom entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Caches the commerce bom entry in the entity cache if it is enabled.
	*
	* @param commerceBOMEntry the commerce bom entry
	*/
	public void cacheResult(CommerceBOMEntry commerceBOMEntry);

	/**
	* Caches the commerce bom entries in the entity cache if it is enabled.
	*
	* @param commerceBOMEntries the commerce bom entries
	*/
	public void cacheResult(java.util.List<CommerceBOMEntry> commerceBOMEntries);

	/**
	* Creates a new commerce bom entry with the primary key. Does not add the commerce bom entry to the database.
	*
	* @param commerceBOMEntryId the primary key for the new commerce bom entry
	* @return the new commerce bom entry
	*/
	public CommerceBOMEntry create(long commerceBOMEntryId);

	/**
	* Removes the commerce bom entry with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param commerceBOMEntryId the primary key of the commerce bom entry
	* @return the commerce bom entry that was removed
	* @throws NoSuchBOMEntryException if a commerce bom entry with the primary key could not be found
	*/
	public CommerceBOMEntry remove(long commerceBOMEntryId)
		throws NoSuchBOMEntryException;

	public CommerceBOMEntry updateImpl(CommerceBOMEntry commerceBOMEntry);

	/**
	* Returns the commerce bom entry with the primary key or throws a {@link NoSuchBOMEntryException} if it could not be found.
	*
	* @param commerceBOMEntryId the primary key of the commerce bom entry
	* @return the commerce bom entry
	* @throws NoSuchBOMEntryException if a commerce bom entry with the primary key could not be found
	*/
	public CommerceBOMEntry findByPrimaryKey(long commerceBOMEntryId)
		throws NoSuchBOMEntryException;

	/**
	* Returns the commerce bom entry with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param commerceBOMEntryId the primary key of the commerce bom entry
	* @return the commerce bom entry, or <code>null</code> if a commerce bom entry with the primary key could not be found
	*/
	public CommerceBOMEntry fetchByPrimaryKey(long commerceBOMEntryId);

	@Override
	public java.util.Map<java.io.Serializable, CommerceBOMEntry> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the commerce bom entries.
	*
	* @return the commerce bom entries
	*/
	public java.util.List<CommerceBOMEntry> findAll();

	/**
	* Returns a range of all the commerce bom entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce bom entries
	* @param end the upper bound of the range of commerce bom entries (not inclusive)
	* @return the range of commerce bom entries
	*/
	public java.util.List<CommerceBOMEntry> findAll(int start, int end);

	/**
	* Returns an ordered range of all the commerce bom entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce bom entries
	* @param end the upper bound of the range of commerce bom entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of commerce bom entries
	*/
	public java.util.List<CommerceBOMEntry> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMEntry> orderByComparator);

	/**
	* Returns an ordered range of all the commerce bom entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce bom entries
	* @param end the upper bound of the range of commerce bom entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of commerce bom entries
	*/
	public java.util.List<CommerceBOMEntry> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMEntry> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Removes all the commerce bom entries from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of commerce bom entries.
	*
	* @return the number of commerce bom entries
	*/
	public int countAll();

	@Override
	public java.util.Set<String> getBadColumnNames();
}