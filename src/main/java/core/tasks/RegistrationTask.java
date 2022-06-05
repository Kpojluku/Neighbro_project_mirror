package core.tasks;

import VK.VKManager;
import com.vk.api.sdk.objects.messages.Message;
import core.Commander;
import core.Keyboards;
import core.constans.Constants;
import core.constans.Status;
import core.dataBase.DataBaseHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.Map;

public class RegistrationTask {

    // Вероятно все методы должны быть synchronized или метод телепорт

    public static void setUserName(Message message) throws SQLException {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
        dataBaseHandler.setUserStringField(message.getFromId(), message.getText(), Constants.USERS_NAME);
    }

    public static boolean setUserAge(Message message) throws SQLException {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
        try {
            int age = Integer.parseInt(message.getText());
            if (age <= 15) {
                VKManager.createVKManager().sendMessage("Чтобы начать пользоваться сервисом, " +
                                "тебе нужно сначала немного подрасти\uD83D\uDE09",
                        message.getFromId(), null);
                return false;
            }
            dataBaseHandler.setUserIntField(message.getFromId(), age, Constants.USERS_AGE);
        } catch (NumberFormatException e) {
            VKManager.createVKManager().sendMessage("Пожалуйста проверьте, что введенный вами возраст, это число",
                    message.getFromId(), null);
            return false;
        }
        return true;
    }

    public static boolean setUserSex(Message message) throws SQLException {
        String sex;
        if ("Я парень".equalsIgnoreCase(message.getText())) {
            sex = "m";
        } else if ("Я девушка".equalsIgnoreCase(message.getText())) {
            sex = "f";
        } else {
            Commander.choiceOption(message.getPeerId());
            return false;
        }
        DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
        dataBaseHandler.setUserStringField(message.getFromId(), sex, Constants.USERS_SEX);
        return true;
    }

    public static boolean setUserSearchingGender(Message message) throws SQLException {
        String sex;
        if ("Мужского".equalsIgnoreCase(message.getText())) {
            sex = "m";
        } else if ("Женского".equalsIgnoreCase(message.getText())) {
            sex = "f";
        } else if ("Без разницы".equalsIgnoreCase(message.getText())) {
            sex = "a";
        } else {
            Commander.choiceOption(message.getPeerId());
            return false;
        }
        DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
        dataBaseHandler.setUserStringField(message.getFromId(), sex, Constants.USERS_SEARCHINGGENDER);
        return true;
    }

    public static boolean setUserCity(Message message) throws SQLException {

        if (message.getGeo() != null && message.getGeo().getPlace() != null && !StringUtils.isEmpty(message.getGeo().getPlace().getCity())) {
            String city = message.getGeo().getPlace().getCity().equals("Нижний Нов") ? "Нижний Новгород" : message.getGeo().getPlace().getCity();
            VKManager.createVKManager().sendMessage("Это ваша область поиска - " + city + "?",
                    message.getFromId(), Keyboards.getYesOrNoBoard());
            DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
            dataBaseHandler.changeStatus(message.getPeerId(), Status.CONFIRMCITY);
            dataBaseHandler.setUserStringField(message.getFromId(), city, Constants.USERS_CITY);
            return false;
        } else if ("Москва".equals(message.getText()) || "Санкт-Петербург".equals(message.getText())) {
            DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
            dataBaseHandler.setUserStringField(message.getFromId(), message.getText(), Constants.USERS_CITY);
        } else {
            VKManager.createVKManager().sendMessage("Необходимо выбрать город",
                    message.getFromId(), Keyboards.getCityBoard());
            return false;
        }
        return true;

    }

    public static void setUserDescription(Message message) throws SQLException {
        if ("Пропустить".equalsIgnoreCase(message.getText())) return;
        DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
        dataBaseHandler.setUserStringField(message.getFromId(), message.getText(), Constants.USERS_DESCRIPTION);
    }

    public static void setUserImage(Message message) throws SQLException {
        int id = message.getPeerId();
        if ("Загрузить фото профиля".equalsIgnoreCase(message.getText())) {
            String photoId = VKManager.createVKManager().getUserInfo(id).getPhotoId();
            String photo = "photo" + photoId;
            DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
            dataBaseHandler.setUserStringField(id, photo, Constants.USERS_IMAGE);
            showFullProfile(id);
            dataBaseHandler.changeStatus(id, Status.SHOWPROFILE);
        } else if ("Выбрать другое фото".equalsIgnoreCase(message.getText())) {
            VKManager.createVKManager().sendMessage("Отправь мне своё фото",
                    id, null);

        } else if (message.getAttachments() != null && !message.getAttachments().isEmpty() &&
                message.getAttachments().get(0).getPhoto() != null) {

            String accessKey = message.getAttachments().get(0).getPhoto().getAccessKey();
            int photoId = message.getAttachments().get(0).getPhoto().getId();
            String photo = String.format("photo%s_%d_%s", id, photoId, accessKey);
            DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
            dataBaseHandler.setUserStringField(id, photo, Constants.USERS_IMAGE);
            showFullProfile(id);
            dataBaseHandler.changeStatus(id, Status.SHOWPROFILE);

        } else {
            VKManager.createVKManager().sendMessage("Нужно прикрепить фото к сообщению",
                    id, null);
        }
    }

    public static void showFullProfile(int id) throws SQLException {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
        Map<String, String> userInfo = dataBaseHandler.getUserProfile(id);
        String searchingGender = getSearchingGender(userInfo);
        if (StringUtils.isEmpty(userInfo.get("isActive")) || userInfo.get("isActive").equals("n")) {
            dataBaseHandler.setUserActivity(id);
        }

        String formContext =
                "Имя: " + userInfo.get("name") + "\n" +
                        "Твой пол: " + (userInfo.get("sex").equals("m") ? "Мужской" : "Женский") + "\n" +
                        "Пол соседа: " + searchingGender + "\n" +
                        "Возраст: " + userInfo.get("age") + "\n" +
                        "Город: " + userInfo.get("city") + "\n" +
                        "Описание: \n" + (userInfo.get("description") == null ? "" : userInfo.get("description"));
        String photo = userInfo.get("image");
        VKManager.createVKManager().sendMessageAndPhoto(formContext, photo, id);

        String options =
                "1. Заполнить анкету заново" + "\n" +
                        "2. Изменить фото" + "\n" +
                        "3. Изменить описание" + "\n" +
                        "4. Искать соседа";
        VKManager.createVKManager().sendMessage(options, id, Keyboards.getCheckFormBoard());

    }

    private static String getSearchingGender(Map<String, String> userInfo) {
        String searchingGender = userInfo.get("searchingGender");

        if (searchingGender.equals("m")) {
            searchingGender = "Мужской";
        } else if (searchingGender.equals("f")) {
            searchingGender = "Женский";
        } else searchingGender = "Без разницы";
        return searchingGender;
    }

}
