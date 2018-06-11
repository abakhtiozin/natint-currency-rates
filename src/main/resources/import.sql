INSERT INTO url (id, url) VALUES (1, 'http://www.boi.org.il/currency.xml?date=');
INSERT INTO url (id, url) VALUES (2, 'https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json&date=');

INSERT INTO currency_code (id, `code`, number) VALUES (1, 'USD', 840);
INSERT INTO currency_code (id, `code`, number) VALUES (2, 'EUR', 978);
INSERT INTO currency_code (id, `code`, number) VALUES (3, 'GBP', 826);
INSERT INTO currency_code (id, `code`, number) VALUES (4, 'UAH', 980);
INSERT INTO currency_code (id, `code`, number) VALUES (5, 'ILS', 376);

INSERT INTO provider (name, currency_code_id, url_id) VALUES ('boi', 5, 1);
INSERT INTO provider (name, currency_code_id, url_id) VALUES ('nbu', 4, 2);
