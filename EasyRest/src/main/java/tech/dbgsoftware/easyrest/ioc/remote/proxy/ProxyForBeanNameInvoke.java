package tech.dbgsoftware.easyrest.ioc.remote.proxy;

import tech.dbgsoftware.easyrest.actors.remote.RemoteRequestUtil;
import tech.dbgsoftware.easyrest.utils.JsonTranslationUtil;
import tech.dbgsoftware.easyrest.utils.LogUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyForBeanNameInvoke implements InvocationHandler {

    private String beanName;

    private long connectionMillis;

    private long resultMillis;

    public ProxyForBeanNameInvoke(String beanName) {
        this.beanName = beanName;
        connectionMillis = -1;
        resultMillis = -1;
    }

    public ProxyForBeanNameInvoke(String beanName, long connectionMillis, long resultMillis) {
        this.beanName = beanName;
        this.connectionMillis = connectionMillis;
        this.resultMillis = resultMillis;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            Object result = RemoteRequestUtil.createRemoteRequest(method, args, beanName, connectionMillis, resultMillis);
            if (method.getReturnType().getName().equalsIgnoreCase(Void.class.getSimpleName())) {
                return null;
            }
            return JsonTranslationUtil.fromJson(String.valueOf(result), method.getGenericReturnType());
        } catch (Exception e){
            LogUtils.error(String.format("Method %s invoke failed. Cause %s", method.getName(), e.getMessage()), e);
            return null;
        }
    }
}