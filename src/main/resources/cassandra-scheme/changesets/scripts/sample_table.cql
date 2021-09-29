create table sample_table
(
    id   text primary key,
    name text
)
    with caching = {'keys': 'ALL', 'rows_per_partition': 'ALL'}
        and compaction = {'class': 'SizeTieredCompactionStrategy'}
        and compression = {'sstable_compression': 'org.apache.cassandra.io.compress.LZ4Compressor',
        'chunk_length_in_kb': '64'}
        and dclocal_read_repair_chance = 0.0
        and speculative_retry = '99.0PERCENTILE';