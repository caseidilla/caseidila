INSERT INTO USER (login, password, pin)
values ('loupa', '111', '11');
INSERT INTO USER (login, password)
values ('poupa', '111');
INSERT INTO USER (login, password)
values ('behindloupa', '111');
INSERT INTO DIALOG (id, name, secret, hidden)
values (-1, 'dialog', false, false);
INSERT INTO USER_DIALOG(login, id)
values ('loupa', -1);
INSERT INTO USER_DIALOG(login, id)
values ('poupa', -1);
INSERT INTO DIALOG (id, name, secret, hidden)
values (-2, null, false, true);
INSERT INTO USER_DIALOG(login, id)
values ('loupa', -2);
INSERT INTO USER_DIALOG(login, id)
values ('behindloupa', -2);
INSERT INTO MESSAGE(sender, timestamp, receiver, body)
values ('loupa', parsedatetime('17-09-2012 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'), 'poupa', 'hi');
INSERT INTO MESSAGE(sender, timestamp, receiver, body)
values ('poupa', parsedatetime('17-09-2013 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'), 'loupa', 'hello');
insert into secret(code, user, timestamp)
values('a', 'loupa', parsedatetime('17-09-2030 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'))

