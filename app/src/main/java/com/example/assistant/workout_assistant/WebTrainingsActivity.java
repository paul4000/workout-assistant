package com.example.assistant.workout_assistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.assistant.workout_assistant.adapters.TrainingsArrayAdapter;
import com.example.assistant.workout_assistant.exercises.Training;
import com.example.assistant.workout_assistant.webService.TrainingService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebTrainingsActivity extends AppCompatActivity {

    private TrainingService trainingService = new TrainingService();
    private List<Training> webTrainings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_trainings);
        loadTrainings();
    }

    private void loadTrainings() {

        trainingService.loadTrainings(new Callback<List<Training>>() {
            @Override
            public void onResponse(Call<List<Training>> call, Response<List<Training>> response) {
                webTrainings = response.body();

                ListView trainingsView = (ListView) findViewById(R.id.trainingsList);
                trainingsView.setAdapter(new TrainingsArrayAdapter(WebTrainingsActivity.this, webTrainings));
            }

            @Override
            public void onFailure(Call<List<Training>> call, Throwable t) {
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setVisibility(TextView.VISIBLE);
                textView.setText(R.string.downloading_error);
            }
        });

    }

}
