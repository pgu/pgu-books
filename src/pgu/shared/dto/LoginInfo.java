package pgu.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LoginInfo implements IsSerializable {

    private boolean loggedIn;
    private String  loginUrl;
    private String  logoutUrl;
    private String  emailAddress;
    private String  nickname;

    @Override
    public String toString() {
        return "LoginInfo [loggedIn=" + loggedIn + ", loginUrl=" + loginUrl + ", logoutUrl=" + logoutUrl
                + ", emailAddress=" + emailAddress + ", nickname=" + nickname + "]";
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(final boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(final String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(final String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }
}
