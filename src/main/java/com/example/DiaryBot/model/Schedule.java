package com.example.DiaryBot.model;

import com.example.DiaryBot.model.enums.DayOfWeek;
import com.example.DiaryBot.model.enums.ScheduleState;
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

    @OneToOne
    private Chat chat;

    private String text;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Enumerated(EnumType.STRING)
    private ScheduleState scheduleState;

    public Schedule(Chat chat, DayOfWeek dayOfWeek) {
        this.chat = chat;
        this.dayOfWeek = dayOfWeek;
        scheduleState = ScheduleState.EDITING;
    }

    public Schedule(DayOfWeek dayOfWeek, String text, Chat chat) {
        this.dayOfWeek = dayOfWeek;
        this.text = text;
        this.chat = chat;
    }
}
