package pgu.server.utils;

import com.google.appengine.api.utils.SystemProperty;

public class AppUtils {

    public boolean isVoid(final String str) {
        return null == str || str.trim().isEmpty();
    }

    public boolean isEnvProd() {
        return SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
    }

}
