SELECT
    t.relname AS table_name,
    i.relname AS index_name,
    a.attname AS column_name,
    ix.indisunique AS is_unique,
    ix.indisprimary AS is_primary,
    a.attnum AS column_position,
    am.amname AS index_type
FROM
    pg_class t
    JOIN pg_index ix ON t.oid = ix.indrelid
    JOIN pg_class i ON i.oid = ix.indexrelid
    JOIN pg_attribute a ON a.attrelid = t.oid AND a.attnum = ANY(ix.indkey)
    JOIN pg_am am ON i.relam = am.oid
WHERE
    t.relname = 'category'
ORDER BY
    t.relname,
    i.relname,
    array_position(ix.indkey, a.attnum);