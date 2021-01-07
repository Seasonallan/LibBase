/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.library.base;

import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Log工具，类似android.util.Log。
 * tag自动产生，格式: customTagPrefix:className.methodName(L:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(L:lineNumber)。
 * Date: 20-3-12
 * Time: 下午12:23
 */
public class LogConsole {

    public static String customTagPrefix = "x_log";

    private LogConsole() {
    }

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(Locale.getDefault(), tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static void d(String content) {
        if (!GlobalContext.isDebug()) return;
        String tag = generateTag();

        Log.d(tag, content);
    }

    public static void d(String content, Throwable tr) {
        if (!GlobalContext.isDebug()) return;
        String tag = generateTag();

        Log.d(tag, content, tr);
    }

    public static void e(String content) {
        if (!GlobalContext.isDebug()) return;
        String tag = generateTag();

        if (TextUtils.isEmpty(content)) {
            Log.e(tag, "empty");
            return;
        }
        Log.e(tag, content);
    }

    public static void e(String content, Throwable tr) {
        if (!GlobalContext.isDebug()) return;
        String tag = generateTag();

        Log.e(tag, content, tr);
    }

    public static void e(String tag, String content, Throwable tr) {
        if (!GlobalContext.isDebug()) return;

        Log.e(tag, content, tr);
    }

    public static void e(String tag, String content) {
        if (!GlobalContext.isDebug()) return;

        Log.e(tag, content);
    }

    public static void i(String content) {
        if (!GlobalContext.isDebug()) return;
        String tag = generateTag();

        Log.i(tag, content);
    }

    public static void i(String content, Throwable tr) {
        if (!GlobalContext.isDebug()) return;
        String tag = generateTag();

        Log.i(tag, content, tr);
    }

    public static void v(String content) {
        if (!GlobalContext.isDebug()) return;
        String tag = generateTag();

        Log.v(tag, content);
    }

    public static void v(String content, Throwable tr) {
        if (!GlobalContext.isDebug()) return;
        String tag = generateTag();

        Log.v(tag, content, tr);
    }

    public static void w(String content) {
        if (!GlobalContext.isDebug()) return;
        String tag = generateTag();

        Log.w(tag, content);
    }

    public static void w(String content, Throwable tr) {
        if (!GlobalContext.isDebug()) return;
        String tag = generateTag();

        Log.w(tag, content, tr);
    }

    public static void w(Throwable tr) {
        if (!GlobalContext.isDebug()) return;
        String tag = generateTag();

        Log.w(tag, tr);
    }


    public static void printEx(String content) {
        if (!GlobalContext.isDebug()) return;
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ":" + content);
    }

    public static void print(String content) {
        if (false){
            return;
        }
        e(content);
        if (!GlobalContext.isDebug()) return;
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ":" + content);
    }
    public static void printNet(String content) {
        if (false){
            return;
        }
        e(content);
        if (!GlobalContext.isDebug()) return;
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ":" + content);
    }
    public static void printEasy(String content) {
        if (false){
            return;
        }
        e(content);
        if (!GlobalContext.isDebug()) return;
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ":" + content);
    }
}
