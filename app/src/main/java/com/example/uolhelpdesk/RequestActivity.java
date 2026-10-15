package com.example.uolhelpdesk;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class RequestActivity extends AppCompatActivity {

    private EditText etPhone;
    private EditText etLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this,
                SystemBarStyle.dark(Color.TRANSPARENT),
                SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT));
        setContentView(R.layout.activity_request);

        View header = findViewById(R.id.header);
        int headerTopPadding = header.getPaddingTop();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            header.setPadding(header.getPaddingLeft(), headerTopPadding + bars.top,
                    header.getPaddingRight(), header.getPaddingBottom());
            v.setPadding(bars.left, 0, bars.right, bars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Read the name sent from the home screen; fall back to "Student" if nothing came through
        String name = getIntent().getStringExtra(MainActivity.EXTRA_STUDENT_NAME);
        if (name == null || name.trim().isEmpty()) {
            name = getString(R.string.default_student_name);
        }
        TextView tvGreeting = findViewById(R.id.tvGreeting);
        tvGreeting.setText(getString(R.string.greeting, name));

        etPhone = findViewById(R.id.etPhone);
        etLink = findViewById(R.id.etLink);
        Button btnCallNumber = findViewById(R.id.btnCallNumber);
        Button btnOpenLink = findViewById(R.id.btnOpenLink);

        btnCallNumber.setOnClickListener(v -> callNumber());
        btnOpenLink.setOnClickListener(v -> openLink());
    }

    private void callNumber() {
        String number = etPhone.getText().toString().trim();
        if (number.isEmpty()) {
            etPhone.setError(getString(R.string.error_phone_empty));
            etPhone.requestFocus();
            return;
        }
        if (!isValidMobileNumber(number)) {
            etPhone.setError(getString(R.string.error_phone_invalid));
            etPhone.requestFocus();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        MainActivity.openIntent(this, intent);
    }

    private void openLink() {
        String link = etLink.getText().toString().trim();
        if (link.isEmpty()) {
            etLink.setError(getString(R.string.error_link_empty));
            etLink.requestFocus();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(toWebLink(link)));
        MainActivity.openIntent(this, intent);
    }

    /** A valid number is exactly 11 digits and starts with 03, e.g. 03001234567. */
    static boolean isValidMobileNumber(String number) {
        return number.matches("03\\d{9}");
    }

    /** Adds https:// in front of the link unless it already starts with http:// or https://. */
    static String toWebLink(String link) {
        String lower = link.toLowerCase(Locale.ROOT);
        if (lower.startsWith("http://") || lower.startsWith("https://")) {
            return link;
        }
        return "https://" + link;
    }
}
