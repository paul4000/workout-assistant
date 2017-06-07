package com.example.assistant.workout_assistant.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.assistant.workout_assistant.R;
import com.example.assistant.workout_assistant.adapters.PlannedTrainingsArrayAdapter;
import com.example.assistant.workout_assistant.authorization.Authorization;
import com.example.assistant.workout_assistant.database.tables.PlannedTrainingsDAO;
import com.example.assistant.workout_assistant.exercises.PlannedTraining;

import java.util.List;

public class PlannedTrainingsActivity extends AppCompatActivity {


    List<PlannedTraining> trainings;
    PlannedTrainingsDAO trainingsDAO = new PlannedTrainingsDAO(this);
    ListView trainingsList;


    SharedPreferences sharedPreferences;
    Authorization authorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned_trainings);

        authorization = new Authorization();
        sharedPreferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);

        boolean isLogged = sharedPreferences.getBoolean("LOGGED", false);
        String token = sharedPreferences.getString("JWT_TOKEN", null);

        if (!isLogged) {
            authorization.askLogin(this);
        }


        trainingsList = (ListView) findViewById(R.id.plannedTrainingList);
        trainings = trainingsDAO.getPlannedTrainings();

        trainingsList.setAdapter(new PlannedTrainingsArrayAdapter(this, trainings, trainingsDAO));


    }

    @Override
    protected void onDestroy() {
        trainingsDAO.close();
        super.onDestroy();
    }
}
