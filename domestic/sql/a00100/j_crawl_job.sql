DROP TABLE j_crawl_job CASCADE;


CREATE TABLE j_crawl_job
(
	id								BIGSERIAL,
	job_type						VARCHAR( 512 ),
	job_name						VARCHAR( 1024 ),
	execution_date					DATE,
	execution_start_time			TIME,
	user_id							VARCHAR( 512 ),
	password						VARCHAR( 512 ),
	check_in_date					DATE,
	check_out_date					DATE,
	room_nums						NUMERIC,
	adult_nums						NUMERIC,
	upper_grade_nums				NUMERIC,
	lower_grade_nums				NUMERIC,
	priority						NUMERIC,
	auto_run						BOOLEAN				DEFAULT TRUE,
	aborted							BOOLEAN				DEFAULT FALSE,
	deleted							BOOLEAN				DEFAULT TRUE,
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


CREATE INDEX ON j_crawl_job
(
	execution_date
)
WITH
(
	FILLFACTOR = 100
);


COMMENT ON TABLE j_crawl_job						IS 'クロール';
COMMENT ON COLUMN j_crawl_job.id					IS '主キー';
COMMENT ON COLUMN j_crawl_job.job_type				IS 'ジョブタイプ';
COMMENT ON COLUMN j_crawl_job.job_name				IS 'ジョブ名';
COMMENT ON COLUMN j_crawl_job.execution_date		IS '実行日';
COMMENT ON COLUMN j_crawl_job.execution_start_time	IS '実行開始時間';
COMMENT ON COLUMN j_crawl_job.user_id				IS 'ユーザID';
COMMENT ON COLUMN j_crawl_job.password				IS 'パスワード';
COMMENT ON COLUMN j_crawl_job.check_in_date			IS 'チェックイン日';
COMMENT ON COLUMN j_crawl_job.check_out_date		IS 'チェックアウト日';
COMMENT ON COLUMN j_crawl_job.room_nums				IS '部屋数';
COMMENT ON COLUMN j_crawl_job.adult_nums			IS '大人人数';
COMMENT ON COLUMN j_crawl_job.upper_grade_nums		IS '高学年';
COMMENT ON COLUMN j_crawl_job.lower_grade_nums		IS '低学年';
COMMENT ON COLUMN j_crawl_job.priority				IS '優先順位';
COMMENT ON COLUMN j_crawl_job.auto_run				IS '自動実行';
COMMENT ON COLUMN j_crawl_job.aborted				IS '中断';
COMMENT ON COLUMN j_crawl_job.deleted				IS '論理削除';
COMMENT ON COLUMN j_crawl_job.created_at			IS '作成日時';
COMMENT ON COLUMN j_crawl_job.updated_at			IS '更新日時';
COMMENT ON COLUMN j_crawl_job.deleted_at			IS '削除日時';
