package core;

import com.vk.api.sdk.objects.messages.*;

import java.util.ArrayList;
import java.util.List;

public class Keyboards {
    private static Keyboard defaultBoard;
    private static Keyboard positiveBoard;
    private static Keyboard choiceSexBoard;
    private static Keyboard choiceSexExpandBoard;
    private static Keyboard cityBoard;
    private static Keyboard imageBoard;
    private static Keyboard imageBoard2;
    private static Keyboard goBackBoard;
    private static Keyboard checkFormBoard;
    private static Keyboard skipBoard;
    private static Keyboard likeDislikeBoard;
    private static Keyboard nextOrMenuBoard;
    private static Keyboard yesOrBackBoard;
    private static Keyboard YesOrNoBoard;

    public static void createKeyboards() {
        defaultBoard = createDefaultBoard();
        positiveBoard = createPositiveBord();
        choiceSexBoard = createChoiceSexBoard();
        choiceSexExpandBoard = createChoiceSexExpandBoard();
        cityBoard = createCityBoard();
        imageBoard = createImageBoard();
        imageBoard2 = createImageBoard2();
        goBackBoard = createGoBackBoard();
        checkFormBoard = createCheckFormBoard();
        skipBoard = createSkipBoard();
        likeDislikeBoard = createLikeDislikeBoard();
        nextOrMenuBoard = createNextOrMenuBoard();
        yesOrBackBoard = createYesOrBackBoard();
        YesOrNoBoard = createYesOrNoBoard();
    }

    public static Keyboard createDefaultBoard() {
        Keyboard keyboard = new Keyboard();
        KeyboardButton one = new KeyboardButton();
        KeyboardButton two = new KeyboardButton();
        KeyboardButton three = new KeyboardButton();

        one.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("1")).setColor(KeyboardButtonColor.POSITIVE);
        two.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("2"));
        three.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("3"));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> list = new ArrayList<>();
        list.add(one);
        list.add(two);
        list.add(three);
        buttons.add(list);
        keyboard.setOneTime(false);
        keyboard.setButtons(buttons);
        return keyboard;
    }

    public static Keyboard getBoardWithName(String name) {
        Keyboard keyboard = new Keyboard();
        KeyboardButton one = new KeyboardButton();

        one.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel(name));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> list = new ArrayList<>();
        list.add(one);
        buttons.add(list);
        keyboard.setOneTime(true);
        keyboard.setButtons(buttons);
        return keyboard;
    }

    public static Keyboard createPositiveBord() {
        Keyboard keyboard = new Keyboard();
        KeyboardButton yes = new KeyboardButton();

        yes.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("\uD83D\uDC4D\uD83C\uDFFB"));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> list = new ArrayList<>();
        list.add(yes);
        buttons.add(list);
        keyboard.setOneTime(false);
        keyboard.setButtons(buttons);
        return keyboard;

    }

    public static Keyboard createChoiceSexBoard() {
        Keyboard keyboard = new Keyboard();
        KeyboardButton male = new KeyboardButton();
        KeyboardButton female = new KeyboardButton();

        male.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Я парень"));
        female.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Я девушка"));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> list = new ArrayList<>();
        list.add(male);
        list.add(female);
        buttons.add(list);
        keyboard.setOneTime(false);
        keyboard.setButtons(buttons);
        return keyboard;
    }

    public static Keyboard createChoiceSexExpandBoard() {
        Keyboard keyboard = new Keyboard();
        KeyboardButton male = new KeyboardButton();
        KeyboardButton female = new KeyboardButton();
        KeyboardButton any = new KeyboardButton();

        male.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Мужского"));
        female.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Женского"));
        any.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Без разницы"));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> list = new ArrayList<>();
        list.add(male);
        list.add(female);
        list.add(any);
        buttons.add(list);
        keyboard.setOneTime(true);
        keyboard.setButtons(buttons);

        return keyboard;
    }

    public static Keyboard createCityBoard() {
        Keyboard keyboard = new Keyboard();
        KeyboardButton Moscow = new KeyboardButton();
        KeyboardButton Petersburg = new KeyboardButton();
        KeyboardButton another = new KeyboardButton();

        Moscow.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Москва"));
        Petersburg.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Санкт-Петербург"));
        another.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.LOCATION));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> listTop = new ArrayList<>();
        List<KeyboardButton> listBottom = new ArrayList<>();
        listTop.add(Moscow);
        listTop.add(Petersburg);
        listBottom.add(another);
        buttons.add(listTop);
        buttons.add(listBottom);
        keyboard.setOneTime(true);
        keyboard.setButtons(buttons);

        return keyboard;
    }

    public static Keyboard createImageBoard() {
        Keyboard keyboard = new Keyboard();
        KeyboardButton profilePhoto = new KeyboardButton();
        KeyboardButton customPhoto = new KeyboardButton();

        profilePhoto.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Загрузить фото профиля"));
        customPhoto.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Выбрать другое фото"));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> listTop = new ArrayList<>();
        List<KeyboardButton> listBottom = new ArrayList<>();
        listTop.add(profilePhoto);
        listBottom.add(customPhoto);

        buttons.add(listTop);
        buttons.add(listBottom);
        keyboard.setOneTime(false);
        keyboard.setButtons(buttons);

        return keyboard;
    }

    public static Keyboard createImageBoard2() {
        Keyboard keyboard = new Keyboard();

        KeyboardButton currentPhoto = new KeyboardButton();
        currentPhoto.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Оставить текущее"));
        List<List<KeyboardButton>> buttons = new ArrayList<>();
        buttons.add(getImageBoard().getButtons().get(0));
        buttons.add(getImageBoard().getButtons().get(1));
        List<KeyboardButton> list = new ArrayList<>();
        list.add(currentPhoto);
        buttons.add(list);
        keyboard.setButtons(buttons);

        return keyboard;
    }

    public static Keyboard createGoBackBoard() {
        Keyboard keyboard = new Keyboard();
        KeyboardButton goBack = new KeyboardButton();

        goBack.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Вернуться назад"));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> list = new ArrayList<>();
        list.add(goBack);

        buttons.add(list);
        keyboard.setOneTime(true);
        keyboard.setButtons(buttons);

        return keyboard;
    }

    public static Keyboard createCheckFormBoard() {
        Keyboard keyboard = new Keyboard();
        KeyboardButton rewriteForm = new KeyboardButton();
        KeyboardButton changePhoto = new KeyboardButton();
        KeyboardButton changeText = new KeyboardButton();
        KeyboardButton searchNeighbor = new KeyboardButton();

        rewriteForm.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("1"));
        changePhoto.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("2"));
        changeText.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("3"));
        searchNeighbor.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("4")).setColor(KeyboardButtonColor.POSITIVE);

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> list = new ArrayList<>();
        list.add(rewriteForm);
        list.add(changePhoto);
        list.add(changeText);
        list.add(searchNeighbor);

        buttons.add(list);
        keyboard.setOneTime(false);
        keyboard.setButtons(buttons);

        return keyboard;
    }

    public static Keyboard createSkipBoard() {
        Keyboard keyboard = new Keyboard();
        KeyboardButton skip = new KeyboardButton();

        skip.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Пропустить"));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> list = new ArrayList<>();
        list.add(skip);

        buttons.add(list);
        keyboard.setOneTime(true);
        keyboard.setButtons(buttons);

        return keyboard;
    }

    public static Keyboard createLikeDislikeBoard() {
        Keyboard keyboard = new Keyboard();
        KeyboardButton like = new KeyboardButton();
        KeyboardButton dislike = new KeyboardButton();
        KeyboardButton complaint = new KeyboardButton();
        KeyboardButton back = new KeyboardButton();

        like.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("\uD83D\uDC4D\uD83C\uDFFB")).setColor(KeyboardButtonColor.POSITIVE);
        dislike.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("\uD83D\uDC4E\uD83C\uDFFB")).setColor(KeyboardButtonColor.NEGATIVE);
        complaint.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Жалоба"));
        back.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("В меню"));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> list = new ArrayList<>();
        list.add(like);
        list.add(dislike);
        list.add(complaint);
        list.add(back);

        buttons.add(list);
        keyboard.setOneTime(false);
        keyboard.setButtons(buttons);

        return keyboard;
    }

    public static Keyboard createNextOrMenuBoard() {
        Keyboard keyboard = new Keyboard();
        KeyboardButton next = new KeyboardButton();
        KeyboardButton menu = new KeyboardButton();

        next.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Искать дальше")).setColor(KeyboardButtonColor.POSITIVE);
        menu.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("В главное меню"));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> listTop = new ArrayList<>();
        List<KeyboardButton> listBottom = new ArrayList<>();
        listTop.add(next);
        listBottom.add(menu);

        buttons.add(listTop);
        buttons.add(listBottom);
        keyboard.setOneTime(false);
        keyboard.setButtons(buttons);

        return keyboard;

    }

    public static Keyboard createYesOrBackBoard(){
        Keyboard keyboard = new Keyboard();
        KeyboardButton yes = new KeyboardButton();
        KeyboardButton back = new KeyboardButton();

        yes.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Да"));
        back.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Назад"));

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> listTop = new ArrayList<>();
        List<KeyboardButton> listBottom = new ArrayList<>();
        listTop.add(yes);
        listBottom.add(back);

        buttons.add(listTop);
        buttons.add(listBottom);
        keyboard.setOneTime(true);
        keyboard.setButtons(buttons);

        return keyboard;
    }

    public static Keyboard createYesOrNoBoard(){
        Keyboard keyboard = new Keyboard();
        KeyboardButton yes = new KeyboardButton();
        KeyboardButton back = new KeyboardButton();

        yes.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Да")).setColor(KeyboardButtonColor.POSITIVE);
        back.setAction(new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT).setLabel("Нет")).setColor(KeyboardButtonColor.NEGATIVE);

        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> list = new ArrayList<>();
        list.add(yes);
        list.add(back);

        buttons.add(list);
        keyboard.setOneTime(false);
        keyboard.setButtons(buttons);

        return keyboard;
    }

    public static Keyboard getDefaultBoard() {
        return defaultBoard;
    }

    public static Keyboard getPositiveBoard() {
        return positiveBoard;
    }

    public static Keyboard getChoiceSexBoard() {
        return choiceSexBoard;
    }

    public static Keyboard getChoiceSexExpandBoard() {
        return choiceSexExpandBoard;
    }

    public static Keyboard getCityBoard() {
        return cityBoard;
    }

    public static Keyboard getImageBoard() {
        return imageBoard;
    }

    public static Keyboard getImageBoard2() {
        return imageBoard2;
    }

    public static Keyboard getGoBackBoard() {
        return goBackBoard;
    }

    public static Keyboard getCheckFormBoard() {
        return checkFormBoard;
    }

    public static Keyboard getSkipBoard() {
        return skipBoard;
    }

    public static Keyboard getLikeDislikeBoard() {
        return likeDislikeBoard;
    }

    public static Keyboard getNextOrMenuBoard() {
        return nextOrMenuBoard;
    }

    public static Keyboard getYesOrBackBoard() {
        return yesOrBackBoard;
    }

    public static Keyboard getYesOrNoBoard() {
        return YesOrNoBoard;
    }
}