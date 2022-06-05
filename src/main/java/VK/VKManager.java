package VK;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;

import java.util.Random;

public class VKManager {
    public static VKCore vkCore;
    private static VKManager vkManagerInstance;

    public static synchronized VKManager createVKManager() {
        if (vkManagerInstance == null) {
            vkManagerInstance = new VKManager();
        }
        return vkManagerInstance;
    }


    static {
        try {
            vkCore = new VKCore();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg, int peerId, Keyboard keyboard) {
        Random random = new Random();
        try {
            if (keyboard == null) {
                vkCore.getVk().messages().send(vkCore.getActor()).peerId(peerId).randomId(random.nextInt()).message(msg).execute();
            } else {
                vkCore.getVk().messages().send(vkCore.getActor()).peerId(peerId).randomId(random.nextInt()).keyboard(keyboard).message(msg).execute();
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageAndPhoto(String msg, String photo, int peerId) {

        Random random = new Random();
        try {
            vkCore.getVk().messages().send(vkCore.getActor()).peerId(peerId).randomId(random.nextInt()).message(msg).attachment(photo).execute();

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageAndPhotoAndKeyboard(String msg, String photo, int peerId, Keyboard keyboard) {
        Random random = new Random();
        try {
            vkCore.getVk().messages().send(vkCore.getActor()).peerId(peerId).randomId(random.nextInt()).message(msg).attachment(photo).keyboard(keyboard).execute();

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }


    /**
     * Обращается к VK API и получает объект, описывающий пользователя.
     *
     * @param id идентификатор пользователя в VK
     * @return информацию о пользователе
     */
    public GetResponse getUserInfo(int id) {
        try {
            return vkCore.getVk().users()
                    .get(vkCore.getActor())
                    .userIds(String.valueOf(id))
                    .fields(Fields.PHOTO_ID)
                    .execute().get(0);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
