DROP TABLE i_plan CASCADE;


CREATE UNLOGGED TABLE i_plan
(
	id					BIGSERIAL,
	hotel_code			VARCHAR,
	hotel_name			VARCHAR,
	plan_code			VARCHAR,
	plan_name			VARCHAR,
	room_code			VARCHAR,
	room_name			VARCHAR,
	created_at			TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP
)
WITH
(
	FILLFACTOR = 100,
	AUTOVACUUM_ENABLED = FALSE
);
