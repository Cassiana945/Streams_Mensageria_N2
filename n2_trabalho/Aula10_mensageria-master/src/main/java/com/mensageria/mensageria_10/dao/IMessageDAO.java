package com.mensageria.mensageria_10.dao;

import com.mensageria.mensageria_10.model.Message;

import java.util.List;
import java.util.Optional;


public interface IMessageDAO {
    Message create(Message message);
    boolean delete(Long id);
    boolean update(Message message);
    Optional <List<Message>> findAll();
}
