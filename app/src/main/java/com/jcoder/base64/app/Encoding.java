package com.jcoder.base64.app;

import com.google.common.io.BaseEncoding;
import com.jcoder.base64.R;

public enum Encoding {

    Base64("base64", R.string.encoding_base64, R.id.encoding_base64),
    Base64Url("base64_url", R.string.encoding_base64_url, R.id.encoding_base64_url),
    Base32("base32", R.string.encoding_base32, R.id.encoding_base32),
    Base32Hex("base32_hex", R.string.encoding_base32_hex, R.id.encoding_base32_hex),
    Base16("base16", R.string.encoding_base16, R.id.encoding_base16);

    private final String id;
    private final int stringResId;
    private final int menuItemId;

    Encoding(String id, int stringResId, int menuItemId) {
        this.id = id;
        this.stringResId = stringResId;
        this.menuItemId = menuItemId;
    }

    public String getId() {
        return this.id;
    }

    public int getStringResId() {
        return stringResId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public static Encoding fromId(String id) {
        for (Encoding encoding : Encoding.values()) {
            if (encoding.id.equalsIgnoreCase(id)) {
                return encoding;
            }
        }
        return Encoding.Base64;
    }

    public static Encoding fromMenuItemId(int id) {
        for (Encoding encoding : Encoding.values()) {
            if (encoding.menuItemId == id) {
                return encoding;
            }
        }
        return Encoding.Base64;
    }

    public static boolean hasMenuItemId(int id) {
        for (Encoding encoding : Encoding.values()) {
            if (encoding.menuItemId == id) {
                return true;
            }
        }
        return false;
    }

    public BaseEncoding getBaseEncoding() {
        BaseEncoding encoding;

        switch (this) {
            case Base16:
                encoding = BaseEncoding.base16();
                break;
            case Base32:
                encoding = BaseEncoding.base32();
                break;
            case Base32Hex:
                encoding = BaseEncoding.base32Hex();
                break;
            case Base64Url:
                encoding = BaseEncoding.base64Url();
                break;
            case Base64:
            default:
                encoding = BaseEncoding.base64();
                break;
        }

        return encoding;
    }

}