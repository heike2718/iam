drop table if exists authbv.USERREFRESHTOKENS;

drop table if exists authbv.CLIENTACCESSTOKENS;

alter table authbv.CLIENTS drop column REFRESHTOKEN_EXPIRATION_HOURS;

alter table authbv.CLIENTS drop column ALLOWED_ORIGINS;
