// tslint:disable-next-line:max-line-length

/** Konstanten für Pattern-Validierung im Frontend. */

const DIACRITICAL_CHARS = "ÄäÀàÁáÂâÃãĀāÅåĂăĄąǍǎǞǟǺǻẠạẪẫẬẢảẤấẦầẮắªẰằẲẳẴẵẶặÆæǼǽḂḃÇçĆćĊċČčdÐðĎďĐđḊḋḐḑD̂d̂ÈèÉéÊêËëĒēĔĕĖėĘęĚěẼẽỄễẸẹẺẻỀềẾếỆệƏəfḞḟĞğĠġĢģǤǥǦǧǴǵḠḡĦħȞȟḤḥḦḧÌìÍíÎîÏïĨĩĪīĬĭĮįİıǏǐỊịỈỉĴĵǰJ̌ĶķǨǩḰḱĸĹĺĻļĽľĿŀŁłL̂l̂M̂m̂ṀṁN̂n̂ÑñŃńŅņŇňŊŋṄṅŉÖöÒòÓóÔôÕõŌōŎŏŐőƠơǑǒǪǫǬǭǾǿȪȫȮȯȰȱỌọỎỏỖỗºỒồỐốỘộỜờỚớØøŒœPpṖṗŖŗŘřŚśŞşŠšȘșṠṡṢṣßẞŢţŤťŦŧȚțṪṫÞþÜüÙùÚúÛûŨũŪūŮůŰűŲųƯưǓǔỤụỦủỨứỪừỬửỮữỰựŴŵẀẁẂẃẄẅẌẍÝýŸÿŶŷȲȳẎẏỲỳỸỹỴỵỶỷƷʒǮǯŹźŻżŽžẐẑẒẓ";

const LETTERS_IN_RE = "a-zA-Z" + DIACRITICAL_CHARS;

export const REG_EXP_PASSWORD = /^[\da-zA-ZäÄöÖüÜß!#$%&()*+,\-./:;=?@\[\]^_`'{|}~]*$/;

export const REG_EXP_PASSWORT_NEU = /^(?!\s)(?=.*\d)(?=.*[a-zA-ZäÄöÖüÜß])[a-zA-ZäÄöÖüÜß\d !#$%&()*+,\-./:;=?@\[\]^_`'{|}~]{8,100}(?<!\s)$/;

export const REG_EXP_EMAIL = /^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;

export const REG_EXP_INPUT_SECURED = "^(?!.*\\.\\.)[!" + LETTERS_IN_RE + "0123456789 !#$%&\\)\\(*+,-./:;=?@\\[\\]^ _`'\\{\\|\\}~°]*$";

export const REG_EXP_NACHNAME = REG_EXP_INPUT_SECURED;

export const REG_EXP_LOGIN_NAME = /^(?!.*\\.\\.)[a-zA-Z0-9.!#$%&'*+/=?\\^_`{|}@~ -]*$/;

export const SONDERZEICHEN_LOGIN = '!"#$%&)(*+,-./:;<=>?@\]\[^ _`\\\'{|}~';

export const SONDERZEICHEN_NEU = "!#$%&\\)\\(*+,-./:;=?@\\[\\]^ _`'\\{\\|\\}~";

export const BUCHSTABEN = 'a-z, A-Z, ÄÖÜäöüß';

export const PASSWORT_LOGIN_ERLAUBTE_ZEICHEN = BUCHSTABEN + ', 0-9, und die Sonderzeichen ' + SONDERZEICHEN_LOGIN + '. Leerzeichen am Anfang und am Ende werden abgeschnitten und können dazu führen, dass das übertragene Passwort falsch ist.';

export const PASSWORT_NEU_ERLAUBTE_ZEICHEN = BUCHSTABEN + ', 0-9, Leerzeichen und Sonderzeichen außer > " < und \\. Leerzeichen am Anfang und am Ende sind nicht erlaubt.';

export const PASSWORTREGELN = 'Ihr Passwort muss mindestens einen Buchstaben und eine Ziffer enthalten.'
    + ' Die Sonderzeichen > " < und \\ sind nicht erlaubt. Erlaubte Buchstaben sind ' + BUCHSTABEN + '. Leerzeichen am Anfang und am Ende sind nicht erlaubt. Mindestlänge 8 Zeichen.';

export const MESSAGE_BENUTZERDATEN_SUCCESS_WITH_LOGOUT = 'Ihre Benutzerdaten wurden erfolgreich geändert. Aus Sicherheitsgründen müssen sich erneut einloggen, wenn Sie auch Ihr Passwort ändern wollen.';

export const MESSAGE_BENUTZERDATEN_SUCCESS = 'Ihre Benutzerdaten wurden erfolgreich geändert.';

export const MESSAGE_PASSWORT_SUCCESS = 'Ihr Passwort wurde erfolgreich geändert. Aus Sicherheitsgründen müssen sich erneut einloggen, wenn Sie auch Ihre Mailadresse oder ihren Login-Namen ändern wollen.';

export const LOGINNAME_REGELN = "Der Loginname darf nur die Buchstaben A-Z und a-z, die Ziffern 0-9, Leerzeichen und die Sonderzeichen @ ! # $ % & ' * + - / = ? ^ _ . ` { | } ~ enthalten."

export const NAME_REGELN = "Erlaubt sind Buchstaben basierend auf dem lateinischen Alphabet einschließlich Akzente, Ziffern, Leezeichen und die Sonderzeichen !#$%&()*+,-./:;=?@^_``{|}[]~°."
