package com.jiawei.httplib.builder;

import com.jiawei.httplib.callback.ICallback;
import com.jiawei.httplib.okhttp.CountingRequestBody;
import com.jiawei.httplib.request.RequestCall;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by jiawei on 2017/6/22.
 *
 * postBuilder需要在Request加上callback参数
 * callback需要在{@link RequestCall}才有，因此需要加一层传过去
 */

public class PostBuilder extends BaseBuilder {

    private File file;

    public BaseBuilder file(File file){
        this.file=file;
        return this;
    }

    @Override
    public Request createRequest(final ICallback callback) {

        RequestBody formBody;
        if (file == null) {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder, params);
            formBody = builder.build();
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addParams(builder, params);

            RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())),file);
            builder.addFormDataPart(file.getName(), file.getName(), fileBody);
            formBody = builder.build();
        }

        CountingRequestBody countingRequestBody = new CountingRequestBody(formBody, new CountingRequestBody.Listener()
        {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength)
            {
                // TODO: 切换到主线程
                callback.onProgress(bytesWritten * 1.0f / contentLength);
            }
        });

        Request request = new Request.Builder()
                .url(url)
                .post(countingRequestBody)
                .build();

        return request;
    }

    private static void addParams(MultipartBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private static void addParams(FormBody.Builder builder, Map<String, String> params) {

        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }


    private static String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try
        {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}