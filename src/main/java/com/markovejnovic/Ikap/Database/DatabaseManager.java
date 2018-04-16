package com.markovejnovic.Ikap.Database;

import com.markovejnovic.Ikap.KitchenBooking.KitchenBooking;
import com.markovejnovic.Ikap.Supporting.Configuration;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection = null;
    private Statement selectAllStatement = null;
    private Statement select2WeeksStatement = null;
    private Statement selectAllDatesStatement = null;

    public void connect() throws ClassNotFoundException, SQLException {
        if (connection == null) {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + Configuration.getDBPath());
        }
    }

    public List<KitchenBooking> getBookings() throws SQLException {
        if (this.selectAllStatement == null) {
            this.selectAllStatement = connection.createStatement();
        }
        ResultSet rs = this.selectAllStatement.executeQuery(
                "SELECT * FROM kitchen_schedule;");

        List<KitchenBooking> list = new ArrayList<KitchenBooking>();
        while (rs.next()) {
            KitchenBooking kb = new KitchenBooking();
            kb.setEmail(rs.getString("email"));
            kb.setDate(LocalDate.ofEpochDay(rs.getLong("date")));
            kb.setStartTime(LocalTime.ofSecondOfDay(rs.getInt("startTime")));
            kb.setStopTime(LocalTime.ofSecondOfDay(rs.getInt("stopTime")));
            kb.setPurpose(rs.getString("purpose"));
            kb.setNotes(rs.getString("notes"));
            list.add(kb);
        }

        return list;
    }

    public List<KitchenBooking> getBookingsFor2Weeks()
            throws SQLException {
        if (this.select2WeeksStatement == null) {
            this.select2WeeksStatement = connection.createStatement();
        }
        ResultSet rs = this.select2WeeksStatement.executeQuery(
                "SELECT * FROM kitchen_schedule WHERE date >" + String.valueOf(LocalDate.now().toEpochDay()) +
                        " AND date < " + String.valueOf(LocalDate.now().plusDays(14).toEpochDay()) +
                        " ORDER BY date" + ";");

        List<KitchenBooking> list = new ArrayList<KitchenBooking>();
        while (rs.next()) {
            KitchenBooking kb = new KitchenBooking();
            kb.setEmail(rs.getString("email"));
            kb.setDate(LocalDate.ofEpochDay(rs.getLong("date")));
            kb.setStartTime(LocalTime.ofSecondOfDay(rs.getInt("startTime")));
            kb.setStopTime(LocalTime.ofSecondOfDay(rs.getInt("stopTime")));
            kb.setPurpose(rs.getString("purpose"));
            kb.setNotes(rs.getString("notes"));
            list.add(kb);
        }

        return list;
    }

    public List<LocalDate> getDates() throws SQLException {
        if (this.selectAllDatesStatement == null) {
            this.selectAllDatesStatement = connection.createStatement();
        }
        ResultSet rs = this.selectAllDatesStatement.executeQuery(
                "SELECT date FROM kitchen_schedule;");
        List<LocalDate> dates = new ArrayList<LocalDate>();
        while (rs.next()) {
            dates.add(LocalDate.ofEpochDay(rs.getLong("date")));
        }

        return dates;
    }

    public void addBooking(KitchenBooking kitchenBooking) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO kitchen_schedule " +
                        "(email, date, startTime, stopTime, purpose, notes) VALUES " +
                        "(?, ?, ?, ?, ?, ?)"
        );
        statement.setString(1, kitchenBooking.getEmail());
        statement.setLong(2, kitchenBooking.getDate().toEpochDay());
        statement.setInt(3, kitchenBooking.getStartTime().toSecondOfDay());
        statement.setInt(4, kitchenBooking.getStopTime().toSecondOfDay());
        statement.setString(5, kitchenBooking.getPurpose());
        statement.setString(6, kitchenBooking.getNotes());
        statement.executeUpdate();
        statement.close();
    }

    public void disconnect() throws SQLException {
        if (selectAllStatement != null) {
            selectAllStatement.close();
            selectAllStatement = null;
        }
        if (selectAllDatesStatement != null) {
            selectAllDatesStatement.close();
            selectAllDatesStatement = null;
        }
        if (select2WeeksStatement != null) {
            select2WeeksStatement.close();
            select2WeeksStatement = null;
        }
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public void forceDisconnect() throws SQLException {
        selectAllStatement.close();
        selectAllStatement = null;
        selectAllDatesStatement.close();
        selectAllDatesStatement = null;
        select2WeeksStatement.close();
        select2WeeksStatement = null;
        connection.close();
        connection = null;
    }
}
