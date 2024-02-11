package com.example.xyz;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import android.os.Handler;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText hourEditText;
    private EditText minuteEditText;
    private TextView currentTimeTextView;
    private AlarmManager alarmManager;
    private Handler handler = new Handler();
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hourEditText = findViewById(R.id.hour_edit_text);
        minuteEditText = findViewById(R.id.minute_edit_text);
        currentTimeTextView = findViewById(R.id.current_time_text_view);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        handler.postDelayed(updateTimeRunnable, 0);
    }

    private Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            updateTime();
            handler.postDelayed(this, 1000);
        }
    };

    private void updateTime() {
        long currentTimeMillis = System.currentTimeMillis();
        String currentTime = timeFormat.format(currentTimeMillis);
        currentTimeTextView.setText(currentTime);
    }

    public void setAlarm(View view) {
        String hourString = hourEditText.getText().toString();
        String minuteString = minuteEditText.getText().toString();

        if (hourString.isEmpty() || minuteString.isEmpty()) {
            Toast.makeText(this, "Wprowadź godzinę i minutę", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = Integer.parseInt(hourString);
        int minute = Integer.parseInt(minuteString);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Alarm ustawiony na " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
    }
}
