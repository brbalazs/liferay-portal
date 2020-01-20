create table CommercePricingClass (
	uuid_ VARCHAR(75) null,
	externalReferenceCode VARCHAR(75) null,
	commercePricingClassId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	groupId LONG,
	name VARCHAR(75) null,
	title VARCHAR(75) null,
	description VARCHAR(75) null,
	lastPublishDate DATE null
);

create table CommercePricingClassRel (
	commercePricingClassRelId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	commercePricingClassId LONG,
	classNameId LONG,
	classPK LONG
);