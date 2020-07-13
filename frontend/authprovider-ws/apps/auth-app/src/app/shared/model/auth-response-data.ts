export interface User {
	uid: string;
	email: string;
}

export interface SignUpLogInResponseData {
	readonly state: string;
	readonly nonce?: string;
	readonly idToken: string;
	readonly oauthFlowType: string;
}


export function createHash(data: SignUpLogInResponseData): string {

	const nonce = data.nonce ? data.nonce : '';

	return '#state=' + data.state
		+ '&nonce=' + nonce
		+ '&idToken=' + data.idToken
		+ '&oauthFlowType=' + data.oauthFlowType;
}

