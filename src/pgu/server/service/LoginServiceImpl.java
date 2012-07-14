package pgu.server.service;

import pgu.client.service.LoginService;
import pgu.server.app.AppLog;
import pgu.shared.dto.LoginInfo;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    private final AppLog log = new AppLog();

    @Override
    public LoginInfo getLoginInfo(final String requestUri) {
        final UserService userService = UserServiceFactory.getUserService();
        final LoginInfo loginInfo = new LoginInfo();

        final User user = userService.getCurrentUser();
        if (user != null && userService.isUserAdmin()) {

            loginInfo.setLoggedIn(true);
            loginInfo.setEmailAddress(user.getEmail());
            loginInfo.setNickname(user.getNickname());
            loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));

        } else {
            loginInfo.setLoggedIn(false);
            loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
        }
        log.info(this, "%s", loginInfo);
        return loginInfo;
    }

}
