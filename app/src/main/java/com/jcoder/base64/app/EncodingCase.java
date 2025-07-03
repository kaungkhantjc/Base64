package com.jcoder.base64.app;

import com.jcoder.base64.R;

public enum EncodingCase {
    Standard("standard", R.id.setting_case_standard),
    Lowercase("lowercase", R.id.setting_case_lower),
    Uppercase("uppercase", R.id.setting_case_upper);

    private final String id;
    private final int menuItemId;

    EncodingCase(String id, int menuItemId) {
        this.id = id;
        this.menuItemId = menuItemId;
    }

    public String getId() {
        return id;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public static EncodingCase fromId(String id) {
        for (EncodingCase encodingCase : EncodingCase.values()) {
            if (encodingCase.id.equalsIgnoreCase(id)) {
                return encodingCase;
            }
        }
        return EncodingCase.Standard;
    }

    public static EncodingCase fromMenuItemId(int id) {
        for (EncodingCase encodingCase : EncodingCase.values()) {
            if (encodingCase.menuItemId == id) {
                return encodingCase;
            }
        }
        return EncodingCase.Standard;
    }

    public static boolean hasMenuItemId(int id) {
        for (EncodingCase encodingCase : EncodingCase.values()) {
            if (encodingCase.menuItemId == id) {
                return true;
            }
        }
        return false;
    }

}
