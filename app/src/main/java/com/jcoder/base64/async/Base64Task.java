package com.jcoder.base64.async;

import android.os.Handler;
import android.os.Looper;

import com.jcoder.base64.utils.Base64Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Base64Task {

    public enum Type {
        ENCODER,
        DECODER
    }

    public interface Callback {
        void onSuccess(String str);

        void onError(IllegalArgumentException e);
    }

    public static void execute(Type type, String str, Callback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String result = type == Type.ENCODER ? Base64Utils.encode(str) : Base64Utils.decode(str);
                handler.post(() -> callback.onSuccess(result));
            } catch (IllegalArgumentException e) {
                handler.post(() -> callback.onError(e));
            }
        });
    }
}
