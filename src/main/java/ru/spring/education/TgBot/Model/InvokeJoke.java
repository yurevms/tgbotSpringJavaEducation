package ru.spring.education.TgBot.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "InvokeJoke")
@Table(name = "InvokeJoke")
public class InvokeJoke {
    @Id
    @GeneratedValue(generator = "InvokeJoke_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "InvokeJoke_id_seq", sequenceName = "InvokeJoke_id_seq", initialValue = 1, allocationSize = 1)
    private Long historyId;

    @Column(name = "joke_id")
    private Long jokeId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "time")
    LocalDateTime time;
}
