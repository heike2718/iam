
export class AppConstants {

	public static tooltips = {
		// tslint:disable-next-line:max-line-length
		PASSWORTREGELN: 'mindestens 8 Zeichen, höchstens 100 Zeichen, mindestens ein Buchstabe, mindestens eine Ziffer, keine Leerzeichen am Anfang und am Ende, '
			+ 'erlaubte Sonderzeichen sind !"#$%&)(*+,-./:;<=>?@][^ _`\'{|}~',
		MAILADRESSE_REGISTRIERT: 'Tragen Sie hier bitte die Mailadresse ein, mit der Sie sich registriert haben.',
		MAILADRESSE_REGISTRIERUNG: 'Mit dieser Mailadresse werden Sie sich später einloggen.',
		TEMPPWD: 'Tragen Sie hier bitte das Einmalpasswort ein, das Ihnen per Mail gesendet wurde.',
	};

	public static colors = {
		DARK_GRAY: '#555753',
		WHITE: '#ffffff'
	};

	public static backgroundColors = {
		INPUT_INVALID: '#fffae6',
		INPUT_VALID: '#eefbe9',
		INPUT_NEUTRAL: '#ffffff',
		INPUT_MISSING: '#d3d7cf'
	};
}
