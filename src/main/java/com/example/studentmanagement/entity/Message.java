package com.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "message")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private User user2;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date messageDate;
}
