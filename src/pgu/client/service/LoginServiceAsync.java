package pgu.client.service;

import pgu.shared.dto.LoginInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

    void getLoginInfo(String url, AsyncCallback<LoginInfo> callback);

}
