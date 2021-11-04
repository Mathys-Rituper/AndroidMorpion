package com.example.morpion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.morpion.db.DatabaseClient;
import com.example.morpion.model.ApplicationMorpion;
import com.example.morpion.model.User;

import java.util.ArrayList;

public class PlateauActivity extends AppCompatActivity {
    //VIEWS
    private TextView textViewStatus;
    private Button buttonToMenu;
    private LinearLayout linearLayoutGame;
    private ArrayList<ImageButton> buttons;
    private DatabaseClient db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plateau);

        textViewStatus = findViewById(R.id.plateauActivity_status);
        buttonToMenu = findViewById(R.id.plateauActivity_menu);
        linearLayoutGame = findViewById(R.id.plateauActivity_game);
        buttons = new ArrayList<>();

        ApplicationMorpion app =(ApplicationMorpion) getApplicationContext();
        db = DatabaseClient.getInstance(getApplicationContext());

        textViewStatus.setText("Construction du plateau de taille " + app.getGame().getTaille());

        buttonToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMenu = new Intent(PlateauActivity.this, MainActivity.class);
                startActivity(toMenu);
                finish();
            }
        });

        for (int i = 0; i<app.getGame().getTaille();i++){
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j<app.getGame().getTaille();j++){
                ImageButton ib = new ImageButton(this);

                ib.setBackgroundResource(R.drawable.emptybutton);

                //getting a proper layout with this is literally the most busted thing ever in this project, dark magic was required to achieve proper button sizing
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        toDp(60),toDp(60)
                );
                params.setMargins(toDp(5), toDp(5), toDp(5), toDp(5));
                ib.setLayoutParams(params);

                ib.setClickable(true);
                ib.setScaleType(ImageView.ScaleType.FIT_XY);
                buttons.add(ib);
                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = buttons.indexOf(v);
                        onButtonClick(index); //Logique du clic bouton déportée sur une méthode à part pour alléger onCreate
                    }
                });
                l.addView(ib);
            }
            linearLayoutGame.addView(l);
        }

        ViewGroup vg = findViewById (R.id.plateauActivity_game);
        vg.invalidate();
        textViewStatus.setText("Au tour de "+ app.getGame().getCurrentPlayer().getName() + " de jouer...");

    }

    private void onButtonClick(int index){
        ApplicationMorpion app =(ApplicationMorpion) getApplicationContext();
        if (app.getGame().getGrid().get(index) == 0){
            int res = app.getGame().jouerCoup(index);
            if (res!=3) { //le coup a été joué avec succès, la grille du modèle a changé
                ImageButton b = buttons.get(index);
                Drawable d = new BitmapDrawable(getResources(),base64ToBitmap(app.getGame().getCurrentPlayer().getBase64image()) );
                b.setBackground(d);
                b.setClickable(false);

                if (res == 1 || res == 2) { //partie terminée : grille pleine ou match nul

                    //mise à jour des scores dans la bd en fin de partie
                    updateUser(app.getGame().getPlayer1());
                    updateUser(app.getGame().getPlayer2());

                    //on attend que la bd se mette à jour avec les nouveaux scores modifiés par le modèle Partie
                    try {
                        Thread.sleep(800); //
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Intent toScoreboard = new Intent(PlateauActivity.this, ScoreboardActivity.class);
                    startActivity(toScoreboard);
                    finish();
                }else {
                    app.getGame().nextPlayer();
                    textViewStatus.setText("Au tour de "+ app.getGame().getCurrentPlayer().getName() + " de jouer...");
                }
            }

        }
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    private int toDp(int dp){
        Resources r = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }

    public void updateUser(User user){
        class UpdateTask extends AsyncTask<Void, Void, User> {
            @Override
            protected User doInBackground(Void... voids) {
                db.getAppDatabase().userDao().update(user);

                return user;
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }
}