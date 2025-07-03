package com.jcoder.base64;

import android.app.AlertDialog;
import android.content.Context;

public class HelpDialog {

    private static final String MESSAGE = "Base64 - v1.0.2 (3)\n" +
            "Developed by Kaung Khant Kyaw.\n" +
            "\n" +
            "Supported Encodings:\n\n" +
            "- Base64\n" +
            "- Base64 URL\n" +
            "- Base32\n" +
            "- Base32 Hex\n" +
            "- Base16\n" +
            "\n\n" +
            "Settings:\n\n" +
            "- Omit padding: Removes \"=\" padding characters.\n\n" +
            "- Ignore case for decoding: Decodes letters regardless of case.\n\n" +
            "- Standard case: Uses default letters for encoding/decoding.\n\n" +
            "- Lowercase: Encodes/decodes with lowercase letters. (Not for Base64/Base64 URL)\n\n" +
            "- Uppercase: Encodes/decodes with uppercase letters. (Not for Base64/Base64 URL)\n\n" +
            "\n" +
            "For more info, visit: https://guava.dev/releases/snapshot-jre/api/docs/com/google/common/io/BaseEncoding.html";

    public static void show(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.menu_help)
                .setMessage(MESSAGE)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

}
