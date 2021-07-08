DROP TABLE j_crawl_job_status CASCADE;


CREATE TABLE j_crawl_job_status
(
	id								BIGSERIAL,
	foreign_id						BIGINT				REFERENCES j_crawl_job( id ) ON DELETE CASCADE,
	status							NUMERIC,
	error_code						VARCHAR( 512 ),
	error_message					VARCHAR,
	created_at						TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY
	(
		id
	)
	WITH
	(
		FILLFACTOR = 100
	),
	UNIQUE
	(
		foreign_id
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
