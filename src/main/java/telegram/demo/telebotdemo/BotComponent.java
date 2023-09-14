package telegram.demo.telebotdemo;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;

@Component
public class BotComponent {

    @Value("${bot.token}")
    private String botToken;

    TelegramBot bot;

    @PostConstruct
    void after() {
        this.bot = new TelegramBot(botToken);

        bot.setUpdatesListener(updates -> {
            handleUpdates(updates);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                e.response().errorCode();
                e.response().description();
            } else {
                e.printStackTrace();
            }
        });
    }

    private void handleUpdates(List<Update> updates) {
        updates.forEach(_update -> {
            System.out.println("[Sender Message]: " + _update.message().text());
            System.out.println("[From info]: " + _update.message().from().toString());

            bot.execute(new SendMessage(_update.message().chat().id(),
                    "[Bot response, your id: ] :" + _update.message().from().id()));
        });
    }

}
