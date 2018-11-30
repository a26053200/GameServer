import com.betel.common.Debug;
import com.betel.servers.balance.BalanceServer;

/**
 * @ClassName: TestBalanceServer
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/11/30 23:52
 */
public class TestBalanceServer extends BalanceServer
{
    public TestBalanceServer()
    {
        super();
    }

    public static void main(String[] args) throws Exception
    {
        Debug.initLog("[TestBalanceServer]", "src/main/resources/log4j_balance_server.properties");
        new BalanceServer().run();
    }
}
