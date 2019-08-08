/*
 * Copyright (C) 2017 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.cjybyjk.systemupdater.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {

    private static final String TAG = "FileUtils";
    // 为App授予读写存储权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void copyFile(File sourceFile, File destFile, ProgressCallBack progressCallBack)
            throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
             FileChannel destChannel = new FileOutputStream(destFile).getChannel()) {
            if (progressCallBack != null) {
                ReadableByteChannel readableByteChannel = new CallbackByteChannel(sourceChannel,
                        sourceFile.length(), progressCallBack);
                destChannel.transferFrom(readableByteChannel, 0, sourceChannel.size());
            } else {
                destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not copy file", e);
            if (destFile.exists()) {
                destFile.delete();
            }
            throw e;
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        copyFile(sourceFile, destFile, null);
    }

    public static String calcSHA1(File sourceFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        InputStream is;
        try {
            is = new FileInputStream(sourceFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "Exception while getting FileInputStream");
            return null;
        }
        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to process file for ");
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Exception on closing inputstream:");
            }
        }
    }

    public static String readToString(File file) {
        String encoding = "UTF-8";
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    public interface ProgressCallBack {
        void update(int progress);
    }

    private static class CallbackByteChannel implements ReadableByteChannel {
        private ProgressCallBack mCallback;
        private long mSize;
        private ReadableByteChannel mReadableByteChannel;
        private long mSizeRead;
        private int mProgress;

        private CallbackByteChannel(ReadableByteChannel readableByteChannel, long expectedSize,
                                    ProgressCallBack callback) {
            this.mCallback = callback;
            this.mSize = expectedSize;
            this.mReadableByteChannel = readableByteChannel;
        }

        @Override
        public void close() throws IOException {
            mReadableByteChannel.close();
        }

        @Override
        public boolean isOpen() {
            return mReadableByteChannel.isOpen();
        }

        @Override
        public int read(ByteBuffer bb) throws IOException {
            int read;
            if ((read = mReadableByteChannel.read(bb)) > 0) {
                mSizeRead += read;
                int progress = mSize > 0 ? Math.round(mSizeRead * 100.f / mSize) : -1;
                if (mProgress != progress) {
                    mCallback.update(progress);
                    mProgress = progress;
                }
            }
            return read;
        }
    }
}
