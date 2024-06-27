package ru.spring.education.TgBot.Service;

import org.springframework.data.domain.Page;
import ru.spring.education.TgBot.Model.JokeModel;

import java.util.Optional;

public interface ServiceInterface {
    Page<JokeModel> getJokes(int page);

    Optional<JokeModel> getJokeByid(Long id, Long userId);

    JokeModel postJoke(JokeModel jokeModel);

    Optional<JokeModel> putJoke(Long id, JokeModel jokeModel);

    void deleteJoke(Long id);

    Page<JokeModel> getTop5Jokes();

    JokeModel getRandomJoke(Long userId);
}
