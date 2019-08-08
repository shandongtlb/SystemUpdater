package io.github.cjybyjk.systemupdater.helper;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.github.cjybyjk.systemupdater.downloader.DownloadClient;
import io.github.cjybyjk.systemupdater.model.Update;

public class UpdatesDownloadHelper {

    private static final String TAG = "UpdateDownloadHelper";
    private Map<String, DownloadClient> mDownloadClientsDict;

    public UpdatesDownloadHelper() {
        mDownloadClientsDict = new HashMap<>();
    }

    public DownloadClient buildDownloadClient(Update update, @Nullable final DownloadListener mDownloadListener) {
        DownloadClient tDownloadClient;
        DownloadClient.DownloadCallback callback = new DownloadClient.DownloadCallback() {
            @Override
            public void onFailure(final boolean cancelled) {
                if (!cancelled) {
                    Log.e(TAG, "Could not download update");
                }
                if (mDownloadListener != null) {
                    mDownloadListener.onFailureListener(cancelled);
                }
            }

            @Override
            public void onResponse(int statusCode, String url,
                                   DownloadClient.Headers headers) {
                if (mDownloadListener != null) {
                    mDownloadListener.onResponseListener(statusCode, url, headers);
                }
            }

            @Override
            public void onSuccess(File destination) {
                if (mDownloadListener != null) {
                    mDownloadListener.onSuccessListener(destination);
                }
            }

        };

        try {
            tDownloadClient = new DownloadClient.Builder()
                    .setUrl(update.getDownloadUrl())
                    .setDestination(update.getFile())
                    .setDownloadCallback(callback)
                    .setProgressListener(new DownloadClient.ProgressListener() {
                        @Override
                        public void update(final long bytesRead, final long contentLength, final long speed, long eta, boolean done) {
                        if (mDownloadListener != null) {
                            mDownloadListener.onProgressChangedListener(bytesRead, contentLength, speed, eta, done);
                        }
                        }
                    })
                    .build();
            return tDownloadClient;
        } catch (IOException exception) {
            Log.e(TAG, "Could not build download client");
            exception.printStackTrace();
            return null;
        }
    }

    public DownloadClient findDownloadClient(String sha1) {
        return mDownloadClientsDict.get(sha1);
    }

    public void addDownloadClient(String sha1, DownloadClient dlClient) {
        mDownloadClientsDict.put(sha1, dlClient);
    }

    public void addDownload(Update update, @Nullable DownloadListener downloadListener) {
        DownloadClient tDownloadClient = buildDownloadClient(update, downloadListener);
        if (tDownloadClient != null) {
            addDownloadClient(update.getFileSHA1(), tDownloadClient);
        }
    }

    public void removeDownloadClient(String sha1) {
        mDownloadClientsDict.remove(sha1);
    }

    public boolean containsSHA1(String sha1) {
        return mDownloadClientsDict.containsKey(sha1);
    }

    public interface DownloadListener {
        void onFailureListener(boolean cancelled);

        void onResponseListener(int statusCode, String url, DownloadClient.Headers headers);

        void onSuccessListener(File destination);

        void onProgressChangedListener(final long bytesRead, final long contentLength, final long speed, long eta, boolean done);
    }
}
