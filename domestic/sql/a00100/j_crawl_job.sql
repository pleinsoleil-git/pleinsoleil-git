DROP TABLE j_crawl_job CASCADE;


CREATE TABLE j_crawl_job
(
	id								BIGSERIAL,
	job_type						VARCHAR( 512 ),
	job_name						VARCHAR( 1024 ),
	execution_date					DATE,
	execution_start_time			TIME,
	priority						NUMERIC,
	auto_run						BOOLEAN			DEFAULT FALSE,
	deleted							BOOLEAN			DEFAULT TRUE,
	created_at						TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP,
	updated_at						TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP,
	deleted_at						TIMESTAMP( 0 )		DEFAULT CURRENT_TIMESTAMP,
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


CREATE INDEX ON j_crawl_job
(
	execution_date
)
WITH
(
	FILLFACTOR = 100
);
