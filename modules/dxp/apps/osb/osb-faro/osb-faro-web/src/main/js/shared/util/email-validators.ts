import * as emailValidator from 'isemail';

const VALIDATE_DOMAINS = /^([a-zA-Z0-9_]([a-zA-Z0-9_-]{0,61}[a-zA-Z0-9_])?\.){1,126}[a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z]$/;

export const validateEmailDomain = (emailDomain: string): boolean =>
	VALIDATE_DOMAINS.test(emailDomain);

export const validateEmailDomainArr = (
	items: string[],
	inputListValue: string
): string | void => {
	const emailDomains = items.concat(inputListValue || []);

	if (emailDomains.some(emailDomain => !validateEmailDomain(emailDomain))) {
		return Liferay.Language.get(
			'please-enter-the-domain-in-this-format-domain-com'
		);
	}
};

export const validateEmail = (email: string): boolean =>
	emailValidator.validate(email, {minDomainAtoms: 2});

export const validateEmailArr = (
	items: string[],
	inputListValue: string
): string | void => {
	const emails = items.concat(inputListValue || []);

	if (emails.some(email => !validateEmail(email))) {
		return Liferay.Language.get(
			'please-enter-the-email-in-this-format-sample-email-com'
		);
	}
};
