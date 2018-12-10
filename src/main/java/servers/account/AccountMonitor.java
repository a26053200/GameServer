package servers.account;

import com.betel.config.ServerConfigVo;
import com.betel.servers.action.ImplAction;
import com.betel.servers.node.NodeServerMonitor;
import consts.Bean;
import servers.account.beans.Account;

/**
 * @ClassName: AccountMonitor
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/3 22:20
 */
public class AccountMonitor extends NodeServerMonitor
{
    public AccountMonitor(ServerConfigVo serverCfgInfo)
    {
        super(serverCfgInfo);

        actionMap.put(Bean.ACCOUNT,      new ImplAction<>(this, Bean.ACCOUNT,     Account.class,       new AccountBusiness()));
    }
}
