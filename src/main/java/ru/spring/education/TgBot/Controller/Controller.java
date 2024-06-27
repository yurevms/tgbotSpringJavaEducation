package ru.spring.education.TgBot.Controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spring.education.TgBot.Model.JokeModel;
import ru.spring.education.TgBot.Service.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/jokes")
public class Controller {

    private final Service service;

    @GetMapping
    public ResponseEntity<Page<JokeModel>> getJokes(@RequestParam("page") int page) {
        return ResponseEntity.ok(service.getJokes(page));
    }

    @GetMapping("/{id}/{userId}")
    public ResponseEntity<JokeModel> getJokeById(@PathVariable Long id, @PathVariable Long userId) {
        Optional<JokeModel> jokeModelOptional = service.getJokeByid(id, userId);
        return jokeModelOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> postJoke(@RequestBody JokeModel jokeModel){
        service.postJoke(jokeModel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJoke(@PathVariable Long id){
        Optional<JokeModel> jokeModelOptional = service.getJokeByid(id, null);
        if(jokeModelOptional.isPresent()){
            service.deleteJoke(id);
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putJoke(@PathVariable Long id, @RequestBody JokeModel jokeModel){
        Optional<JokeModel> jokeModelOptional = service.getJokeByid(id, null);
        if(jokeModelOptional.isPresent()){
            service.putJoke(id, jokeModel);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/top")
    public ResponseEntity<Page<JokeModel>> getTopJokes() {
        return ResponseEntity.ok(service.getTop5Jokes());
    }

    @GetMapping("/random")
    public ResponseEntity<JokeModel> getRandomJokes(@RequestParam("user_id") Long user_id) {
        return ResponseEntity.ok(service.getRandomJoke(user_id));
    }

}
