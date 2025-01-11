// =====================================================
// Projekt: profil-api
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.auth_validations;

/**
 * InputSecuredConstants
 */
public interface InputSecuredConstants {

	String A = "ÄäÀàÁáÂâÃãĀāÅåĂăĄąǍǎǞǟǺǻẠạẪẫẬẢảẤấẦầẮắ\u00AAẰằẲẳẴẵẶặÆæǼǽ";

	String B = "Ḃḃ";

	String C = "ÇçĆćĊċČč";

	String D = "dÐðĎďĐđḊḋḐḑD\u0302d\u0302";

	String E = "ÈèÉéÊêËëĒēĔĕĖėĘęĚěẼẽỄễẸẹẺẻỀềẾếỆệƏə";

	String F = "fḞḟ";

	String G = "ĞğĠġĢģǤǥǦǧǴǵḠḡ";

	String H = "ĦħȞȟḤḥḦḧ";

	String I = "ÌìÍíÎîÏïĨĩĪīĬĭĮįİıǏǐỊịỈỉ";

	String J = "ĴĵǰJ\u030c";

	String K = "ĶķǨǩḰḱĸ";

	String L = "ĹĺĻļĽľĿŀŁłL\u0302l\u0302";

	String M = "M\u0302m\u0302Ṁṁ";

	String N = "N\u0302n\u0302ÑñŃńŅņŇňŊŋṄṅŉ";

	String O = "ÖöÒòÓóÔôÕõŌōŎŏŐőƠơǑǒǪǫǬǭǾǿȪȫȮȯȰȱỌọỎỏỖỗºỒồỐốỘộỜờỚớØøŒœ";

	String P = "PpṖṗ";

	String R = "ŖŗŘř";

	String S = "ŚśŞşŠšȘșṠṡṢṣß\u1E9E";

	String T = "ŢţŤťŦŧȚțṪṫ";

	String TH = "Þþ";

	String U = "ÜüÙùÚúÛûŨũŪūŮůŰűŲųƯưǓǔỤụỦủỨứỪừỬửỮữỰự";

	String W = "ŴŵẀẁẂẃẄẅ";

	String X = "Ẍẍ";

	String Y1 = "ÝýŸÿŶŷȲȳ";

	String Y2 = "ẎẏỲỳỸỹỴỵỶỷƷʒǮǯ";

	String Z = "ŹźŻżŽžẐẑẒẓ";

	String DIACRITICS = A + B + C + D + E + F + G + H + I + J + K + L + M + N + O + P + R + S + T + TH + U + W + X + Y1 + Y2 + Z;

	String LATIN = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	String DIGITS = "0123456789";

	String SPECIAL_CHARS_FOR_MESSAGE = "!#$%&()*+,-./:;=?@^_``{|}[]~°";

	/** nur unkritische Sonderzeichen */
	/** !#$%&)(*+,-./:;=?@\]\[\^ _`'{|}~° */
	String SPECIALS_REGEXP = "!#$%&)(*+,-./:;=?@\\]\\[\\^ _`'{|}~°";

	/**
	 * Alle Buchstaben und diakritischen Zeichen aus StringLatin, alle Ziffern, Leerzeichen, Minus, Unterstrich, Punkt,
	 * Komma, Apostrophe, Leerzeichen, aber keine Zeilenumbrüche! Keine aufeinanderfolgenden Punkte, um ../
	 * auszuschließen?
	 */
	String INPUT_SECURED_WHITELIST = "^(?!.*\\.\\.)[" + LATIN + DIACRITICS + DIGITS + SPECIALS_REGEXP + " " + "]*$";

}
