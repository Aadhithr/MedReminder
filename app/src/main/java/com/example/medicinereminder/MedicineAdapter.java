package com.example.medicinereminder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicines;

    public MedicineAdapter(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_item, parent, false);
        return new MedicineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.medicineName.setText(medicine.getName());

        LocalDateTime dueDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(medicine.getDueDate()), ZoneOffset.UTC);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy");
        holder.medicineDate.setText(dateFormat.format(dueDate));

        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
        holder.medicineTime.setText(timeFormat.format(dueDate.toLocalTime()));
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder {

        TextView medicineName;
        TextView medicineDate;
        TextView medicineTime;
        ImageButton editButton;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medicineDate = itemView.findViewById(R.id.medicine_date);
            medicineTime = itemView.findViewById(R.id.medicine_time);
            editButton = itemView.findViewById(R.id.edit_button);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Medicine medicine = medicines.get(position);
                        Intent intent = new Intent(itemView.getContext(), EditReminderActivity.class);
                        intent.putExtra("medicineId", medicine.getId());
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
