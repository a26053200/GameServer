package servers.balance;

import com.betel.servers.center.CenterMonitor;
import consts.ServerConfig;
import consts.ServerName;

/**
 * @ClassName: BalanceMonitor
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/2 23:53
 */
public class BalanceMonitor extends CenterMonitor
{

    public BalanceMonitor()
    {
        super(ServerConfig.getServerConfig(ServerName.BALANCE_SERVER));
    }
}
