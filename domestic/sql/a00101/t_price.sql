DROP TABLE t_price CASCADE;


CREATE TABLE t_price
(
	id								BIGSERIAL,
	execution_date					DATE,
	hotel_code						VARCHAR,
	hotel_name						VARCHAR,
	plan_code						VARCHAR,
	plan_name						VARCHAR,
	plan_url						VARCHAR,
	room_code						VARCHAR,
	room_name						VARCHAR,
	room_info						VARCHAR,
	room_remark						VARCHAR,
	room_option_meal				VARCHAR,
	room_option_people				VARCHAR,
	room_option_payment				VARCHAR,
	point_rate						NUMERIC,
	price							NUMERIC,
	original_price					NUMERIC,
	discounted_price				NUMERIC,
	per_person_price				NUMERIC,
	created_at						TIMESTAMP( 0 )			DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY
	(
		id
	)
	WITH
	(
		FILLFACTOR = 100
	)
)
WITH
(
	FILLFACTOR = 100
);
