DROP TABLE j_crawl_process_result CASCADE;


CREATE TABLE j_crawl_process_result
(
	id								BIGSERIAL,
	foreign_id						BIGINT				REFERENCES j_crawl_process( id ) ON DELETE CASCADE,
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


CREATE INDEX ON j_crawl_process_result
(
	foreign_id
)
WITH
(
	FILLFACTOR = 100
);


COMMENT ON TABLE j_crawl_process_result						IS 'クロール';
COMMENT ON COLUMN j_crawl_process_result.id					IS '主キー';
COMMENT ON COLUMN j_crawl_process_result.foreign_id			IS '外部キー';
COMMENT ON COLUMN j_crawl_process_result.status				IS 'ステータス';
COMMENT ON COLUMN j_crawl_process_result.error_code			IS 'エラーコード';
COMMENT ON COLUMN j_crawl_process_result.error_message		IS 'メッセージ';
COMMENT ON COLUMN j_crawl_process_result.created_at			IS '作成日時';
