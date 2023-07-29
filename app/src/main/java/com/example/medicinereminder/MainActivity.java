package com.example.medicinereminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView recyclerView;
    private FloatingActionButton addMedicineButton;
    private MedicineAdapter medicineAdapter;  // Adapter for RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "medicine-database")
                .fallbackToDestructiveMigration()
                .build();

        recyclerView = findViewById(R.id.medicine_recycler_view);
        addMedicineButton = findViewById(R.id.add_medicine_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(() -> {
            List<Medicine> medicines = db.medicineDao().getAll();
            runOnUiThread(() -> {
                medicineAdapter = new MedicineAdapter(medicines);
                recyclerView.setAdapter(medicineAdapter);
            });
        }).start();
    }
}
