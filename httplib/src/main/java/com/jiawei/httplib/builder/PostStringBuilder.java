package com.jiawei.httplib.builder;

import com.jiawei.httplib.callback.ICallback;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by jiawei on 2017/6/27.
 */

public class PostStringBuilder extends BaseBuilder {

    private String mcontent;
    private MediaType mMediaType;

    //todo 增加content的非空判断
    public PostStringBuilder content(String content)
    {
        mcontent = content;
        return this;
    }

    //todo 给mediaType默认值 在后续提取到request中时

    public PostStringBuilder mediaType(MediaType mediaType)
    {
        mMediaType = mediaType;
        return this;
    }

    @Override
    public Request createRequest(ICallback callback) {
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        Headers header = mHeaderBuild.build();
        RequestBody requestBody = RequestBody.create(mMediaType, mcontent);
        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .headers(header)
                .post(requestBody)
                .build();

        return request;
    }
}