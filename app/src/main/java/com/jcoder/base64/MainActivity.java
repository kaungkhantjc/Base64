package com.jcoder.base64;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.io.BaseEncoding;
import com.jcoder.base64.app.Encoding;
import com.jcoder.base64.app.EncodingCase;
import com.jcoder.base64.app.Type;
import com.jcoder.base64.databinding.ActivityMainBinding;
import com.jcoder.base64.utils.Clipboard;
import com.jcoder.base64.utils.Preferences;
import com.jcoder.base64.utils.Str;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity implements TextWatcher {

    private static final int LARGE_INPUT_THRESHOLD = 10000;

    private ActivityMainBinding binding;
    private SubMenu settingsSubMenu;
    private Preferences prefs;
    private boolean userInputChangedByApp;

    private Encoding encoding = Encoding.Base64;
    private EncodingCase encodingCase = EncodingCase.Standard;
    private boolean isOmitPaddingEnabled = false;
    private boolean isIgnoreCaseEnabled = false;

    // To re-execute the previous user input when setting changes
    private Type previousExecutionType;
    private String previousExecutionUserInput;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = new Preferences(this);
        encoding = prefs.getEncoding();
        encodingCase = prefs.getEncodingCase();
        isOmitPaddingEnabled = prefs.isOmitPaddingEnabled();
        isIgnoreCaseEnabled = prefs.isIgnoreCaseEnabled();
        updateActionBarTitle();

        binding.btnEncode.setOnClickListener(v -> execute(Type.ENCODE, getPreviousExecutionUserInput()));
        binding.btnDecode.setOnClickListener(v -> execute(Type.DECODE, getPreviousExecutionUserInput()));
        binding.edt.addTextChangedListener(this);

        handleTextFromActionSend(getIntent());
        if (!prefs.isHelpShown()) {
            HelpDialog.show(this);
            prefs.setHelpShown();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleTextFromActionSend(intent);
        super.onNewIntent(intent);
    }

    private void handleTextFromActionSend(Intent intent) {
        if (intent != null && TextUtils.equals(intent.getAction(), Intent.ACTION_SEND) && TextUtils.equals(intent.getType(), "text/plain")) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (text != null) setTextToEditor(text);
        }
    }

    private void updateActionBarTitle() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.setTitle(encoding.getStringResId());
    }

    private void updateCaseSettingsMenu() {
        settingsSubMenu.setGroupEnabled(R.id.setting_cases, isCaseSettingsEnabled());
    }

    private boolean isCaseSettingsEnabled() {
        return encoding != Encoding.Base64 && encoding != Encoding.Base64Url;
    }

    private String getPreviousExecutionUserInput() {
        return binding.edt.getText().toString();
    }

    private BaseEncoding getBaseEncoding() {
        BaseEncoding baseEncoding = encoding.getBaseEncoding();

        if (isOmitPaddingEnabled) baseEncoding = baseEncoding.omitPadding();
        if (isIgnoreCaseEnabled) baseEncoding = baseEncoding.ignoreCase();

        if (isCaseSettingsEnabled()) {
            switch (encodingCase) {
                case Lowercase:
                    baseEncoding = baseEncoding.lowerCase();
                    break;

                case Uppercase:
                    baseEncoding = baseEncoding.upperCase();
                    break;
            }
        }
        return baseEncoding;
    }

    private void updateUiForExecution(boolean isExecuting) {
        binding.progress.setVisibility(isExecuting ? View.VISIBLE : View.GONE);
        binding.btnEncode.setEnabled(!isExecuting);
        binding.btnDecode.setEnabled(!isExecuting);
    }

    private void setTextToEditor(String str) {
        userInputChangedByApp = true;
        binding.edt.setText(str);
        binding.edt.setSelection(str.length());
        userInputChangedByApp = false;
    }

    private void reExecute() {
        if (this.previousExecutionType == null || this.previousExecutionUserInput == null || this.previousExecutionUserInput.isEmpty())
            return;
        execute(this.previousExecutionType, this.previousExecutionUserInput);
    }

    private void execute(Type type, String userInput) {
        this.previousExecutionType = type;
        this.previousExecutionUserInput = userInput;

        if (this.previousExecutionUserInput.length() >= LARGE_INPUT_THRESHOLD) {
            updateUiForExecution(true);
        }

        executor.execute(() -> {
            String result = null;
            Exception caughtException = null;

            try {
                BaseEncoding baseEncoding = getBaseEncoding();
                if (type == Type.ENCODE) {
                    byte[] originalBytes = Str.toBytes(userInput);
                    result = baseEncoding.encode(originalBytes);
                } else {
                    byte[] decoded = baseEncoding.decode(userInput);
                    result = Str.bytesToStr(decoded);
                }
            } catch (Exception e) {
                caughtException = e;
            }

            final String finalResult = result;
            Exception finalCaughtException = caughtException;

            mainThreadHandler.post(() -> {
                if (finalCaughtException != null) {
                    if (!isFinishing()) showErrorDialog(finalCaughtException);
                } else {
                    setTextToEditor(finalResult);
                }

                updateUiForExecution(false);
            });

        });
    }

    private void showErrorDialog(Exception e) {
        String message = e.getMessage();
        new AlertDialog.Builder(this)
                .setTitle(e.getClass().getSimpleName())
                .setMessage(message)
                .setNeutralButton(R.string.menu_copy, (dialog, which) -> Clipboard.copyText(this, message))
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void pasteText() {
        String text = Clipboard.getText(this);
        if (text != null) binding.edt.getText().insert(binding.edt.getSelectionEnd(), text);
    }

    private void copyText() {
        Clipboard.copyText(this, getPreviousExecutionUserInput());
        Toast.makeText(this, R.string.msg_text_copied, Toast.LENGTH_SHORT).show();
    }

    private void clearText() {
        binding.edt.getText().clear();
    }

    private void shareText(int titleRes, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(titleRes)));
    }

    private void shareApp() {
        shareText(R.string.menu_share_app, "https://play.google.com/store/apps/details?id=" + getPackageName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(encoding.getMenuItemId()).setChecked(true);

        MenuItem settingsItem = menu.findItem(R.id.menu_settings);
        settingsSubMenu = settingsItem.getSubMenu();
        if (settingsSubMenu == null) return super.onCreateOptionsMenu(menu);

        settingsSubMenu.findItem(R.id.setting_omit_padding).setChecked(isOmitPaddingEnabled);
        settingsSubMenu.findItem(R.id.setting_ignore_case).setChecked(isIgnoreCaseEnabled);
        settingsSubMenu.findItem(encodingCase.getMenuItemId()).setChecked(true);
        updateCaseSettingsMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_copy) copyText();
        else if (itemId == R.id.menu_paste) pasteText();
        else if (itemId == R.id.menu_clear) clearText();
        else if (itemId == R.id.menu_share_text)
            shareText(R.string.menu_share_text, getPreviousExecutionUserInput());
        else if (itemId == R.id.menu_share_app) shareApp();
        else if (itemId == R.id.menu_help) HelpDialog.show(this);

        else if (itemId == R.id.setting_omit_padding) {
            isOmitPaddingEnabled = !isOmitPaddingEnabled;
            item.setChecked(isOmitPaddingEnabled);
            prefs.setOmitPaddingEnabled(isOmitPaddingEnabled);
            reExecute();
        } else if (itemId == R.id.setting_ignore_case) {
            isIgnoreCaseEnabled = !isIgnoreCaseEnabled;
            item.setChecked(isIgnoreCaseEnabled);
            prefs.setIgnoreCaseEnabled(isIgnoreCaseEnabled);
            if (previousExecutionType == Type.DECODE) reExecute();
        } else if (Encoding.hasMenuItemId(itemId)) {
            item.setChecked(true);
            encoding = Encoding.fromMenuItemId(itemId);
            prefs.setEncoding(encoding);
            updateActionBarTitle();
            updateCaseSettingsMenu();
            reExecute();
        } else if (EncodingCase.hasMenuItemId(itemId)) {
            item.setChecked(true);
            encodingCase = EncodingCase.fromMenuItemId(itemId);
            prefs.setEncodingCase(encodingCase);
            reExecute();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!userInputChangedByApp) {
            this.previousExecutionType = null;
            this.previousExecutionUserInput = null;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
