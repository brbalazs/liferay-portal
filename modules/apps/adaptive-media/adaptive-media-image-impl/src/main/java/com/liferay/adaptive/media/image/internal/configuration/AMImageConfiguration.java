/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.adaptive.media.image.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Sergio González
 */
@ExtendedObjectClassDefinition(category = "adaptive-media")
@Meta.OCD(
	id = "com.liferay.adaptive.media.image.internal.configuration.AMImageConfiguration",
	localization = "content/Language",
	name = "adaptive-media-image-configuration-name"
)
public interface AMImageConfiguration {

	/**
	 * Sets the supported mime types that generate adaptive media images.
	 */
	@Meta.AD(
		deflt = "image/bmp|image/gif|image/jpeg|image/pjpeg|image/png|image/tiff|image/x-citrix-jpeg|image/x-citrix-png|image/x-ms-bmp|image/x-png|image/x-tiff",
		description = "supported-mime-types-key-description",
		name = "supported-mime-type", required = false
	)
	public String[] supportedMimeTypes();

	/**
	 * Set this to <code>true</code> to enable animated gif image scaling with
	 * gifsicle library. See https://www.lcdf.org/gifsicle for more information.
	 */
	@Meta.AD(
		deflt = "false", description = "gifsicle-enabled-key-description",
		name = "gifsicle-enabled", required = false
	)
	public boolean gifsicleEnabled();

	/**
	 * Set the maximum image size for adaptive media generation. Images larger
	 * than this value will not generate adaptive media images. A value of -1
	 * indicates that all images will generate adaptive media images. A value of
	 * 0 indicates that no adaptive media images will be generated.
	 */
	@Meta.AD(
		deflt = "10485760", description = "max-image-size-key-description",
		name = "max-image-size", required = false
	)
	public int imageMaxSize();

}