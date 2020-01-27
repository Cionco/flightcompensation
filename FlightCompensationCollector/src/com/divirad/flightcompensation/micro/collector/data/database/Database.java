package com.divirad.flightcompensation.micro.collector.data.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.divirad.flightcompensation.micro.collector.server.ContextListener;

public class Database {
    public interface ISetParams {
        void run(PreparedStatement ps) throws SQLException;
    }
    public interface IUseResultSet <T> {
        T run(ResultSet ps) throws SQLException;
    }

    /**
     * Executes a sql query with no parameter
     * @param sql prepared sql string
     * @param useResultSet function using the ResultSet to create the return value
     * @param <T> return Type
     * @return the return value of useResultSet
     */
    public static <T> T query(String sql, IUseResultSet<T> useResultSet) {
        return query(sql, ps -> {}, useResultSet);
    }

    /**
     * Executes a sql query
     * @param sql prepared sql string
     * @param setParams function to set the parameters of the PreparedStatement
     * @param useResultSet function using the ResultSet to create the return value
     * @param <T> return Type
     * @return the return value of useResultSet
     */
    public static <T> T query(String sql, ISetParams setParams, IUseResultSet<T> useResultSet) {
        try (Connection con = ContextListener.getDataSource().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                setParams.run(ps);
                try (ResultSet rs = ps.executeQuery()) {
                    return useResultSet.run(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Executes a sql command with no parameter
     * @param sql prepared sql string
     */
    public static void execute(String sql) {
        execute(sql, ps -> {});
    }

    /**
     * Executes a sql command
     * @param sql prepared sql string
     * @param setParams function to set the parameters of the PreparedStatement
     */
    public static void execute(String sql, ISetParams setParams) {
        try (Connection con = ContextListener.getDataSource().getConnection()) {
        		try (PreparedStatement ps = con.prepareStatement(sql)) {
                setParams.run(ps);
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getLastID() {
        return query("SELECT LAST_INSERT_ID();", rs -> rs != null && rs.next() ? rs.getInt(1) : null);
    }
}
