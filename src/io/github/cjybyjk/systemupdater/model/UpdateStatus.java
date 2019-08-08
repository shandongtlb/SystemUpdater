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

public final class UpdateStatus {
    public static final int UNKNOWN = 0;
    public static final int STARTING = 1;
    public static final int DOWNLOADING = 2;
    public static final int DOWNLOADED = 3;
    public static final int DOWNLOAD_PAUSED = 4;
    public static final int DOWNLOAD_FAILED = 5;
    public static final int VERIFYING = 6;
    public static final int VERIFIED = 7;
    public static final int VERIFICATION_FAILED = 8;
    public static final int INSTALLING = 9;
    public static final int INSTALLED = 10;
    public static final int INSTALLATION_FAILED = 11;
    public static final int INSTALLATION_CANCELLED = 12;
    public static final int INSTALLATION_SUSPENDED = 13;
}
