create index IX_FCABA3C8 on CommercePricingClass (companyId, externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_8A3D0197 on CommercePricingClass (groupId);
create index IX_287E2FA7 on CommercePricingClass (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_34C73E9 on CommercePricingClass (uuid_[$COLUMN_LENGTH:75$], groupId);

create index IX_8098CF07 on CommercePricingClassRel (classNameId, classPK);
create index IX_4ADCD8A0 on CommercePricingClassRel (commercePricingClassId, classNameId);