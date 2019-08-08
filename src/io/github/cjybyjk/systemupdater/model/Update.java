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
package io.github.cjybyjk.systemupdater.model;

import java.io.File;

public class Update implements UpdateInfo {

    private int mStatus = UpdateStatus.UNKNOWN;
    private File mFile;
    private String mName;
    private String mDescription;
    private String mDownloadUrl;
    private String mFileSHA1;
    private long mTimestamp;
    private String mType;
    private String mVersion;
    private long mRequirement;
    private long mFileSize;
    private int mDownloadProgress;
    private int mInstallProgress;
    private OnStatusChangedCallback mOnStatusChangedCallback;
    private OnProgressChangedCallback mOnProgressChangedCallback;
    public Update() {
    }

    public Update(UpdateInfo update) {
        mName = update.getName();
        mDescription = update.getDescription();
        mDownloadUrl = update.getDownloadUrl();
        mTimestamp = update.getTimestamp();
        mType = update.getType();
        mVersion = update.getVersion();
        mFileSHA1 = update.getFileSHA1();
        mFileSize = update.getFileSize();
        mStatus = update.getStatus();
        mFile = update.getFile();
        mDownloadProgress = update.getDownloadProgress();
    }

    @Override
    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
        if (mOnStatusChangedCallback != null) {
            mOnStatusChangedCallback.onStatusChangedCallback(status);
        }
    }

    @Override
    public File getFile() {
        return mFile;
    }

    public void setFile(File file) {
        mFile = file;
    }

    @Override
    public int getInstallProgress() {
        return mInstallProgress;
    }

    public void setInstallProgress(int progress) {
        mInstallProgress = progress;
    }

    @Override
    public int getDownloadProgress() {
        return mDownloadProgress;
    }

    public void setDownloadProgress(int progress, long speed) {
        mDownloadProgress = progress;
        if (mOnProgressChangedCallback != null) {
            mOnProgressChangedCallback.onProgressChangedCallback(progress, speed);
        }
    }

    @Override
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }

    @Override
    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    @Override
    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        mVersion = version;
    }

    @Override
    public long getRequirement() {
        return mRequirement;
    }

    public void setRequirement(long timestamp) {
        mRequirement = timestamp;
    }

    @Override
    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    @Override
    public long getFileSize() {
        return mFileSize;
    }

    public void setFileSize(long fileSize) {
        mFileSize = fileSize;
    }

    @Override
    public String getFileSHA1() {
        return mFileSHA1;
    }

    public void setFileSHA1(String fileSHA1) {
        mFileSHA1 = fileSHA1;
    }

    public void setOnStatusChangedCallback(OnStatusChangedCallback tCallback) {
        mOnStatusChangedCallback = tCallback;
    }

    public void setOnProgressChangedCallback(OnProgressChangedCallback tCallback) {
        mOnProgressChangedCallback = tCallback;
    }

    public interface OnStatusChangedCallback {
        void onStatusChangedCallback(int status);
    }

    public interface OnProgressChangedCallback {
        void onProgressChangedCallback(int progress, long speed);
    }

}
