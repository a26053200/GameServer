package servers.gate;

import com.betel.common.Monitor;
import com.betel.config.ServerConfigVo;
import com.betel.servers.forward.ServerClient;
import com.betel.servers.http.HttpServer;
import com.betel.servers.http.HttpServerMonitor;
import consts.ServerConfig;
import consts.ServerName;

/**
 * @ClassName: HttpGateServer
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/6 0:00
 */
public class HttpGateServer extends HttpServer
{

    public HttpGateServer(ServerConfigVo serverConfig, Monitor monitor)
    {
        super(serverConfig, monitor);
    }

    public static void main(String[] args) throws Exception
    {
        ServerConfigVo httpGateSrvCfg = ServerConfig.getServerConfig(ServerName.HTTP_GATE_SERVER);
        ServerConfigVo centerSrvCfg = ServerConfig.getServerConfig(ServerName.BALANCE_SERVER);
        HttpServerMonitor mnt = new HttpServerMonitor(httpGateSrvCfg);
        HttpGateServer server = new HttpGateServer(httpGateSrvCfg, mnt);
        server.setCenterServerCfg(centerSrvCfg);
        server.setServerClient(new ServerClient(centerSrvCfg, mnt));
        server.run();
    }
}
