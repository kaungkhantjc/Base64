package com.jcoder.base64.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class Clipboard {

    public static void copyText(Context context, String str) {
        try {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("text", str);
            clipboardManager.setPrimaryClip(clipData);
        } catch (Exception ignored) {
        }
    }

    public static String getText(Context context) {
        try {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData == null || clipData.getItemCount() == 0) return null;

            ClipData.Item firstItem = clipData.getItemAt(0);
            return firstItem.coerceToText(context).toString();
        } catch (Exception ignored) {
            return null;
        }
    }

}
