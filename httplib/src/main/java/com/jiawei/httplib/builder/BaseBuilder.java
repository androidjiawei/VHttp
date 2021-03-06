package com.jiawei.httplib.builder;


import com.jiawei.httplib.callback.ICallback;
import com.jiawei.httplib.request.BaseRequest;
import com.jiawei.httplib.request.CacheMode;
import com.jiawei.httplib.request.RequestCall;

import java.util.Map;
import java.util.stream.DoubleStream;

/**
 * Created by jiawei on 2017/6/21.
 * 这里引入泛型解决了 使用Builder链式调用的顺序问题
 * 有可能父类的return this 调不到子类的方法
 */

public abstract class BaseBuilder<T extends BaseBuilder > {

    String url;
    Object tag;
    Map<String, String> headers;
    Map<String, String> params;
    CacheMode mMode;
    String cacheKey;

    public T url(String url){
        this.url=url;
        return (T)this;
    }

    public T tag(Object tag){
        this.tag=tag;
        return (T)this;
    }

    public T params(Map<String, String> params){
        this.params=params;
        return (T)this;
    }

    public T header(Map<String, String> headers){
        this.headers=headers;
        return (T)this;
    }

    public T cache(CacheMode mode){
        this.mMode=mode;
        return (T)this;
    }

    public T cacheKey(String cacheKey){
        this.cacheKey=cacheKey;
        return (T)this;
    }

    public RequestCall build() {
        return new RequestCall(this);
    }

    public abstract BaseRequest createRequest(ICallback callback);
}
