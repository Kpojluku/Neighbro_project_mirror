package VK;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class VKCore {
    private VkApiClient vk;
    private static int ts;
    private GroupActor actor;
    private static int maxMsgId = -1;
    private static final Logger logger = LogManager.getLogger(VKCore.class);

    public VKCore() throws ClientException, ApiException {

        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        // Загрузка конфигураций

        Properties prop = new Properties();
        int groupId;
        String accessToken;
        try {
            prop.load(new FileInputStream("src/main/resources/vkconfig.properties"));
            groupId = Integer.parseInt(prop.getProperty("groupId"));
            accessToken = prop.getProperty("accessToken");
            actor = new GroupActor(groupId, accessToken);

            ts = vk.messages().getLongPollServer(actor).execute().getTs();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке файла конфигурации");
        }

    }

    public GroupActor getActor() {
        return actor;
    }

    public VkApiClient getVk() {
        return vk;
    }

    public List<Message> getMessages() throws ClientException, ApiException {
        MessagesGetLongPollHistoryQuery eventsQuery = vk.messages()
                .getLongPollHistory(actor)
                .ts(ts);
                /*
                messageId - максимально полученный ID, нужен, чтобы не было ошибки 10 internal server error,
                который является ограничением в API VK. В случае, если ts слишком старый (больше суток),
                а max_msg_id не передан, метод может вернуть ошибку 10 (Internal server error).
                 */
        if (maxMsgId > 0) {
            eventsQuery.maxMsgId(maxMsgId);
        }
        List<Message> messages = eventsQuery
                .execute()
                .getMessages()
                .getItems();

        if (!messages.isEmpty()) {
            try {
                ts = vk.messages()
                        .getLongPollServer(actor)
                        .execute()
                        .getTs();
                return messages;
            } catch (ClientException e) {
                logger.error("ClientException");
                e.printStackTrace();
            }
        }
        return null;

    }

}
