package ru.spring.education.TgBot.Repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.spring.education.TgBot.Model.InvokeJoke;

public interface RepositoryForInvokeJoke extends PagingAndSortingRepository<InvokeJoke, Long> {
    void save(InvokeJoke invokeJoke);
}
