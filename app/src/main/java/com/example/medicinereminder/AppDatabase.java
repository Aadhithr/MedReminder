package com.example.medicinereminder;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Medicine.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MedicineDao medicineDao();

    // Migration from version 1 to version 2
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // If you need to do some migration operation, you can do it here
            // For example, you can add a new column or modify existing data
            // This method will be called when you update the database version from 1 to 2
        }
    };

    // Migration from version 2 to version 3
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // If you need to do some migration operation, you can do it here
            // For example, you can add a new column or modify existing data
            // This method will be called when you update the database version from 2 to 3
        }
    };

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "medicine-database")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Add both migrations here
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
