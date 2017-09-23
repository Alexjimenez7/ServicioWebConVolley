package volley.alexjimenez.com.serviciowebconvolley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome " +
                getIntent().getStringExtra("username"));
    }

    public void back_to_login(View v) {
        finish();
    }
}
