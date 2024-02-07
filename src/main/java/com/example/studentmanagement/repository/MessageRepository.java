package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query(value = "SELECT * FROM message WHERE (from_id = ?1 AND to_id = ?2) OR (from_id = ?2 AND to_id = ?1) ORDER BY message_date", nativeQuery = true)
    List<Message> findAllByUsersId(int user1, int user2);

}
