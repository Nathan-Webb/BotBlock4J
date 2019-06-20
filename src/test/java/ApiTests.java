import com.nathanwebb.botblock4j.BlockAuth;
import com.nathanwebb.botblock4j.BotBlockRequests;
import com.nathanwebb.botblock4j.exceptions.EmptyResponseException;
import com.nathanwebb.botblock4j.exceptions.FailedToSendException;
import com.nathanwebb.botblock4j.exceptions.RateLimitedException;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ApiTests {
    private BlockAuth blockAuth;
    @Test
    public void testPostRequest(){
        try {

            BufferedReader botListTokenReader = new BufferedReader(new FileReader("testing" + File.separator + "bfd_token.txt"));
            String bfdToken = botListTokenReader.readLine();
            botListTokenReader.close();

            blockAuth = new BlockAuth();
            blockAuth.setListAuthToken("botsfordiscord.com",  bfdToken);
            BotBlockRequests.postGuilds("460545687592108032",300, blockAuth);
        } catch (Exception | FailedToSendException | EmptyResponseException | RateLimitedException e){
            e.printStackTrace();
        }
    }
}
