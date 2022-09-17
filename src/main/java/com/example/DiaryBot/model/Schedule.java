package com.example.DiaryBot.model;

import com.example.DiaryBot.model.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private String text;

    @OneToOne
    private Chat chat;

    public Schedule(DayOfWeek dayOfWeek, Chat chat) {
        this.dayOfWeek = dayOfWeek;
        this.chat = chat;
    }

    public Schedule(DayOfWeek dayOfWeek, String text, Chat chat) {
        this.dayOfWeek = dayOfWeek;
        this.text = text;
        this.chat = chat;
    }
}
