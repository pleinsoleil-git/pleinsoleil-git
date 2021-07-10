DROP TABLE j_crawl_process CASCADE;


CREATE TABLE j_crawl_process
(
	id								BIGSERIAL,
	foreign_id						BIGINT					REFERENCES j_crawl_request( id ) ON DELETE CASCADE,
	hotel_code						VARCHAR( 512 ),
	priority						NUMERIC,
	aborted							BOOLEAN					DEFAULT FALSE,
	deleted							BOOLEAN					DEFAULT TRUE,
	created_at						TIMESTAMP( 0 )			DEFAULT CURRENT_TIMESTAMP,
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


CREATE INDEX ON j_crawl_process
(
	foreign_id
)
WITH
(
	FILLFACTOR = 100
);


COMMENT ON TABLE j_crawl_process							IS 'クロール';
COMMENT ON COLUMN j_crawl_process.id						IS '主キー';
COMMENT ON COLUMN j_crawl_process.foreign_id				IS '外部キー';
COMMENT ON COLUMN j_crawl_process.hotel_code				IS '施設番号';
COMMENT ON COLUMN j_crawl_process.priority					IS '優先順位';
COMMENT ON COLUMN j_crawl_process.aborted					IS '中断';
COMMENT ON COLUMN j_crawl_process.deleted					IS '論理削除';
COMMENT ON COLUMN j_crawl_process.created_at				IS '作成日時';
COMMENT ON COLUMN j_crawl_process.updated_at				IS '更新日時';
COMMENT ON COLUMN j_crawl_process.deleted_at				IS '削除日時';

アパホテル〈三田駅前〉	138037	342084	00002283
