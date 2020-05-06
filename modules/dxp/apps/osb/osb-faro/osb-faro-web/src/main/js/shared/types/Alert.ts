export namespace Alert {
	export enum Types {
		ALERT = 'ALERT',
		DEFAULT = 'DEFAULT',
		ERROR = 'ERROR',
		PENDING = 'PENDING',
		SUCCESS = 'SUCCESS',
		WARNING = 'WARNING'
	}

	export type AddAlert = ({
		alertType,
		message,
		timeout
	}: {
		alertType: Types;
		message: string;
		timeout?: boolean;
	}) => Promise<any>;

	export type RemoveAlert = () => (action: {
		payload: {
			id: string;
		};
		type: 'REMOVE_ALERT';
	}) => void;
}
