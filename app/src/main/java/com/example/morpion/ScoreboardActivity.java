package com.example.morpion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.morpion.db.DatabaseClient;
import com.example.morpion.model.ApplicationMorpion;
import com.example.morpion.model.Partie;
import com.example.morpion.model.User;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardActivity extends AppCompatActivity {

    private TextView textViewSubtitle;
    private ListView listViewScoreboard;
    private Button buttonToMenu;
    private Button buttonReplay;
    private DatabaseClient db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        textViewSubtitle= findViewById(R.id.scoreboardActivity_result);
        buttonToMenu = findViewById(R.id.scoreboardActivity_buttonMenu);
        buttonReplay = findViewById(R.id.scoreboardActibity_buttonReplay);
        listViewScoreboard = findViewById(R.id.scoreboardActivity_listview);

        db = DatabaseClient.getInstance(getApplicationContext());

        ApplicationMorpion app =(ApplicationMorpion) getApplicationContext();

        if (app.getGame().getWinner()==null){
            textViewSubtitle.setText("Match nul !");
        } else {
            textViewSubtitle.setText(app.getGame().getWinner().getName()+ " a gagné la partie !");
        }

        //génération du tableau des scores

        ArrayList<String> scoreboardContent = new ArrayList<>();

        ArrayAdapter<String> userArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, scoreboardContent);
        listViewScoreboard.setAdapter(userArrayAdapter);

        updateAdapter(userArrayAdapter);

        buttonToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMenu = new Intent(ScoreboardActivity.this, MainActivity.class);
                startActivity(toMenu);
                finish();
            }
        });

        buttonReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.setGame(new Partie(app.getGame().getTaille(), app.getGame().getPlayer1(), app.getGame().getPlayer2())); //on recrée une partie de la même taille que la précédente avec les mêmes joueurs
                Intent toPlateau = new Intent(ScoreboardActivity.this, PlateauActivity.class);
                startActivity(toPlateau);
                finish(); //pour que l'appui de la touche retour renvoie au menu
            }
        });

    }

    public void updateAdapter(ArrayAdapter adapter){

        class GetUserCountTask extends AsyncTask<Void, Void, Integer> {
            @Override
            protected Integer doInBackground(Void... voids) {
                ApplicationMorpion app =(ApplicationMorpion) getApplicationContext();

                return (Integer) db.getAppDatabase().userDao().getUserCount();

            }

            @Override
            protected void onPostExecute(Integer count) {
                super.onPostExecute(count);
                ArrayList<String> spinnerPlayersValues = new ArrayList<>();

                class UpdateSpinner extends AsyncTask<Void, Void, List<User>>{
                    @Override
                    protected List<User> doInBackground(Void... voids) {
                        ApplicationMorpion app =(ApplicationMorpion) getApplicationContext();

                        return (List<User>) db.getAppDatabase().userDao().getAll();
                    }

                    @Override
                    protected void onPostExecute(List<User> users) {
                        super.onPostExecute(users);

                        for (int i = 0; i<users.size();i++){
                            spinnerPlayersValues.add(users.get(i).getName() + " ("+users.get(i).getVictories()+"/"+users.get(i).getTies()+'/'+users.get(i).getDefeats()+")");
                        }
                        adapter.clear();
                        adapter.addAll(spinnerPlayersValues);

                        adapter.notifyDataSetChanged();
                    }
                }
                UpdateSpinner u = new UpdateSpinner();
                u.execute();
            }
        }

        GetUserCountTask ut = new GetUserCountTask();
        ut.execute();
    }


}