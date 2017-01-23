package com.windfindtech.icommon.jsondata.enumtype;

/**
 * 记录信息的类型
 *
 * @author cplu
 */
public enum UsageType {
    getPassword("getPassword"),
    login("logIn"),
    loginSuccess("loginSuccess"),
    loginFailure("loginFailure"),

    /// for v7 and later
    portalLogin("portalLogin"),
    wsLogin("wsLogin"),

    unknown("unknown"),
    ;
    String type;

    UsageType(String type) {
        this.type = type;
    }
}
