CREATE LOCAL TEMPORARY TABLE IF NOT EXISTS temp_price_rakuten
(
	id								BIGSERIAL,
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
	point_rate						VARCHAR,
	price							VARCHAR,
	original_price					VARCHAR,
	discounted_price				VARCHAR,
	per_person_price				VARCHAR
)
WITH
(
	FILLFACTOR = 100
);
