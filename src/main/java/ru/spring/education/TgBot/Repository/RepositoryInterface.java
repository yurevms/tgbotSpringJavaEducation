package ru.spring.education.TgBot.Repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.spring.education.TgBot.Model.JokeModel;

import java.util.Optional;


public interface RepositoryInterface extends PagingAndSortingRepository<JokeModel, Long> {

    Page<JokeModel> findAll(Pageable pageable);

    Optional<JokeModel> findById(Long id);

    JokeModel save(JokeModel jokeModel);

    void deleteById(Long id);

    @Query(value = "SELECT j FROM jokes j ORDER BY (SELECT COUNT(c) FROM InvokeJoke c WHERE c.jokeId = j.id) DESC")
    Page<JokeModel> getTop5Jokes(Pageable pageable);

    @Query(value = "SELECT * FROM jokes ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    JokeModel getRandomJoke();

}
