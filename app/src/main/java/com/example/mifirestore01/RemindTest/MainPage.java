package com.example.mifirestore01.RemindTest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mifirestore01.Notas1.NoteListActivity;
import com.example.mifirestore01.Notas2.NoteList2Activity;
import com.example.mifirestore01.R;
import com.example.mifirestore01.Usuarios.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainPage extends AppCompatActivity {

    private FloatingActionButton add;
    private Dialog dialog;
    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private AdapterReminders adapter;
    private List<Reminders> temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        setTitle(R.string.titulo_Recordatorios);

        appDatabase = AppDatabase.geAppdatabase(MainPage.this);

        add = findViewById(R.id.button_add_remind);
//        empty = findViewById(R.id.empty);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReminder();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainPage.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setItemsInRecyclerView();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_reminder);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.navigation_notes1:
                        startActivity(new Intent(getApplicationContext(), NoteListActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.navigation_reminder:
//                        startActivity(new Intent(getApplicationContext(), MainPage.class));
//                        overridePendingTransition(0,0);
//                        finish();
                        return true;
                    case R.id.navigation_notes2:
                        startActivity(new Intent(getApplicationContext(), NoteList2Activity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                }
                return false;
            }
        });
    }

    public void addReminder() {

        dialog = new Dialog(MainPage.this);
        dialog.setContentView(R.layout.floating_popup);

        final TextView textView = dialog.findViewById(R.id.date);
        Button select, add;
        select = dialog.findViewById(R.id.selectDate);
        add = dialog.findViewById(R.id.addButton);
        final EditText message = dialog.findViewById(R.id.message);


        final Calendar newCalender = Calendar.getInstance();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(MainPage.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final Calendar newDate = Calendar.getInstance();
                        Calendar newTime = Calendar.getInstance();
                        TimePickerDialog time = new TimePickerDialog(MainPage.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                newDate.set(year, month, dayOfMonth, hourOfDay, minute, 0);
                                Calendar tem = Calendar.getInstance();
                                Log.w("TIME", System.currentTimeMillis() + "");
                                if (newDate.getTimeInMillis() - tem.getTimeInMillis() > 0)
                                    textView.setText(newDate.getTime().toString());
                                else
                                    Toast.makeText(MainPage.this, R.string.toast_InvalidDate, Toast.LENGTH_SHORT).show();

                            }
                        }, newTime.get(Calendar.HOUR_OF_DAY), newTime.get(Calendar.MINUTE), true);
                        time.show();

                    }
                }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                String miTexto = textView.getText().toString().trim();
                if (miTexto == null || miTexto.isEmpty()) {
                    Toast.makeText(MainPage.this, R.string.toast_EmptyDate, Toast.LENGTH_SHORT).show();
                } else {

                    RoomDAO roomDAO = appDatabase.getRoomDAO();
                    Reminders reminders = new Reminders();
                    reminders.setMessage(message.getText().toString().trim());
                    Date remind = new Date(textView.getText().toString().trim());
                    reminders.setRemindDate(remind);
                    roomDAO.Insert(reminders);
                    List<Reminders> l = roomDAO.getAll();
                    reminders = l.get(l.size() - 1);
                    Log.e("ID chahiye", reminders.getId() + "");

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(remind);
                    calendar.set(Calendar.SECOND, 0);
                    Intent intent = new Intent(MainPage.this, NotifierAlarm.class);
                    intent.putExtra("Message", reminders.getMessage());
                    intent.putExtra("RemindDate", reminders.getRemindDate().toString());
                    intent.putExtra("id", reminders.getId());
                    PendingIntent intent1 = PendingIntent.getBroadcast(MainPage.this, reminders.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);

                    Toast.makeText(MainPage.this, R.string.toast_ReminderActivo, Toast.LENGTH_SHORT).show();
                    setItemsInRecyclerView();
                    AppDatabase.destroyInstance();
                    dialog.dismiss();
                }
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }

    public static void setAlarm(int i, Long timestamp, Context ctx) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, NotifierAlarm.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }

    public void setItemsInRecyclerView() {

        RoomDAO dao = appDatabase.getRoomDAO();
        temp = dao.orderThetable();
        if (temp.size() > 0) {
//            empty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter = new AdapterReminders(temp);
        recyclerView.setAdapter(adapter);

    }
}
