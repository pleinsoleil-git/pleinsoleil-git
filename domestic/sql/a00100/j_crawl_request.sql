DROP TABLE j_crawl_request CASCADE;


CREATE TABLE j_crawl_request
(
	id								BIGSERIAL,
	foreign_id						BIGINT					REFERENCES j_crawl_job( id ) ON DELETE CASCADE,
	request_type					VARCHAR( 512 ),
	request_name					VARCHAR( 1024 ),
	user_id							VARCHAR( 512 ),
	password						VARCHAR( 512 ),
	priority						NUMERIC,
	aborted							BOOLEAN					DEFAULT FALSE,
	deleted							BOOLEAN					DEFAULT TRUE,
	created_at						TIMESTAMP( 0 )			DEFAULT CURRENT_TIMESTAMP,
	updated_at						TIMESTAMP,
	deleted_at						TIMESTAMP,
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


COMMENT ON TABLE j_crawl_request							IS 'クロール';
COMMENT ON COLUMN j_crawl_request.id						IS '主キー';
COMMENT ON COLUMN j_crawl_request.foreign_id				IS '外部キー';
COMMENT ON COLUMN j_crawl_request.request_type				IS 'リクエストタイプ';
COMMENT ON COLUMN j_crawl_request.request_name				IS 'リクエスト名';
COMMENT ON COLUMN j_crawl_request.user_id					IS 'ユーザID';
COMMENT ON COLUMN j_crawl_request.password					IS 'パスワード';
COMMENT ON COLUMN j_crawl_request.priority					IS '優先順位';
COMMENT ON COLUMN j_crawl_request.aborted					IS '中断';
COMMENT ON COLUMN j_crawl_request.deleted					IS '論理削除';
COMMENT ON COLUMN j_crawl_request.created_at				IS '作成日時';
COMMENT ON COLUMN j_crawl_request.updated_at				IS '更新日時';
COMMENT ON COLUMN j_crawl_request.deleted_at				IS '削除日時';
