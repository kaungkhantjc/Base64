package com.jcoder.base64;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jcoder.base64.async.Base64Task;
import com.jcoder.base64.databinding.ActivityMainBinding;
import com.jcoder.base64.utils.ClipboardUtils;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEncode.setOnClickListener(v -> startBase64Process(Base64Task.Type.ENCODER, getInputText()));
        binding.btnDecode.setOnClickListener(v -> startBase64Process(Base64Task.Type.DECODER, getInputText()));
    }

    private String getInputText() {
        return binding.edt.getText().toString();
    }

    private void startBase64Process(Base64Task.Type type, String str) {
        Button targetButton = type == Base64Task.Type.ENCODER ? binding.btnEncode : binding.btnDecode;
        targetButton.setEnabled(false);

        Base64Task.execute(type, str, new Base64Task.Callback() {
            @Override
            public void onSuccess(String str) {
                binding.edt.setText(str);
                binding.edt.setSelection(str.length());
                targetButton.setEnabled(true);
            }

            @Override
            public void onError(IllegalArgumentException e) {
                targetButton.setEnabled(true);
                showErrorDialog(e);
            }
        });
    }

    private void pasteText() {
        binding.edt.getText().insert(binding.edt.getSelectionEnd(), ClipboardUtils.getText(this));
    }

    private void copyText() {
        ClipboardUtils.copyText(this, getInputText());
        Toast.makeText(this, R.string.msg_text_copied, Toast.LENGTH_SHORT).show();
    }

    private void clearText() {
        binding.edt.getText().clear();
    }

    private void showErrorDialog(IllegalArgumentException e) {
        new AlertDialog.Builder(this)
                .setTitle(e.getClass().getSimpleName())
                .setMessage(e.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_copy) copyText();
        else if (itemId == R.id.menu_paste) pasteText();
        else if (itemId == R.id.menu_clear) clearText();
        else if (itemId == R.id.menu_share_text)
            shareText(R.string.menu_share_text, getInputText());
        else if (itemId == R.id.menu_share_app) shareApp();

        return super.onOptionsItemSelected(item);
    }

}