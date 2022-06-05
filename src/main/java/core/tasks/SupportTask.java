package core.tasks;

import VK.VKManager;
import core.Keyboards;
import core.constans.Status;
import core.dataBase.DataBaseHandler;

import java.sql.SQLException;

public class SupportTask {
    private static final DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();


    public static void getMenu(int id) throws SQLException {
        dataBaseHandler.changeStatus(id, Status.MAINMENU);

        String textMenu =
                "Отдохнём немного\n" +
                        "1. Искать соседа" + "\n" +
                        "2. Моя анкета" + "\n" +
                        "3. Отключить анкету";
        VKManager.createVKManager().sendMessage(textMenu, id, Keyboards.getDefaultBoard());

    }

    public static void offUserProfile(int id) throws SQLException {
        dataBaseHandler.offProfile(id);
        VKManager.createVKManager().sendMessage("Надеюсь, я тебе помог в поисках\n" +
                "Захочешь продолжить, просто напиши мне", id, null);

    }
}
