package servers.account;

import com.alibaba.fastjson.JSONObject;
import com.betel.asd.Business;
import com.betel.consts.FieldName;
import com.betel.session.Session;
import com.betel.utils.IdGenerator;
import com.betel.utils.JwtHelper;
import consts.Action;
import consts.ReturnCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import servers.account.beans.Account;

import java.util.List;

/**
 * @ClassName: AccountBusiness
 * @Description: TODO
 * @Author: zhengnan
 * @Date: 2018/12/5 23:04
 */
public class AccountBusiness extends Business<Account>
{
    private class Field
    {
        static final String USERNAME = "username";
        static final String PASSWORD = "password";
    }

    final static Logger logger = LogManager.getLogger(AccountBusiness.class);

    private static final String ViceKey = "username";
    @Override
    public String getViceKey()
    {
        return ViceKey;
    }

    @Override
    public void Handle(Session session, String method)
    {
        switch (method)
        {
            case Action.ACCOUNT_LOGIN:
                accountLogin(session);
                break;
            case Action.ACCOUNT_REGISTER:
                accountRegister(session);
                break;
            default:
                break;
        }
    }

    private void accountLogin(Session session)
    {
        String username = session.getRecvJson().getString(Field.USERNAME);
        String password = session.getRecvJson().getString(Field.PASSWORD);

        List<Account> allAccount = service.getViceEntrys(username);

        if (allAccount.size() > 0)
        {//已经注册过
            Account account = allAccount.get(0);
            if (account.getPassword().equals(password)) {//密码正确，登陆成功
                try {
                    logger.info(String.format("用户:%s 登陆成功", username));
                    rspdMessage(session,ReturnCode.Login_success);
                    onLoginSuccess(session, account.getId());
                    updateEntry(session);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    rspdMessage(session,ReturnCode.Error_unknown);
                }
            } else {//密码错误，登陆失败
                logger.info(String.format("用户:%s 登陆失败", username));
                rspdMessage(session,ReturnCode.Wrong_password);
            }
        }else{//还未注册过
            logger.info(String.format("用户:%s 登陆失败", username));
            rspdMessage(session,ReturnCode.Register_not_yet);
        }
    }

    private void onLoginSuccess(Session session, String account_id)
    {
        //游戏服务器的网关地址列表 json
        JSONObject gameServerJson = JSONObject.parseObject(monitor.getDB().get("GameServer"));
        JSONObject rspdJson = new JSONObject();
        rspdJson.put("action", Action.ACCOUNT_LOGIN);
        rspdJson.put("aid", account_id);
        rspdJson.put("token", JwtHelper.createJWT(account_id));
        rspdJson.put("srvList", gameServerJson);
        action.rspdClient(session, rspdJson);
    }

    private void accountRegister(Session session)
    {
        String username = session.getRecvJson().getString(Field.USERNAME);
        String password = session.getRecvJson().getString(Field.PASSWORD);

        List<Account> allAccount = service.getViceEntrys(username);
        if (allAccount.size() > 0)
        {//已经注册过
            //Account account = allAccount.get(0);
            rspdMessage(session,ReturnCode.Error_already_exits);
        }else{
            String nowTime = now();
            Account account = new Account();
            account.setId(Long.toString(IdGenerator.getInstance().nextId()));
            account.setUsername(username);
            account.setPassword(password);
            account.setRegisterTime(nowTime);
            account.setLastLoginTime(nowTime);
            account.setLastLoginIp(session.getContext().channel().remoteAddress().toString());
            service.addEntry(account);
            rspdMessage(session,ReturnCode.Register_success);
        }
    }

    private void rspdMessage(Session session, String msg)
    {
        JSONObject rspdJson = new JSONObject();
        rspdJson.put(FieldName.ACTION, Action.RETURN_MESSAGE);
        rspdJson.put(FieldName.MSG, msg);
        action.rspdClient(session, rspdJson);
    }

    @Override
    public Account updateEntry(Session session)
    {
        Account account = service.getEntryById(session.getRecvJson().getString(FieldName.ID));
        if(account != null)
        {
            account.setLastLoginTime(now());
            account.setLastLoginIp(session.getContext().channel().remoteAddress().toString());
        }
        return account;
    }
}
