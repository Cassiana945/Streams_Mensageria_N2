package com.mensageria.mensageria_10.dao;

import com.mensageria.mensageria_10.config.ConnectionFactory;
import com.mensageria.mensageria_10.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MessageDAO implements IMessageDAO {

    @Override
    public Message create(Message message) {
        String query = "INSERT INTO tempo " +
                "(cidade, alerta) " +
                "VALUES (?, ?)";
        Message m = new Message();
        try (Connection con = ConnectionFactory.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, message.getCidade());
            ps.setString(2, message.getAlerta());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                m.setId(rs.getLong(1));
                m.setCidade(rs.getString(2));
                m.setAlerta(rs.getString(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return m;
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM tempo WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Message message) {
        String query = "UPDATE tempo SET cidade = ?, alerta = ? WHERE id = ?";
        try (Connection con = ConnectionFactory.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, message.getCidade());
            ps.setString(2, message.getAlerta());
            ps.setLong(3, message.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<List<Message>> findAll() {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM tempo";
        try (Connection con = ConnectionFactory.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message m = new Message();
                m.setId(rs.getLong("id"));
                m.setCidade(rs.getString("cidade"));
                m.setAlerta(rs.getString("alerta"));
                messages.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(messages);
    }
}
