package com.example.playlistdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.lang.Object;

import static javafx.scene.input.KeyCode.T;

@Component("SqliteDaoImpl")
public class SQLiteDAOImpl implements SQLiteDAO {
    private SimpleJdbcInsert insertMP3;
    private NamedParameterJdbcTemplate jdbcTemplate;
    private javax.sql.DataSource dataSource;

    @Autowired
    public void setDataSource(javax.sql.DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertMP3 = new SimpleJdbcInsert(dataSource).withTableName("mp3").usingColumns("name", "author");
        this.dataSource = dataSource;
    }

    @Override
    public int insert(MP3 mp3) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", mp3.getName());
        params.addValue("author", mp3.getAuthor());
        return insertMP3.execute(params);
    }

    @Override
    public int insertList(List<MP3> mp3List) {
        String sql = "insert into mp3 (name, author) VALUES (:author, :name)";

        SqlParameterSource[] params = new SqlParameterSource[mp3List.size()];

        int i = 0;

        for (MP3 mp3 : mp3List) {
            MapSqlParameterSource p = new MapSqlParameterSource();
            p.addValue("name", mp3.getName());
            p.addValue("author", mp3.getAuthor());

            params[i] = p;
            i++;
        }

        // SqlParameterSource[] batch =
        // SqlParameterSourceUtils.createBatch(listMP3.toArray());
        int[] updateCounts = jdbcTemplate.batchUpdate(sql, params);
        return updateCounts.length;
    }

    @Override
    public void delete(MP3 mp3) {
        delete(mp3.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "delete from mp3 where id=:id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        jdbcTemplate.update(sql, params);
    }

    @Override
    public MP3 getMP3ByID(int id) {
        String sql = "select * from mp3 where id=:id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbcTemplate.queryForObject(sql, params, RowMapper<T> rowMapper);
    }

    @Override
    public List<MP3> getMP3ListByName(String name) {
        String sql = "select * from mp3 where upper(name) like :name";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", "%" + name.toUpperCase() + "%");

        return jdbcTemplate.query(sql, params, new MP3RowMapper());
    }

    @Override
    public List<MP3> getMP3ListByAuthor(String author) {
        String sql = "select * from mp3 where upper(author) like :author";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author", "%" + author.toUpperCase() + "%");

        return jdbcTemplate.query(sql, params, new MP3RowMapper());
    }

    @Override
    public int getMP3Count() {
        String sql = "select count(*) from mp3";
        return jdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
    }

    @Override
    public Map<String, Integer> getStat() {
        String sql = "select author, count(*) as count from mp3 group by author";

        return jdbcTemplate.query(sql, new ResultSetExtractor<Map<String, Integer>>() {

            public Map<String, Integer> extractData(ResultSet rs) throws SQLException {
                Map<String, Integer> map = new TreeMap<>();
                while (rs.next()) {
                    String author = rs.getString("author");
                    int count = rs.getInt("count");
                    map.put(author, count);
                }
                return map;
            }
        });

    }
}
