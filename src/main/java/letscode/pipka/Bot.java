package letscode.pipka;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
public class Bot {

    private final TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));

    public void serve() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        Message message = update.message();
        InlineQuery inlineQuery = update.inlineQuery();
        CallbackQuery callbackQuery = update.callbackQuery();

        BaseRequest request = null;
        if (inlineQuery != null) {
            InlineQueryResultArticle paper = buildInlineButton("paper", "Paper", "0");
            InlineQueryResultArticle scissors = buildInlineButton("scissors", "Scissors", "1");
            InlineQueryResultArticle rock = buildInlineButton("rock", "Rock", "2");

            request = new AnswerInlineQuery(inlineQuery.id(), paper, scissors, rock);
        } else if (message != null) {
            long chatId = message.chat().id();
            request = new SendMessage(chatId, "Hello");
        }

        if(request != null) {
            bot.execute(request);
        }
    }

    private InlineQueryResultArticle buildInlineButton(String id, String title, String callbackData) {
        return new InlineQueryResultArticle(id, title, "i`am ready to fight")
                .replyMarkup(new InlineKeyboardMarkup(
                                new InlineKeyboardButton("Processing").callbackData(callbackData)
                        )
                );
    }
}
