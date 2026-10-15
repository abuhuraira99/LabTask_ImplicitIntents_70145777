package com.example.uolhelpdesk;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    /** Key used to pass the student's name to RequestActivity. */
    public static final String EXTRA_STUDENT_NAME = "com.example.uolhelpdesk.EXTRA_STUDENT_NAME";

    private static final String UOL_WEBSITE = "https://www.uol.edu.pk";
    private static final String HELP_DESK_NUMBER = "tel:04212345678";
    private static final String CAMPUS_LOCATION = "geo:0,0?q=University+of+Lahore";

    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Light status bar icons, because the header behind them is dark green
        EdgeToEdge.enable(this,
                SystemBarStyle.dark(Color.TRANSPARENT),
                SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT));
        setContentView(R.layout.activity_main);

        View header = findViewById(R.id.header);
        int headerTopPadding = header.getPaddingTop();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            // The header colour runs behind the status bar, the content stays below it
            header.setPadding(header.getPaddingLeft(), headerTopPadding + bars.top,
                    header.getPaddingRight(), header.getPaddingBottom());
            v.setPadding(bars.left, 0, bars.right, bars.bottom);
            return insets;
        });

        etName = findViewById(R.id.etName);
        Button btnWebsite = findViewById(R.id.btnWebsite);
        Button btnCall = findViewById(R.id.btnCall);
        Button btnMap = findViewById(R.id.btnMap);
        Button btnRequest = findViewById(R.id.btnRequest);

        // Implicit Intent: Android picks the browser for an https link
        btnWebsite.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(UOL_WEBSITE));
            openIntent(MainActivity.this, intent);
        });

        // Implicit Intent: ACTION_DIAL only fills in the number, it never places the call
        btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(HELP_DESK_NUMBER));
            openIntent(MainActivity.this, intent);
        });

        // Implicit Intent: a geo: Uri with a search query is handled by the maps app
        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(CAMPUS_LOCATION));
            openIntent(MainActivity.this, intent);
        });

        // Explicit Intent: names RequestActivity directly and carries the student's name
        btnRequest.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                etName.setError(getString(R.string.error_name));
                etName.requestFocus();
                return;
            }
            Intent intent = new Intent(MainActivity.this, RequestActivity.class);
            intent.putExtra(EXTRA_STUDENT_NAME, name);
            startActivity(intent);
        });
    }

    /**
     * Starts the given Intent safely. If no installed app can handle it, the user sees
     * a Toast instead of the app crashing. Used by every button that leaves the app.
     */
    public static void openIntent(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.toast_no_app, Toast.LENGTH_SHORT).show();
        }
    }
}
