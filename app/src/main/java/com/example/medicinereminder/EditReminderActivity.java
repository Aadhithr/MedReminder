package com.example.medicinereminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Calendar;

public class EditReminderActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText medicineName;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button submitButton;
    private long medicineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        db = AppDatabase.getInstance(this); // Initialize the database

        medicineName = findViewById(R.id.medicine_name);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
        submitButton = findViewById(R.id.submit_button);
        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> {
            // Call a method to delete the reminder
            deleteReminder();
        });

        // Get the ID of the Medicine to be edited from the Intent.
        medicineId = getIntent().getLongExtra("medicineId", -1);

        new Thread(() -> {
            // Retrieve the Medicine object from the database.
            Medicine medicineToEdit = db.medicineDao().findById(medicineId);
            // Update the UI on the main thread.
            runOnUiThread(() -> {
                if (medicineToEdit != null) { // Check if the medicineToEdit object is not null
                    medicineName.setText(medicineToEdit.getName());

                    LocalDateTime dueDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(medicineToEdit.getDueDate()), ZoneOffset.UTC);

                    datePicker.updateDate(dueDate.getYear(), dueDate.getMonthValue() - 1, dueDate.getDayOfMonth());

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        timePicker.setHour(dueDate.getHour());
                        timePicker.setMinute(dueDate.getMinute());
                    } else {
                        // For older versions of Android, use the deprecated methods.
                        timePicker.setCurrentHour(dueDate.getHour());
                        timePicker.setCurrentMinute(dueDate.getMinute());
                    }
                } else {
                    // Handle the case when the Medicine object is not found in the database
                    // For example, you can display an error message and finish the activity.
                    Toast.makeText(this, "Medicine not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();

        submitButton.setOnClickListener(v -> {
            int hour, minute;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
            } else {
                // For older versions of Android, use the deprecated methods.
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
            }

            // Convert the selected time to a LocalDateTime object
            LocalDateTime selectedDateTime = LocalDateTime.of(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(), hour, minute);

            // Convert the LocalDateTime object to milliseconds
            long selectedTimeMillis = selectedDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

            new Thread(() -> {
                Medicine medicineToUpdate = db.medicineDao().findById(medicineId);
                if (medicineToUpdate != null) {
                    medicineToUpdate.setName(medicineName.getText().toString());
                    medicineToUpdate.setDueDate(selectedTimeMillis);
                    db.medicineDao().update(medicineToUpdate);
                } else {
                    // Handle the case when the Medicine object is not found in the database
                    // For example, you can display an error message.
                    runOnUiThread(() -> Toast.makeText(this, "Medicine not found", Toast.LENGTH_SHORT).show());
                }
            }).start();

            finish();
        });
    }

    private void deleteReminder() {
        new Thread(() -> {
            Medicine medicineToDelete = db.medicineDao().findById(medicineId);
            if (medicineToDelete != null) {
                db.medicineDao().delete(medicineToDelete);
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Medicine not found", Toast.LENGTH_SHORT).show());
            }
        }).start();

        finish();
    }
}
