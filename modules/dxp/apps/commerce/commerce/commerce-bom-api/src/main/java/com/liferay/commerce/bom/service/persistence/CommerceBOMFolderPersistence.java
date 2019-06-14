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

import com.liferay.commerce.bom.exception.NoSuchBOMFolderException;
import com.liferay.commerce.bom.model.CommerceBOMFolder;

import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * The persistence interface for the commerce bom folder service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Luca Pellizzon
 * @see com.liferay.commerce.bom.service.persistence.impl.CommerceBOMFolderPersistenceImpl
 * @see CommerceBOMFolderUtil
 * @generated
 */
@ProviderType
public interface CommerceBOMFolderPersistence extends BasePersistence<CommerceBOMFolder> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link CommerceBOMFolderUtil} to access the commerce bom folder persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the commerce bom folders where companyId = &#63;.
	*
	* @param companyId the company ID
	* @return the matching commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findByCompany(long companyId);

	/**
	* Returns a range of all the commerce bom folders where companyId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @return the range of matching commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findByCompany(long companyId,
		int start, int end);

	/**
	* Returns an ordered range of all the commerce bom folders where companyId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findByCompany(long companyId,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator);

	/**
	* Returns an ordered range of all the commerce bom folders where companyId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of matching commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findByCompany(long companyId,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Returns the first commerce bom folder in the ordered set where companyId = &#63;.
	*
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching commerce bom folder
	* @throws NoSuchBOMFolderException if a matching commerce bom folder could not be found
	*/
	public CommerceBOMFolder findByCompany_First(long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator)
		throws NoSuchBOMFolderException;

	/**
	* Returns the first commerce bom folder in the ordered set where companyId = &#63;.
	*
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching commerce bom folder, or <code>null</code> if a matching commerce bom folder could not be found
	*/
	public CommerceBOMFolder fetchByCompany_First(long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator);

	/**
	* Returns the last commerce bom folder in the ordered set where companyId = &#63;.
	*
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching commerce bom folder
	* @throws NoSuchBOMFolderException if a matching commerce bom folder could not be found
	*/
	public CommerceBOMFolder findByCompany_Last(long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator)
		throws NoSuchBOMFolderException;

	/**
	* Returns the last commerce bom folder in the ordered set where companyId = &#63;.
	*
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching commerce bom folder, or <code>null</code> if a matching commerce bom folder could not be found
	*/
	public CommerceBOMFolder fetchByCompany_Last(long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator);

	/**
	* Returns the commerce bom folders before and after the current commerce bom folder in the ordered set where companyId = &#63;.
	*
	* @param commerceBOMFolderId the primary key of the current commerce bom folder
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next commerce bom folder
	* @throws NoSuchBOMFolderException if a commerce bom folder with the primary key could not be found
	*/
	public CommerceBOMFolder[] findByCompany_PrevAndNext(
		long commerceBOMFolderId, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator)
		throws NoSuchBOMFolderException;

	/**
	* Returns all the commerce bom folders that the user has permission to view where companyId = &#63;.
	*
	* @param companyId the company ID
	* @return the matching commerce bom folders that the user has permission to view
	*/
	public java.util.List<CommerceBOMFolder> filterFindByCompany(long companyId);

	/**
	* Returns a range of all the commerce bom folders that the user has permission to view where companyId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @return the range of matching commerce bom folders that the user has permission to view
	*/
	public java.util.List<CommerceBOMFolder> filterFindByCompany(
		long companyId, int start, int end);

	/**
	* Returns an ordered range of all the commerce bom folders that the user has permissions to view where companyId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching commerce bom folders that the user has permission to view
	*/
	public java.util.List<CommerceBOMFolder> filterFindByCompany(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator);

	/**
	* Returns the commerce bom folders before and after the current commerce bom folder in the ordered set of commerce bom folders that the user has permission to view where companyId = &#63;.
	*
	* @param commerceBOMFolderId the primary key of the current commerce bom folder
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next commerce bom folder
	* @throws NoSuchBOMFolderException if a commerce bom folder with the primary key could not be found
	*/
	public CommerceBOMFolder[] filterFindByCompany_PrevAndNext(
		long commerceBOMFolderId, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator)
		throws NoSuchBOMFolderException;

	/**
	* Removes all the commerce bom folders where companyId = &#63; from the database.
	*
	* @param companyId the company ID
	*/
	public void removeByCompany(long companyId);

	/**
	* Returns the number of commerce bom folders where companyId = &#63;.
	*
	* @param companyId the company ID
	* @return the number of matching commerce bom folders
	*/
	public int countByCompany(long companyId);

	/**
	* Returns the number of commerce bom folders that the user has permission to view where companyId = &#63;.
	*
	* @param companyId the company ID
	* @return the number of matching commerce bom folders that the user has permission to view
	*/
	public int filterCountByCompany(long companyId);

	/**
	* Returns all the commerce bom folders where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @return the matching commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findByC_P(long companyId,
		long parentCommerceBOMFolderId);

	/**
	* Returns a range of all the commerce bom folders where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @return the range of matching commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findByC_P(long companyId,
		long parentCommerceBOMFolderId, int start, int end);

	/**
	* Returns an ordered range of all the commerce bom folders where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findByC_P(long companyId,
		long parentCommerceBOMFolderId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator);

	/**
	* Returns an ordered range of all the commerce bom folders where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of matching commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findByC_P(long companyId,
		long parentCommerceBOMFolderId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Returns the first commerce bom folder in the ordered set where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching commerce bom folder
	* @throws NoSuchBOMFolderException if a matching commerce bom folder could not be found
	*/
	public CommerceBOMFolder findByC_P_First(long companyId,
		long parentCommerceBOMFolderId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator)
		throws NoSuchBOMFolderException;

	/**
	* Returns the first commerce bom folder in the ordered set where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching commerce bom folder, or <code>null</code> if a matching commerce bom folder could not be found
	*/
	public CommerceBOMFolder fetchByC_P_First(long companyId,
		long parentCommerceBOMFolderId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator);

	/**
	* Returns the last commerce bom folder in the ordered set where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching commerce bom folder
	* @throws NoSuchBOMFolderException if a matching commerce bom folder could not be found
	*/
	public CommerceBOMFolder findByC_P_Last(long companyId,
		long parentCommerceBOMFolderId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator)
		throws NoSuchBOMFolderException;

	/**
	* Returns the last commerce bom folder in the ordered set where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching commerce bom folder, or <code>null</code> if a matching commerce bom folder could not be found
	*/
	public CommerceBOMFolder fetchByC_P_Last(long companyId,
		long parentCommerceBOMFolderId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator);

	/**
	* Returns the commerce bom folders before and after the current commerce bom folder in the ordered set where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* @param commerceBOMFolderId the primary key of the current commerce bom folder
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next commerce bom folder
	* @throws NoSuchBOMFolderException if a commerce bom folder with the primary key could not be found
	*/
	public CommerceBOMFolder[] findByC_P_PrevAndNext(long commerceBOMFolderId,
		long companyId, long parentCommerceBOMFolderId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator)
		throws NoSuchBOMFolderException;

	/**
	* Returns all the commerce bom folders that the user has permission to view where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @return the matching commerce bom folders that the user has permission to view
	*/
	public java.util.List<CommerceBOMFolder> filterFindByC_P(long companyId,
		long parentCommerceBOMFolderId);

	/**
	* Returns a range of all the commerce bom folders that the user has permission to view where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @return the range of matching commerce bom folders that the user has permission to view
	*/
	public java.util.List<CommerceBOMFolder> filterFindByC_P(long companyId,
		long parentCommerceBOMFolderId, int start, int end);

	/**
	* Returns an ordered range of all the commerce bom folders that the user has permissions to view where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching commerce bom folders that the user has permission to view
	*/
	public java.util.List<CommerceBOMFolder> filterFindByC_P(long companyId,
		long parentCommerceBOMFolderId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator);

	/**
	* Returns the commerce bom folders before and after the current commerce bom folder in the ordered set of commerce bom folders that the user has permission to view where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* @param commerceBOMFolderId the primary key of the current commerce bom folder
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next commerce bom folder
	* @throws NoSuchBOMFolderException if a commerce bom folder with the primary key could not be found
	*/
	public CommerceBOMFolder[] filterFindByC_P_PrevAndNext(
		long commerceBOMFolderId, long companyId,
		long parentCommerceBOMFolderId,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator)
		throws NoSuchBOMFolderException;

	/**
	* Removes all the commerce bom folders where companyId = &#63; and parentCommerceBOMFolderId = &#63; from the database.
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	*/
	public void removeByC_P(long companyId, long parentCommerceBOMFolderId);

	/**
	* Returns the number of commerce bom folders where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @return the number of matching commerce bom folders
	*/
	public int countByC_P(long companyId, long parentCommerceBOMFolderId);

	/**
	* Returns the number of commerce bom folders that the user has permission to view where companyId = &#63; and parentCommerceBOMFolderId = &#63;.
	*
	* @param companyId the company ID
	* @param parentCommerceBOMFolderId the parent commerce bom folder ID
	* @return the number of matching commerce bom folders that the user has permission to view
	*/
	public int filterCountByC_P(long companyId, long parentCommerceBOMFolderId);

	/**
	* Caches the commerce bom folder in the entity cache if it is enabled.
	*
	* @param commerceBOMFolder the commerce bom folder
	*/
	public void cacheResult(CommerceBOMFolder commerceBOMFolder);

	/**
	* Caches the commerce bom folders in the entity cache if it is enabled.
	*
	* @param commerceBOMFolders the commerce bom folders
	*/
	public void cacheResult(
		java.util.List<CommerceBOMFolder> commerceBOMFolders);

	/**
	* Creates a new commerce bom folder with the primary key. Does not add the commerce bom folder to the database.
	*
	* @param commerceBOMFolderId the primary key for the new commerce bom folder
	* @return the new commerce bom folder
	*/
	public CommerceBOMFolder create(long commerceBOMFolderId);

	/**
	* Removes the commerce bom folder with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param commerceBOMFolderId the primary key of the commerce bom folder
	* @return the commerce bom folder that was removed
	* @throws NoSuchBOMFolderException if a commerce bom folder with the primary key could not be found
	*/
	public CommerceBOMFolder remove(long commerceBOMFolderId)
		throws NoSuchBOMFolderException;

	public CommerceBOMFolder updateImpl(CommerceBOMFolder commerceBOMFolder);

	/**
	* Returns the commerce bom folder with the primary key or throws a {@link NoSuchBOMFolderException} if it could not be found.
	*
	* @param commerceBOMFolderId the primary key of the commerce bom folder
	* @return the commerce bom folder
	* @throws NoSuchBOMFolderException if a commerce bom folder with the primary key could not be found
	*/
	public CommerceBOMFolder findByPrimaryKey(long commerceBOMFolderId)
		throws NoSuchBOMFolderException;

	/**
	* Returns the commerce bom folder with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param commerceBOMFolderId the primary key of the commerce bom folder
	* @return the commerce bom folder, or <code>null</code> if a commerce bom folder with the primary key could not be found
	*/
	public CommerceBOMFolder fetchByPrimaryKey(long commerceBOMFolderId);

	@Override
	public java.util.Map<java.io.Serializable, CommerceBOMFolder> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the commerce bom folders.
	*
	* @return the commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findAll();

	/**
	* Returns a range of all the commerce bom folders.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @return the range of commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findAll(int start, int end);

	/**
	* Returns an ordered range of all the commerce bom folders.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator);

	/**
	* Returns an ordered range of all the commerce bom folders.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link CommerceBOMFolderModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of commerce bom folders
	* @param end the upper bound of the range of commerce bom folders (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of commerce bom folders
	*/
	public java.util.List<CommerceBOMFolder> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<CommerceBOMFolder> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Removes all the commerce bom folders from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of commerce bom folders.
	*
	* @return the number of commerce bom folders
	*/
	public int countAll();
}