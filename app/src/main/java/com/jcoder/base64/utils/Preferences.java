package com.jcoder.base64.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.jcoder.base64.app.Encoding;
import com.jcoder.base64.app.EncodingCase;

public class Preferences {

    private static final String KEY_ENCODING = "encoding";
    private static final String KEY_ENCODING_CASE = "encoding_case";
    private static final String KEY_OMIT_PADDING = "omit_padding";
    private static final String KEY_IGNORE_CASE = "ignore_case";
    private static final String KEY_HELP_DIALOG_CODE = "help_dialog_code";

    private final SharedPreferences prefs;

    public Preferences(Context context) {
        prefs = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }

    public void setEncoding(Encoding encoding) {
        prefs.edit().putString(KEY_ENCODING, encoding.getId()).apply();
    }

    public Encoding getEncoding() {
        return Encoding.fromId(prefs.getString(KEY_ENCODING, null));
    }

    public void setEncodingCase(EncodingCase encodingCase) {
        prefs.edit().putString(KEY_ENCODING_CASE, encodingCase.getId()).apply();
    }

    public EncodingCase getEncodingCase() {
        return EncodingCase.fromId(prefs.getString(KEY_ENCODING_CASE, null));
    }

    public void setOmitPaddingEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_OMIT_PADDING, enabled).apply();
    }

    public boolean isOmitPaddingEnabled() {
        return prefs.getBoolean(KEY_OMIT_PADDING, false);
    }

    public void setIgnoreCaseEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_IGNORE_CASE, enabled).apply();
    }

    public boolean isIgnoreCaseEnabled() {
        return prefs.getBoolean(KEY_IGNORE_CASE, false);
    }

    public boolean isHelpShown() {
        String helpDialogCode = prefs.getString(KEY_HELP_DIALOG_CODE, null);
        return TextUtils.equals(helpDialogCode, "1");
    }

    public void setHelpShown() {
        prefs.edit().putString(KEY_HELP_DIALOG_CODE, "1").apply();
    }

}
