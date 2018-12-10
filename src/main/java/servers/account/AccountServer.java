package servers.account;

import com.betel.common.Monitor;
import com.betel.config.ServerConfigVo;
import com.betel.servers.forward.ServerClient;
import com.betel.servers.node.NodeServer;
import consts.ServerConfig;
import consts.ServerName;

/**
 * @ClassName: AccountServer
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/3 22:20
 */
public class AccountServer extends NodeServer
{

    public AccountServer(ServerConfigVo serverConfig,  Monitor monitor)
    {
        super(serverConfig, monitor);
    }

    public static void main(String[] args) throws Exception
    {
        ServerConfigVo accountSrvCfg = ServerConfig.getServerConfig(ServerName.ACCOUNT_SERVER);
        ServerConfigVo centerSrvCfg = ServerConfig.getServerConfig(ServerName.BALANCE_SERVER);
        AccountMonitor mnt = new AccountMonitor(accountSrvCfg);
        AccountServer server = new AccountServer(accountSrvCfg, mnt);
        server.setCenterServerCfg(centerSrvCfg);
        server.setServerClient(new ServerClient(centerSrvCfg, mnt));
        server.run();
    }
}
