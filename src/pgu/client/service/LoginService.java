package pgu.client.service;

import pgu.shared.dto.LoginInfo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {

    LoginInfo getLoginInfo(String url);

}
