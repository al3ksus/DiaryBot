package com.example.DiaryBot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Timer;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToOne
    private Chat chat;

    private String text;

    private String time;

    public Reminder(Chat chat, String text) {
        this.chat = chat;
        this.text = text;
    }

    public String toString() {
        return text + ' ' + (time == null? "": time);
    }
}
