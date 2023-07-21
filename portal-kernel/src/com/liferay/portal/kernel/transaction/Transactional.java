/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.transaction;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Brian Wing Shun Chan
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Transactional {

	public boolean enabled() default true;

	public Isolation isolation() default Isolation.DEFAULT;

	public Class<? extends Throwable>[] noRollbackFor() default {};

	public String[] noRollbackForClassName() default {};

	public Propagation propagation() default Propagation.REQUIRED;

	public boolean readOnly() default false;

	public Class<? extends Throwable>[] rollbackFor() default {};

	public String[] rollbackForClassName() default {};

	public int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

}