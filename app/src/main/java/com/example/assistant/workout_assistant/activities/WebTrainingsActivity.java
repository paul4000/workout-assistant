package com.example.assistant.workout_assistant.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.assistant.workout_assistant.R;
import com.example.assistant.workout_assistant.adapters.ResponseTrainingsArrayAdapter;
import com.example.assistant.workout_assistant.authorization.Authorization;
import com.example.assistant.workout_assistant.webService.ResponseTrainingsHeader;
import com.example.assistant.workout_assistant.webService.TrainingService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebTrainingsActivity extends AppCompatActivity {

    private TrainingService trainingService = new TrainingService();
    private List<ResponseTrainingsHeader> headersTrainingList = new ArrayList<>();


    SharedPreferences sharedPreferences;
    Authorization authorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_trainings);

        sharedPreferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        authorization = new Authorization(sharedPreferences);

        if (!authorization.isLogged()) {
            authorization.askLogin(this);
        }


        loadTrainings();

        ListView trainingsList = (ListView) findViewById(R.id.headersList);
        trainingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WebTrainingsActivity.this, TrainingDetailsActivity.class);
                ResponseTrainingsHeader trainingHeader = (ResponseTrainingsHeader) parent.getItemAtPosition(position);
                intent.putExtra("TRAINING_HEADER", trainingHeader);
                intent.putExtra("MODE", "WEB");

                startActivity(intent);
            }
        });
    }

    private void loadTrainings() {

        trainingService.loadTrainings(new Callback<List<ResponseTrainingsHeader>>() {
            @Override
            public void onResponse(Call<List<ResponseTrainingsHeader>> call, Response<List<ResponseTrainingsHeader>> response) {
                headersTrainingList = response.body();
                ListView trainingsView = (ListView) findViewById(R.id.headersList);
                trainingsView.setAdapter(new ResponseTrainingsArrayAdapter(WebTrainingsActivity.this, headersTrainingList));
            }

            @Override
            public void onFailure(Call<List<ResponseTrainingsHeader>> call, Throwable t) {
                TextView errorInfo = (TextView) findViewById(R.id.error_info);
                errorInfo.setVisibility(TextView.VISIBLE);
                errorInfo.setText(R.string.downloading_error);
                Log.d("POBIERANIE", t.getMessage());
            }
        });

    }

}
