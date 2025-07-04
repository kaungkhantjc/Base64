package com.jcoder.base64;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Insets;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;

public class EdgeToEdge {

    private static final int STATUS_BARS = 1;
    private static final int NAVIGATION_BARS = 1 << 1;
    private static final int CAPTION_BAR = 1 << 2;

    /**
     * @noinspection BooleanMethodIsAlwaysInverted
     */
    public static boolean isEnforced() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM;
    }

    /**
     * @noinspection deprecation
     */
    private static void setAppearanceLightSystemBars(Window window, boolean isLight) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return;

        WindowInsetsController insetsController = window.getInsetsController();
        if (insetsController == null) return;

        View decorView = window.getDecorView();
        if (isLight) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            insetsController.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            insetsController.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        } else {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            insetsController.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            insetsController.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        }
    }

    private static boolean isDarkMode(Window window) {
        View decorView = window.getDecorView();
        Resources resources = decorView.getResources();
        return (resources.getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    public static void enable(Activity activity) {
        if (!isEnforced()) return;

        Window window = activity.getWindow();
        //noinspection deprecation
        window.setDecorFitsSystemWindows(false);
        //noinspection deprecation
        window.setStatusBarContrastEnforced(false);
        window.setNavigationBarContrastEnforced(true);
        setAppearanceLightSystemBars(window, !isDarkMode(window));
    }

    public static void applyPaddings(View view) {
        if (!isEnforced()) return;

        view.setOnApplyWindowInsetsListener((v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(STATUS_BARS | NAVIGATION_BARS | CAPTION_BAR);
            view.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return insets;
        });
    }

}
