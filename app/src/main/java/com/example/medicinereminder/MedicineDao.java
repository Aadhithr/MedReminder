package com.example.medicinereminder;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MedicineDao {
    @Query("SELECT * FROM Medicine")
    List<Medicine> getAll();

    @Query("SELECT * FROM Medicine WHERE id = :id")
    Medicine findById(long id);

    @Insert
    void insert(Medicine medicine);

    @Delete
    void delete(Medicine medicine);

    @Update
    void update(Medicine medicine);
}