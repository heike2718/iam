// =====================================================
// Projekt: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================

package de.egladil.web.bv_admin.domain.validation;

/**
 * StringLatinConstants
 */
public interface StringLatinConstants {

	String A1 = "ÄäÀàÁáÂâÃãĀāÅåĂăĄąǍǎǞǟǺǻẠạẪẫẬẢảẤấẦầẮắ\u00AA"; // für Sortierung auf der Bildschirmtastatur

	// auseinandergenommen

	String A2 = "ẰằẲẳẴẵẶặÆæǼǽ";

	String B = "Ḃḃ";

	String C = "ÇçĆćĊċČč";

	String D = "ÐðĎďĐđḊḋḐḑD\u0302d\u0302";

	String E = "ÈèÉéÊêËëĒēĔĕĖėĘęĚěẼẽỄễẸẹẺẻỀềẾếỆệƏə";

	String F = "Ḟḟ";

	String G = "ĞğĠġĢģǤǥǦǧǴǵḠḡ";

	String H = "ĦħȞȟḤḥḦḧ";

	String I1 = "ÌìÍíÎîÏïĨĩĪī"; // für Sortierung auf der Bildschirmtastatur auseinandergenommen

	String I2 = "ĬĭĮįİıǏǐỊịỈỉ";

	String J = "ĴĵǰJ\u030c";

	String K = "ĶķǨǩḰḱĸ";

	String L = "ĹĺĻļĽľĿŀŁłL\u0302l\u0302";

	String M = "M\u0302m\u0302Ṁṁ";

	String N = "N\u0302n\u0302ÑñŃńŅņŇňŊŋṄṅŉ";

	String O1 = "ÖöÒòÓóÔôÕõŌōŎŏŐőƠơǑǒ"; // für Sortierung auf der Bildschirmtastatur auseinandergenommen

	String O2 = "ǪǫǬǭǾǿȪȫȮȯȰȱỌọỎỏỖỗºỒồỐốỘộỜờỚớØøŒœ";

	String P = "Ṗṗ";

	String R = "ŔŕŖŗŘř";

	String S = "ŚśŞşŠšȘșṠṡṢṣß\u1E9E";

	String T = "ŢţŤťŦŧȚțṪṫ";

	String TH = "Þþ";

	String U = "ÜüÙùÚúÛûŨũŪūŮůŰűŲųƯưǓǔỤụỦủỨứỪừỬửỮữỰự";

	String W = "ŴŵẀẁẂẃẄẅ";

	String X = "Ẍẍ";

	String Y1 = "ÝýŸÿŶŷȲȳ";

	String Y2 = "ẎẏỲỳỸỹỴỵỶỷƷʒǮǯ";

	String Z = "ŹźŻżŽžẐẑẒẓ";

	String DIACRITICS = A1 + A2 + B + C + D + E + F + G + H + I1 + I2 + J + K + L + M + N + O1 + O2 + P + R + S + T + TH + U + W + X
		+ Y1 + Y2 + Z;

	// in der UI werden einige diakritische Zeichen nicht richtig dargestellt, da sie aus 2 UI-Zeichen zusammengesetzt
	// sind.
	// da das vermutlich niemandem auffällt, werden für die Bildschirmtastatur hier einige zulässige Zeichen entfernt
	// Das sind genau die Zeichen, die auch in pkg_oasis_utils.transliterate Probleme machen.

	String UI_D = "ÐðĎďĐđḊḋḐḑ";

	String UI_J = "Ĵĵǰ";

	String UI_L = "ĹĺĻļĽľĿŀŁł";

	String UI_M = "Ṁṁ";

	String UI_N = "ÑñŃńŅņŇňŊŋṄṅŉ";

	String LATIN = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	String DIGITS = "0123456789";

	/** alle Sonderzeichen */
	String SPECIALS = "\" \\-_\\.,'`'\\@()/‘+:;=\\[\\]{}!#$§%&\\*\\?\\\\^|~°";

	/**
	 * Alle Buchstaben und diakritischen Zeichen aus StringLatin, alle Ziffern, Leerzeichen, Minus, Unterstrich, Punkt,
	 * Komma, Apostrophe
	 */
	String WHITELIST_REGEXP = "^[" + LATIN + DIACRITICS + DIGITS + SPECIALS + "]*$";

	/**
	 * Alle Buchstaben und diakritischen Zeichen aus StringLatin, alle Ziffern, Minus, Unterstrich, Punkt, Komma,
	 * Apostrophe und alle Arten von whitespace
	 */
	String WHITELIST_REGEXP_PLUS_WHITESPACE = "^[" + LATIN + DIACRITICS + DIGITS + SPECIALS + "\\s]*$";

}
