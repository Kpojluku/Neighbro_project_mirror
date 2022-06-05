package core.tasks;

import VK.VKManager;
import com.vk.api.sdk.objects.messages.Message;
import core.Commander;
import core.Keyboards;
import core.constans.Constants;
import core.constans.Status;
import core.dataBase.DataBaseHandler;

import java.sql.SQLException;
import java.util.Map;

public class SearchTask {

    public static void analyzeAnswer(Message message) throws SQLException {
        String text = message.getText();
        int id = message.getPeerId();
        DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
        //Лайк
        if ("\uD83D\uDC4D\uD83C\uDFFB".equals(text)) {
            // добавить пользователя из previousUserId в users_like
            int prevUserId = dataBaseHandler.getPreviousUserId(id);
            dataBaseHandler.addUserToLikeTable(id, prevUserId);
            // Проверка на взаимность
            if (dataBaseHandler.isMatch(prevUserId, id)) {
                dataBaseHandler.changeStatus(id, Status.CROSSROADS);
                showMatchProfile(id, prevUserId, dataBaseHandler);
                sendMessageAnotherUser(prevUserId, id, dataBaseHandler);
            } else {
                // проверяем статус prevUserId, если не равен someoneLiked
                // устанавливаем ему поле USERS_LASTLIKED тем юзером, который его лайкнул
                // отправляем ему сообщение, что он понравился кому-то, меняем ему статус на someoneLiked
                // и включаем ему соответствующую логику
                if(!Status.SOMEONELIKED.equals(dataBaseHandler.getUserStatus(prevUserId))){
                    dataBaseHandler.setUserIntField(prevUserId, id, Constants.USERS_LASTLIKED);
                    VKManager.createVKManager().sendMessage("Какому-то пользователю понравилась твоя анкета\n" +
                                    "Показать его?",
                            prevUserId, Keyboards.getYesOrNoBoard());
                    dataBaseHandler.setUserLakedState(id, prevUserId);
                }
                showNextProfile(id);
            }
        //Дизлайк
        } else if ("\uD83D\uDC4E\uD83C\uDFFB".equals(text)) {
            int prevUserId = dataBaseHandler.getPreviousUserId(id);
            dataBaseHandler.addUserToDislikeTable(id, prevUserId);
            showNextProfile(id);
        } else if ("Жалоба".equalsIgnoreCase(text)){
            // можно сделать отправку жалоб на почту
            // Предложить выбрать тему жалобы
            int prevUserId = dataBaseHandler.getPreviousUserId(id);
            dataBaseHandler.addUserToDislikeTable(id, prevUserId);
            VKManager.createVKManager().sendMessage("Жалоба на " + prevUserId,
                    28534344, null);
            VKManager.createVKManager().sendMessage("Спасибо. Я рассмотрю вашу жалобу на этого пользователя",
                    id, null);
            showNextProfile(id);
        }else if("В меню".equalsIgnoreCase(text)){
            SupportTask.getMenu(id);
        }else {
            Commander.choiceOption(message.getPeerId());
        }
    }

    public static void showNextProfile(int id) throws SQLException {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
        dataBaseHandler.setUserLastActivity(id);
        Map<String, String> userProfile = dataBaseHandler.getNextUserProfile(id);
        String nextUserId = userProfile.get("id");
        if (nextUserId != null) {
            dataBaseHandler.setPreviousUserId(id, Integer.parseInt(nextUserId));
            showProfile(userProfile, id);
        } else {
            dataBaseHandler.changeStatus(id, Status.CROSSROADS);
            VKManager.createVKManager().sendMessage("Анкеты под твои параметры закончились\n" +
                            "Можешь пока подписаться на меня, сделать репост закрепленной записи и немного подождать\n" +
                            "А затем поищи снова, за это время кто-нибудь обязательно появится\uD83D\uDE09",
                    id, Keyboards.getNextOrMenuBoard());
        }
    }

    private static void showProfile(Map<String, String> userProfile, int id) {
        String formContext =
                userProfile.get("name") + ", " +
                        userProfile.get("age") + ", " +
                        userProfile.get("city") + "\n" +
                        (userProfile.get("description") == null ? "" : userProfile.get("description"));
        String photo = userProfile.get("image");
        VKManager.createVKManager().sendMessageAndPhotoAndKeyboard(formContext, photo, id, Keyboards.getLikeDislikeBoard());

    }

    private static void showMatchProfile(int userId, int anotherId, DataBaseHandler dataBaseHandler) throws SQLException {
        Map<String, String> userProfile = dataBaseHandler.getUserProfile(anotherId);
        String formContext =
                "Отлично! Надеюсь найдёте общий язык - vk.com/id" + userProfile.get("id") + "\n" +
                        userProfile.get("name") + ", " +
                        userProfile.get("age") + ", " +
                        userProfile.get("city") + "\n" +
                        (userProfile.get("description") == null ? "" : userProfile.get("description"));
        String photo = userProfile.get("image");
        VKManager.createVKManager().sendMessageAndPhotoAndKeyboard(formContext, photo, userId, Keyboards.getNextOrMenuBoard());

    }

    private static void sendMessageAnotherUser(int id, int anotherId, DataBaseHandler dataBaseHandler) throws SQLException {
        Map<String, String> userProfile = dataBaseHandler.getUserProfile(anotherId);
        String formContext =
                "Нашёл кое-кого для тебя\nvk.com/id" + userProfile.get("id") + "\n" +
                        userProfile.get("name") + ", " +
                        userProfile.get("age") + ", " +
                        userProfile.get("city") + "\n" +
                        (userProfile.get("description") == null ? "" : userProfile.get("description"));
        String photo = userProfile.get("image");
        dataBaseHandler.changeStatus(id, Status.CROSSROADS);
        VKManager.createVKManager().sendMessageAndPhotoAndKeyboard(formContext, photo, id, Keyboards.getNextOrMenuBoard());

    }

    public static void showLastLikedUser(int id) throws SQLException {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
        int lastLikedId = dataBaseHandler.getLastLikedUserProfile(id);
        dataBaseHandler.changeStatus(id, Status.SEARCH);
        if(lastLikedId != 0){
            Map<String, String> userProfile = dataBaseHandler.getUserProfile(lastLikedId);
            dataBaseHandler.setPreviousUserId(id, lastLikedId);
            showProfile(userProfile, id);
        }else {
            // такого не должно быть, на всякий
            showNextProfile(id);
        }
    }

}
