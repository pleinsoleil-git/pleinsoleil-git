DROP TABLE t_price_rakuten CASCADE;


CREATE TABLE t_price_rakuten
(
	id								BIGSERIAL,
	hotel_code						VARCHAR( 512 ),
	hotel_name						VARCHAR( 1024 ),
	plan_code						VARCHAR( 512 ),
	plan_name						VARCHAR( 1024 ),
	room_code						VARCHAR( 512 ),
	room_name						VARCHAR( 1024 ),
	room_info						VARCHAR,
	room_remark						VARCHAR,
	room_option_meal				VARCHAR( 1024 ),
	room_option_people				VARCHAR( 1024 ),
	room_option_payment				VARCHAR( 1024 ),
	original_price					NUMERIC,
	discounted_price				NUMERIC,
	created_at						TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP,
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
