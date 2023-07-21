/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.scope;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be used to declare that a method on a JAX-RS resource
 * does not need any scope to be authorized.<br /> <br /> When used on JAX-RS
 * resource class, all methods without the annotation will inherit the resource
 * class annotation.
 *
 * <p>
 * If scope annotation checking is enabled and a method has no {@link
 * RequiresScope} or {@link RequiresNoScope} annotation, request won't be
 * authorized to execute that method as security precaution.
 * </p>
 *
 * @author Tomas Polesovsky
 * @see    RequiresScope
 * @review
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresNoScope {
}