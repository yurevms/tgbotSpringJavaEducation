package ru.spring.education.TgBot.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.spring.education.TgBot.Model.JokeModel;
import ru.spring.education.TgBot.Model.InvokeJoke;
import ru.spring.education.TgBot.Model.User;
import ru.spring.education.TgBot.Repository.RepositoryForInvokeJoke;
import ru.spring.education.TgBot.Repository.RepositoryInterface;

import java.time.LocalDateTime;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service implements ServiceInterface{

    private final RepositoryInterface repository;
    private final RepositoryForInvokeJoke repositoryForInvokeJoke;

    @Override
    public Page<JokeModel> getJokes(int page) {
        int pageSize = 5;
        return repository.findAll(Pageable.ofSize(pageSize).withPage(page));
    }

    @Override
    public Optional<JokeModel> getJokeByid(Long id, Long userId) {
        InvokeJoke invokeJoke = new InvokeJoke();
        invokeJoke.setUserId(userId);
        invokeJoke.setTime(LocalDateTime.now());
        invokeJoke.setJokeId(id);
        repositoryForInvokeJoke.save(invokeJoke);
        return repository.findById(id);
    }

    @Override
    public JokeModel postJoke(JokeModel jokeModel) {
        jokeModel.setCreatedTime(LocalDateTime.now());
        jokeModel.setUpdatedTime(null);
        if(jokeModel.getUser() == null) {
            jokeModel.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
        repository.save(jokeModel);
        return jokeModel;
    }

    @Override
    public Optional<JokeModel> putJoke(Long id, JokeModel jokeModel) {
        Optional<JokeModel> savedData = repository.findById(id);
        JokeModel modJoke = savedData.get();
        modJoke.setJoke(jokeModel.getJoke());
        modJoke.setUpdatedTime(LocalDateTime.now());
        repository.save(modJoke);
        return savedData;
    }

    @Override
    public void deleteJoke(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<JokeModel> getTop5Jokes() {
        return repository.getTop5Jokes(Pageable.ofSize(5).withPage(0));
    }

    @Override
    public JokeModel getRandomJoke(Long userId) {
        JokeModel jokeModel = repository.getRandomJoke();
        Long id = jokeModel.getId();
        InvokeJoke invokeJoke = new InvokeJoke();
        invokeJoke.setUserId(userId);
        invokeJoke.setTime(LocalDateTime.now());
        invokeJoke.setJokeId(id);
        repositoryForInvokeJoke.save(invokeJoke);
        return jokeModel;
    }

}
