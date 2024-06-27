package ru.spring.education.TgBot.MyTelegramBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.spring.education.TgBot.Model.JokeModel;
import ru.spring.education.TgBot.Model.User;
import ru.spring.education.TgBot.Service.Service;
import ru.spring.education.TgBot.Service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class TelegramBotService {

    private final TelegramBot telegramBot;
    private final Service jokeService;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final UserSessionManager userSessionManager;

    @Autowired
    public TelegramBotService(TelegramBot telegramBot, Service jokeService, UserServiceImpl userService, PasswordEncoder passwordEncoder, UserSessionManager userSessionManager) {
        this.telegramBot = telegramBot;
        this.jokeService = jokeService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userSessionManager = userSessionManager;
        this.telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, Throwable::printStackTrace);
    }

    private void processUpdate(Update update) {
        String messageText = update.message().text();
        Long chatId = update.message().chat().id();

        if (!messageText.startsWith("/") || messageText.length() == 1) {
            handleUnknownCommand(chatId);
            return;
        }

        if (messageText.startsWith("/login")) {
            handleLogin(chatId, messageText.replace("/login ", ""));
        } else if (messageText.startsWith("/getjokes")) {
            handleGetAllJokes(chatId, messageText.replace("/getjokes ", ""));
        } else if (messageText.startsWith("/getjoke")) {
            handleGetJokeById(chatId, messageText.replace("/getjoke ", ""));
        } else if (messageText.startsWith("/addjoke")) {
            handleAddJoke(chatId, messageText.replace("/addjoke ", ""));
        } else if (messageText.startsWith("/updatejoke")) {
            handleUpdateJoke(chatId, messageText.replace("/updatejoke ", ""));
        } else if (messageText.startsWith("/deletejoke")) {
            handleDeleteJoke(chatId, messageText.replace("/deletejoke ", ""));
        } else if (messageText.startsWith("/topjokes")) {
            handleGetTopJokes(chatId);
        } else if (messageText.startsWith("/randomjoke")) {
            handleGetRandomJoke(chatId);
        }
    }

    private void handleLogin(Long chatId, String credentials) {
        String[] parts = credentials.split(" ");
        if (parts.length != 2) {
            sendMessage(chatId, "Использование: /login <Логин> <Пароль>");
            return;
        }
        String username = parts[0];
        String password = parts[1];
        try {
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                userSessionManager.loginUser(chatId, username);
                sendMessage(chatId, "Авторизация успешна!");
            } else {
                sendMessage(chatId, "Неверный пароль.");
            }
        } catch (Exception e) {
            sendMessage(chatId, "Пользователь не найден.");
        }
    }

    private void handleGetAllJokes(Long chatId, String page) {
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            pageNumber = 0;
            sendMessage(chatId, "Неверный формат номера страницы");
            return;
        }
        Page<JokeModel> jokes = jokeService.getJokes(pageNumber);
        List<JokeModel> jokesss = jokes.getContent();
        StringBuilder response = new StringBuilder("Вот все шутки на странице:\n");
        for (JokeModel joke : jokesss) {
            response.append(joke.getId()).append(": ").append(joke.getJoke()).append("\n");
        }
        sendMessage(chatId, response.toString());
    }

    private void handleGetJokeById(Long chatId, String idText) {
        try {
            Long id = Long.parseLong(idText);
            Optional<JokeModel> joke = jokeService.getJokeByid(id, chatId);
            if (joke.isPresent()) {
                sendMessage(chatId, joke.get().getJoke());
            } else {
                sendMessage(chatId, "Шутка не найдена.");
            }
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID.");
        }
    }

    private void handleAddJoke(Long chatId, String jokeText) {
        if (!userSessionManager.isUserLoggedIn(chatId)) {
            sendMessage(chatId, "У вас не достаточно прав. Пожалуйста авторизуйтесь.");
            return;
        }
        JokeModel newJoke = new JokeModel();
        newJoke.setJoke(jokeText);
        String username = userSessionManager.getUsername(chatId);
        User user = userService.findByUsername(username).get();
        newJoke.setUser(user);
        JokeModel savedJoke = jokeService.postJoke(newJoke);
        sendMessage(chatId, "Шутка добавлена с ID: " + savedJoke.getId());
    }

    private void handleUpdateJoke(Long chatId, String text) {
        if (!userSessionManager.isUserLoggedIn(chatId)) {
            sendMessage(chatId, "У вас не достаточно прав. Пожалуйста авторизуйтесь.");
            return;
        }
        String[] parts = text.split(" ", 2);
        if (parts.length < 2) {
            sendMessage(chatId, "Использование: /updatejoke <id> <новый текст шутки>");
            return;
        }
        try {
            Long id = Long.parseLong(parts[0]);
            String jokeText = parts[1];
            JokeModel jokeModel = new JokeModel();
            jokeModel.setJoke(jokeText);
            Optional<JokeModel> updatedJoke = jokeService.putJoke(id, jokeModel);
            if (updatedJoke.isPresent()) {
                sendMessage(chatId, "Шутка обновлена.");
            } else {
                sendMessage(chatId, "Шутка не найдена.");
            }
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID.");
        }
    }

    private void handleDeleteJoke(Long chatId, String idText) {
        if (!userSessionManager.isUserLoggedIn(chatId)) {
            sendMessage(chatId, "У вас не достаточно прав. Пожалуйста авторизуйтесь.");
            return;
        }
        try {
            Long id = Long.parseLong(idText);
            jokeService.deleteJoke(id);
            sendMessage(chatId, "Шутка удалена.");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID.");
        }
    }

    private void handleUnknownCommand(Long chatId) {
        sendMessage(chatId, "Неизвестная команда.");
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage request = new SendMessage(chatId, text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
        telegramBot.execute(request);
    }

    private void handleGetTopJokes(Long chatId) {
        Page<JokeModel> jokes = jokeService.getTop5Jokes();
        List<JokeModel> jokesss = jokes.getContent();
        StringBuilder response = new StringBuilder("Вот топ 5 шуток:\n");
        for (JokeModel joke : jokesss) {
            response.append(joke.getId()).append(": ").append(joke.getJoke()).append("\n");
        }
        sendMessage(chatId, response.toString());
    }

    private void handleGetRandomJoke(Long chatId) {
        JokeModel randomJoke = jokeService.getRandomJoke(chatId);
        sendMessage(chatId, randomJoke.getJoke());
    }
}
