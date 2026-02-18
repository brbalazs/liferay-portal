/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import React, {SetStateAction} from 'react';

export interface IRoom {
	actions: {
		[action: string]: {
			href: string;
			method: string | 'DELETE' | 'GET' | 'PATCH' | 'POST' | 'PUT';
		};
	};
	dateCreated: string;
	dateModified: string;
	embedded: IRoomObjectEntry;
	entryClassName: string;
	score: number;
}

export interface IRoomObjectEntry {
	actions: any;
	creator: {
		additionalName: string;
		contentType: string;
		externalReferenceCode: string;
		familyName: string;
		givenName: string;
		id: number;
		name: string;
	};
	dateCreated: string;
	dateModified: string;
	description: string;
	externalReferenceCode: string;
	id: number;
	name: string;
	r_accountToDSRRooms_accountEntry: {
		description: string;
		externalReferenceCode: string;
		id: number;
		logoId: number;
		logoURL: string;
		name: string;
	};
	r_accountToDSRRooms_accountEntryId: number;
	siteId: number;
	status: {
		code: number;
		label: string;
		label_i18n: string;
	};
}

export type TAccountDTO = {
	externalReferenceCode: string;
	id: number;
	name: string;
	status: number;
	type: string;
};

export type TAccountsDTO = {
	items: Array<TAccountDTO>;
	lastPage: number;
	page: number;
	pageSize: number;
	totalCount: number;
};

export type TRoomTemplateDTO = {
	banner?: {
		fileBase64?: string;
		fileURL: string;
		id: number;
	};
	clientLogo?: {
		fileBase64?: string;
		fileURL: string;
		id: number;
	};
	clientName: string;
	createDate: string;
	description?: string;
	externalReferenceCode: string;
	friendlyUrlPath: string;
	id: number;
	modifiedDate: string;
	name: string;
	ownerId: number;
	ownerName: string;
	primaryColor?: string;
	secondaryColor?: string;
	usages?: number;
};

export type TRoomTemplatesDTO = {
	items: Array<TRoomTemplateDTO>;
	lastPage: number;
	page: number;
	pageSize: number;
	totalCount: number;
};

export type TRoomTemplatePayload = {
	banner?: {
		fileBase64: string;
	};
	clientLogo?: {
		fileBase64: string;
	};
	clientName: string;
	description?: string;
	name: string;
	primaryColor?: string;
	secondaryColor?: string;
};

export type TUserAccountBrief = {
	alternateName?: string;
	emailAddress: string;
	externalReferenceCode?: string;
	id: number;
	image?: string;
	name: string;
	roleKey?: string;
};

export type TUserAccountBriefsDTO = {
	items: Array<TUserAccountBrief>;
	lastPage: number;
	page: number;
	pageSize: number;
	totalCount: number;
};

export type TRoomDataContext = {
	accountId?: number;
	accountName?: string;
	banner: {
		base64?: string;
		name?: string;
		size?: number;
	};
	channelId?: number;
	channelName?: string;
	clientLogo: {
		base64?: string;
	};
	clientName: string;
	description?: string;
	digitalSalesRoomId?: number;
	errors: {
		accountId?: null | string;
		banner?: null | string;
		channelId?: null | string;
		channelName?: null | string;
		clientLogo?: null | string;
		clientName?: null | string;
		description?: null | string;
		friendlyURL?: null | string;
		primaryColor?: null | string;
		roomName?: null | string;
		secondaryColor?: null | string;
		share?: null | string;
	};
	friendlyURL: string;
	primaryColor: string;
	roomName: string;
	secondaryColor: string;
	share?: {
		emailAddresses: Array<string>;
		roleKey?: string;
	};
	templateId?: number;
};

export type TRoomContext = {
	dataContext: TRoomDataContext;
	loading: boolean;
	setDataContext: React.Dispatch<React.SetStateAction<TRoomDataContext>>;
	setLoading?: React.Dispatch<React.SetStateAction<boolean>>;
};

export type TRoomStepProps = {
	setHandleStepSubmit(
		callback: SetStateAction<(event: Event) => Promise<boolean>>
	): void;
	numberOfSteps: number;
	showHeader?: boolean;
	step?: number;
};

export type TRoomInitializerProps = {
	closeModal: () => void;
	numberOfSteps?: number;
};
