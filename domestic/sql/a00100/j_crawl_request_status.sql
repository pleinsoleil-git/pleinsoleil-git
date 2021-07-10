DROP TABLE j_crawl_request_status CASCADE;


CREATE TABLE j_crawl_request_status
(
	id								BIGSERIAL,
	foreign_id						BIGINT				REFERENCES j_crawl_request( id ) ON DELETE CASCADE,
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
	)
)
WITH
(
	FILLFACTOR = 100
);


CREATE INDEX ON j_crawl_request_status
(
	foreign_id
)
WITH
(
	FILLFACTOR = 100
);


COMMENT ON TABLE j_crawl_request_status						IS 'クロール';
COMMENT ON COLUMN j_crawl_request_status.id					IS '主キー';
COMMENT ON COLUMN j_crawl_request_status.foreign_id			IS '外部キー';
COMMENT ON COLUMN j_crawl_request_status.status				IS 'ステータス';
COMMENT ON COLUMN j_crawl_request_status.error_code			IS 'エラーコード';
COMMENT ON COLUMN j_crawl_request_status.error_message		IS 'メッセージ';
COMMENT ON COLUMN j_crawl_request_status.created_at			IS '作成日時';
