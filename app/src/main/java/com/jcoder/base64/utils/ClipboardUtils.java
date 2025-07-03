package com.jcoder.base64.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtils {

    public static void copyText(Context context, String str) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", str);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static String getText(Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            ClipData.Item firstItem = clipData.getItemAt(0);
            if (firstItem.getText() != null) return firstItem.coerceToText(context).toString();
            else return "";
        } else return "";
    }

}
