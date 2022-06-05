package core;

import VK.VKManager;
import com.vk.api.sdk.objects.messages.Message;
import core.constans.Status;
import core.dataBase.DataBaseHandler;
import core.tasks.RegistrationTask;
import core.tasks.SearchTask;
import core.tasks.SupportTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class Commander {
    private static final Logger logger = LogManager.getLogger(Commander.class);

    /**
     * Обработка сообщений, получаемых через сервис Вконтакте.
     *
     * @param message сообщение (запрос) пользователя
     */
    public static void execute(Message message) {

        DataBaseHandler dataBaseHandler = DataBaseHandler.getDataBaseHandler();
        System.out.println(message.getPeerId());
        try {
            String[] state = dataBaseHandler.getUserState(message.getFromId());
            String status = state[0];
            String isActive = state[1];
            if (!StringUtils.isEmpty(status)) {
                if ("n".equals(isActive) && !Status.CHOICE.equals(status)) {
                    VKManager.createVKManager().sendMessage("Рад снова видеть тебя\n" +
                                    "Хочешь продолжить поиски?",
                            message.getPeerId(), Keyboards.getYesOrNoBoard());
                    dataBaseHandler.changeStatus(message.getPeerId(), Status.CHOICE);
                    return;
                }
                distribution(message, status, dataBaseHandler);
            } else {
                dataBaseHandler.addNewUser(message.getFromId());
                distribution(message, Status.NEWUSER, dataBaseHandler);
            }

        } catch (SQLException e) {
            logger.error("SQLException " + e.getMessage());
            e.printStackTrace();
            SqlError(message.getPeerId());
        }
    }

    private static void distribution(Message message, String status, DataBaseHandler dataBaseHandler) throws SQLException {
        int id = message.getPeerId();

        switch (status) {
            case (Status.SEARCH):
                SearchTask.analyzeAnswer(message);
                break;
            case (Status.MAINMENU):
                if ("1".equals(message.getText())) {
                    dataBaseHandler.changeStatus(message.getPeerId(), Status.SEARCH);
                    SearchTask.showNextProfile(message.getPeerId());
                } else if ("2".equals(message.getText())) {
                    dataBaseHandler.changeStatus(id, Status.SHOWPROFILE);
                    RegistrationTask.showFullProfile(id);
                } else if ("3".equals(message.getText())) {
                    dataBaseHandler.changeStatus(id, Status.OFFPROFILE);
                    VKManager.createVKManager().sendMessage("Вы уверены, что хотите отключить анкету? " +
                                    "Другие пользователи больше не смогут вас увидеть",
                            id, Keyboards.getYesOrBackBoard());

                } else {
                    choiceOption(id);
                }
                break;
            case (Status.CROSSROADS):
                if ("Искать дальше".equals(message.getText())) {
                    dataBaseHandler.changeStatus(message.getPeerId(), Status.SEARCH);
                    SearchTask.showNextProfile(message.getPeerId());
                } else if ("В главное меню".equals(message.getText())) {
                    SupportTask.getMenu(id);
                } else {
                    choiceOption(id);
                }
                break;
            case (Status.SOMEONELIKED):
                if ("Да".equalsIgnoreCase(message.getText())) {
                    SearchTask.showLastLikedUser(id);
                } else if ("Нет".equalsIgnoreCase(message.getText())) {
                    SupportTask.getMenu(id);
                } else {
                    choiceOption(id);
                }
                break;
            case (Status.OFFPROFILE):
                if ("Да".equalsIgnoreCase(message.getText())) {
                    SupportTask.offUserProfile(id);
                } else if ("Назад".equalsIgnoreCase(message.getText())) {
                    SupportTask.getMenu(id);
                } else {
                    choiceOption(id);
                }
                break;
            case (Status.SHOWPROFILE):
                checkUserFormMenu(message, id, dataBaseHandler);
                break;
            case (Status.RESETDESCRIPTION):
                if (!"Вернуться назад".equals(message.getText())) {
                    RegistrationTask.setUserDescription(message);
                }
                dataBaseHandler.changeStatus(id, Status.SHOWPROFILE);
                RegistrationTask.showFullProfile(id);
                break;
            case (Status.RESETPHOTO):
                if ("Оставить текущее".equals(message.getText())) {
                    dataBaseHandler.changeStatus(id, Status.SHOWPROFILE);
                    RegistrationTask.showFullProfile(id);
                } else {
                    RegistrationTask.setUserImage(message);
                }
                break;
            case (Status.NEWUSER):
                // Приветствие и предложение заполнить анкету
                if (!("\uD83D\uDC4D\uD83C\uDFFB".equals(message.getText()) || "да".equalsIgnoreCase(message.getText()))) {
                    helloMessage(id);
                } else {
                    askName(id, dataBaseHandler);
                }
                break;
            case (Status.SETNAME):
                RegistrationTask.setUserName(message);
                askAge(id, dataBaseHandler);
                break;
            case (Status.SETAGE):
                if (RegistrationTask.setUserAge(message))
                    askSex(id, dataBaseHandler);
                break;
            case (Status.SETSEX):
                if (RegistrationTask.setUserSex(message))
                    askGenderYouSearch(id, dataBaseHandler);
                break;
            case (Status.SETSEARCHINGGENDER):
                if (RegistrationTask.setUserSearchingGender(message))
                    askCity(id, dataBaseHandler);
                break;
            case (Status.SETCITY):
                if (RegistrationTask.setUserCity(message))
                    askDescription(id, dataBaseHandler);
                break;
            case (Status.CONFIRMCITY):
                if ("Нет".equalsIgnoreCase(message.getText())) {
                    askCity(id, dataBaseHandler);
                } else {
                    askDescription(id, dataBaseHandler);
                }
                break;
            case (Status.SETDESCRIPTION):
                RegistrationTask.setUserDescription(message);
                askPhoto(id, dataBaseHandler);
                break;
            case (Status.SETPHOTO):
                RegistrationTask.setUserImage(message);
                break;
            case (Status.CHOICE):
                if ("Да".equalsIgnoreCase(message.getText())) {
                    dataBaseHandler.setUserActivity(id);
                    dataBaseHandler.changeStatus(id, Status.CROSSROADS);
                    VKManager.createVKManager().sendMessage("Отлично, тогда продолжим", id, Keyboards.getNextOrMenuBoard());
                } else if ("Нет".equalsIgnoreCase(message.getText())) {
                    VKManager.createVKManager().sendMessage("Ты знаешь, где меня искать, когда снова понадоблюсь:)", id, null);
                    dataBaseHandler.changeStatus(id, Status.CROSSROADS);
                } else {
                    choiceOption(id);
                }
        }
    }

    public static void choiceOption(int id) {
        VKManager.createVKManager().sendMessage("Выберите один из предложенных вариантов",
                id, null);
    }

    private static void checkUserFormMenu(Message message, int id, DataBaseHandler dataBaseHandler) throws SQLException {
        if ("1".equals(message.getText())) {
            askName(id, dataBaseHandler);
        } else if ("2".equals(message.getText())) {
            askPhotoAgain(id, dataBaseHandler);
        } else if ("3".equals(message.getText())) {
            dataBaseHandler.changeStatus(id, Status.RESETDESCRIPTION);
            VKManager.createVKManager().sendMessage("Теперь расскажи о себе (чем занимаешься, увлекаешься)\n" +
                            "Напиши, каким ты видишь своего идеального соседа\n" +
                            "Какие твои представления о квартире которую ищешь или опиши ту, которая уже есть\n" +
                            "Удели этому этапу особое внимание\uD83D\uDE09",
                    id, Keyboards.getGoBackBoard());
        } else if ("4".equals(message.getText())) {
            dataBaseHandler.changeStatus(id, Status.SEARCH);
            SearchTask.showNextProfile(id);
        } else {
            choiceOption(id);
        }
    }

    private static void helloMessage(int id) {
        VKManager.createVKManager().sendMessage("Я помогу найти твоего идеального соседа.\n" +
                        "Можно, для начала, я задам тебе несколько вопросов?",
                id, Keyboards.getPositiveBoard());
    }

    private static void askName(int id, DataBaseHandler dataBaseHandler) throws SQLException {
        dataBaseHandler.changeStatus(id, Status.SETNAME);
        String name = VKManager.createVKManager().getUserInfo(id).getFirstName();
        VKManager.createVKManager().sendMessage("Как тебя зовут?",
                id, Keyboards.getBoardWithName(name));
    }

    private static void askAge(int id, DataBaseHandler dataBaseHandler) throws SQLException {
        dataBaseHandler.changeStatus(id, Status.SETAGE);
        VKManager.createVKManager().sendMessage("Сколько тебе лет?",
                id, null);
    }

    private static void askSex(int id, DataBaseHandler dataBaseHandler) throws SQLException {
        dataBaseHandler.changeStatus(id, Status.SETSEX);
        VKManager.createVKManager().sendMessage("Определимся с полом",
                id, Keyboards.getChoiceSexBoard());
    }

    private static void askGenderYouSearch(int id, DataBaseHandler dataBaseHandler) throws SQLException {
        dataBaseHandler.changeStatus(id, Status.SETSEARCHINGGENDER);
        VKManager.createVKManager().sendMessage("Какого пола видите вашего соседа?",
                id, Keyboards.getChoiceSexExpandBoard());
    }

    private static void askCity(int id, DataBaseHandler dataBaseHandler) throws SQLException {
        dataBaseHandler.changeStatus(id, Status.SETCITY);
        VKManager.createVKManager().sendMessage("В каком городе будем искать соседа?",
                id, Keyboards.getCityBoard());
    }

    private static void askDescription(int id, DataBaseHandler dataBaseHandler) throws SQLException {
        dataBaseHandler.changeStatus(id, Status.SETDESCRIPTION);
        VKManager.createVKManager().sendMessage("Теперь расскажи о себе (чем занимаешься, увлекаешься)\n" +
                        "Напиши, каким ты видишь своего идеального соседа\n" +
                        "Какие твои представления о квартире которую ищешь или опиши ту, которая уже есть\n" +
                        "Удели этому этапу особое внимание\uD83D\uDE09",
                id, Keyboards.getSkipBoard());
    }

    private static void askPhoto(int id, DataBaseHandler dataBaseHandler) throws SQLException {
        dataBaseHandler.changeStatus(id, Status.SETPHOTO);

        VKManager.createVKManager().sendMessage("Пришли мне своё фото, которое будут видеть другие пользователи",
                id, Keyboards.getImageBoard());
    }

    private static void askPhotoAgain(int id, DataBaseHandler dataBaseHandler) throws SQLException {
        dataBaseHandler.changeStatus(id, Status.RESETPHOTO);

        VKManager.createVKManager().sendMessage("Пришли мне своё фото, которое будут видеть другие пользователи",
                id, Keyboards.getImageBoard2());
    }

    public static void SqlError(int id) {
        VKManager.createVKManager().sendMessage("Возникла ошибка в базе данных в приложение Neighbro",
                102638836, null);
        VKManager.createVKManager().sendMessage("Похоже возникли какие-то проблемы. Попробуйте отправить мне сообщение чуть позже",
                id, null);
    }

}