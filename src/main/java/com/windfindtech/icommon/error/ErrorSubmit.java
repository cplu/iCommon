package com.windfindtech.icommon.error;

import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPOutputStream;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Error Submit
 * Created by cplu on 2014/8/28.
 */
public class ErrorSubmit {

    public static void sendAppLogs(final File log_file, final WSCallback<BaseResponse> callback){
        sendAppLogs(log_file, null, callback);
    }

    public static void sendAppLogs(final File log_file, final String userFeedback, final WSCallback<BaseResponse> callback){
        Observable.create(new Observable.OnSubscribe<byte[]>() {
            @Override
            public void call(Subscriber<? super byte[]> subscriber) {
                try {
                    FileInputStream fis = new FileInputStream(log_file);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream((int) log_file.length());
                    GZIPOutputStream zos = new GZIPOutputStream(bos);

                    byte[] buffer = new byte[1024];
                    int n;
                    while ((n = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, n);
                        //fileContent.append(new String(buffer, 0, n));
                    }
                    zos.close();
                    final byte[] log_bytes = bos.toByteArray();
                    bos.close();
                    subscriber.onNext(log_bytes);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
        .flatMap(new Func1<byte[], Observable<BaseResponse>>() {
            @Override
            public Observable<BaseResponse> call(byte[] bytes) {
                return WSManager.instance().uploadAppLog(bytes, userFeedback);
            }
        })
        .subscribe(callback);
    }
}
