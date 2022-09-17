package com.example.DiaryBot.model;

import com.example.DiaryBot.model.enums.BotState;
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
public class Chat {

    @Id
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private BotState botState;

}
