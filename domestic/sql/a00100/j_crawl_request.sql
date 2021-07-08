DROP TABLE j_crawl_request CASCADE;


CREATE TABLE j_crawl_request
(
	id								BIGSERIAL,
	foreign_id						BIGINT				REFERENCES j_crawl_job( id ) ON DELETE CASCADE,
	request_type					VARCHAR( 512 ),
	request_name					VARCHAR( 1024 ),
	priority						NUMERIC,
	aborted							BOOLEAN			DEFAULT FALSE,
	deleted							BOOLEAN			DEFAULT TRUE,
	created_at						TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP,
	updated_at						TIMESTAMP( 0 ),
	deleted_at						TIMESTAMP( 0 ),
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


CREATE INDEX ON j_crawl_request
(
	foreign_id
)
WITH
(
	FILLFACTOR = 100
);
