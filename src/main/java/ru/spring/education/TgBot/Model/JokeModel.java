package ru.spring.education.TgBot.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "jokes")
@Table(name = "jokes")
public class JokeModel {
    @Id
    @GeneratedValue(generator = "jokes_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "jokes_id_seq", sequenceName = "jokes_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "joke")
    private String joke;

    @Column(name = "createdTime")
    private LocalDateTime createdTime;

    @Column(name = "updatedTime")
    private LocalDateTime updatedTime;

    @JsonIgnore
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
}
