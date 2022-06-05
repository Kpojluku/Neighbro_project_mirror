package VK;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import core.Keyboards;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VKServer {
    public static int maxMsgId = -1;

    public static VKCore vkCore;

    static {
        try {
            vkCore = new VKCore();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, ApiException {
        Keyboards.createKeyboards();
        runServer();
    }

    public static void runServer() throws NullPointerException, ApiException, InterruptedException {
        System.out.println("Running server...");
        while (true) {
            Thread.sleep(300);
            try {
                List<Message> messages = vkCore.getMessages();
                if (messages != null && !messages.isEmpty()) {

                    for (Message message : messages) {

                        if (message != null && !message.isOut()) {

                            int messageId = message.getId();
                            if (messageId > maxMsgId) {
                                maxMsgId = messageId;
                            }

                            ExecutorService exec = Executors.newCachedThreadPool();
                            exec.execute(new Messenger(message));
                        }
                    }
                }
            } catch (ClientException e) {
                System.out.println("Возникли проблемы");
                final int RECONNECT_TIME = 10000;
                System.out.println("Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");
                Thread.sleep(RECONNECT_TIME);

            }
        }
    }
}
