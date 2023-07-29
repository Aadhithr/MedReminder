package com.example.medicinereminder;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneOffset;

public class AddReminderActivity extends AppCompatActivity {
    private EditText medicineName;
    private Button submitButton;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "medicine-database")
                .fallbackToDestructiveMigration()
                .build();

        medicineName = findViewById(R.id.medicine_name);
        submitButton = findViewById(R.id.submit_button);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = medicineName.getText().toString();

                // Combine the DatePicker and TimePicker values into a LocalDateTime.
                LocalDateTime dueDate = LocalDateTime.of(
                        datePicker.getYear(),
                        datePicker.getMonth() + 1,  // months are 0-indexed in DatePicker
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                // Convert the LocalDateTime to a timestamp
                long timestamp = dueDate.toInstant(ZoneOffset.UTC).toEpochMilli();

                Medicine medicine = new Medicine();
                medicine.setName(name);
                medicine.setDueDate(timestamp);

                new Thread(() -> {
                    db.medicineDao().insert(medicine);
                    runOnUiThread(() -> finish());
                }).start();
            }
        });
    }
}
