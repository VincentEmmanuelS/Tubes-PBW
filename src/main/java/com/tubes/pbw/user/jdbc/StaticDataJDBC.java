package com.tubes.pbw.user.jdbc;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tubes.pbw.user.model.DataStatic;
import com.tubes.pbw.user.model.Tahun;
import com.tubes.pbw.user.repository.StaticRepository;

@Repository
public class StaticDataJDBC implements StaticRepository{

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<DataStatic> findDistance(String email, String rideType, int tahun, String matric) {
        String sql ="""
                select
                    sum(distance) as value , month
                from
                    (SELECT 
                        Extract(Day from tanggal_event) as tanggal, EXTRACT(MONTH FROM tanggal_event) as month, EXTRACT(YEAR FROM tanggal_event) as year , distance, matric_distance, elevation, matric_elevation, duration, ridetype, email
                    FROM manualentry
                    ORDER BY tanggal_event)as data1
                where
                    email = ? AND matric_distance = ? AND ridetype = ? and year = ?
                group by
                    month
                """;
       return jdbc.query(sql,this::mapToRowStatic,email,matric,rideType,tahun);
    }

    public DataStatic mapToRowStatic(ResultSet resultSet, int rowNum) throws SQLException{
        return new DataStatic(resultSet.getInt("month"), resultSet.getDouble("value"));
    }

    @Override
    public List<Tahun> listYear(String email) {
        String sql ="""
                SELECT DISTINCT 
                    CAST(EXTRACT(YEAR FROM tanggal_event) AS INT) AS tahun
                FROM 
                    manualentry
                WHERE 
                    email = ?;

                """;
        return jdbc.query(sql,this::mapToYear,email );
    }

    public Tahun mapToYear(ResultSet resultSet, int rowNum) throws SQLException{
        return new Tahun(resultSet.getInt("tahun"));
    }

    @Override
    public List<DataStatic> findDuration(String email, int tahun) {
        String sql ="""
                select
                    CAST(EXTRACT(EPOCH FROM SUM(duration))/ 3600 AS decimal(6,3)) AS value , month
                from
                    (SELECT 
                        Extract(Day from tanggal_event) as tanggal, EXTRACT(MONTH FROM tanggal_event) as month, EXTRACT(YEAR FROM tanggal_event) as year , distance, matric_distance, elevation, matric_elevation, duration, ridetype, email
                    FROM manualentry
                    ORDER BY tanggal_event)as data1
                where
                    email = ? and year = ?
                group by
	                month
                """;
        return jdbc.query(sql, this::mapToRowStatic,email,tahun);
    }

    @Override
    public List<DataStatic> findActivity(String email, String ridetype, int tahun) {
        String sql ="""
            select
                count(rideType) as value , month
            from
                (SELECT 
                    Extract(Day from tanggal_event) as tanggal, EXTRACT(MONTH FROM tanggal_event) as month, EXTRACT(YEAR FROM tanggal_event) as year , distance, matric_distance, elevation, matric_elevation, duration, ridetype, email
                FROM manualentry
                ORDER BY tanggal_event)as data1
            where
                email = ? and year = ? and ridetype = ?
            group by
                month
            """;
        return jdbc.query(sql, this::mapToRowStatic, email, tahun, ridetype);
    }
    


}
