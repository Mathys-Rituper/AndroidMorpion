package com.example.morpion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.morpion.model.ApplicationMorpion;
import com.example.morpion.model.Partie;
import com.example.morpion.model.User;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private final String[] POSSIBLE_SIZES = {"3","4","5","6"};
    static final int REQUEST_IMAGE_CAPTURE_1 = 1;
    static final int REQUEST_IMAGE_CAPTURE_2 = 2;

    //VIEWS
    private Spinner spinnerPlayer1;
    private Spinner spinnerPlayer2;
    private EditText editTextNamePlayer1;
    private EditText editTextNamePlayer2;
    private Button buttonPhotoPlayer1;
    private Button buttonPhotoPlayer2;
    private Spinner sizeSpinner;
    private ImageView imagePlayer1;
    private ImageView imagePlayer2;
    private Button playButton;

    //initialization variables
    private User player1;
    private User player2;
    private int gridSize;
    private String base64player1Image;
    private String base64player2Image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerPlayer1 = (Spinner) findViewById(R.id.mainActivity_player1spinner);
        spinnerPlayer2 = (Spinner) findViewById(R.id.mainActivity_player2spinner);
        sizeSpinner = (Spinner) findViewById(R.id.mainActivity_tailleGrille);
        editTextNamePlayer1 = (EditText) findViewById(R.id.mainActivity_player1name);
        editTextNamePlayer2 = (EditText) findViewById(R.id.mainActivity_player2name);
        buttonPhotoPlayer1 = (Button) findViewById(R.id.mainactivity_player1selectphoto);
        buttonPhotoPlayer2 = (Button) findViewById(R.id.mainactivity_player2selectphoto);
        imagePlayer1 = (ImageView) findViewById(R.id.mainActivity_player1Photo);
        imagePlayer2 = (ImageView) findViewById(R.id.mainActivity_player2Photo);
        playButton = (Button) findViewById(R.id.mainActivity_buttonPlay);

        this.gridSize = 3;

        player1 = null;
        player2 = null;
        base64player1Image = "";
        base64player2Image ="";

        ApplicationMorpion app =(ApplicationMorpion) getApplicationContext();

        String[] spinnerPlayersValues = new String[app.getPlayers().size()+1];
        spinnerPlayersValues[0] = "Choisir un joueur...";


        String[] playerNames =  app.getPlayers().keySet().toArray(new String[app.getPlayers().size()]);
        for (int i = 0; i<playerNames.length;i++){
            String name = playerNames[i];
            spinnerPlayersValues[i+1] = name;
        }

        ArrayAdapter<String> adapterplayer1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerPlayersValues);
        adapterplayer1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayer1.setAdapter(adapterplayer1);


        ArrayAdapter<String> adapterplayer2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerPlayersValues);
        adapterplayer2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayer2.setAdapter(adapterplayer2);


        ArrayAdapter<String> adaptersize = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, POSSIBLE_SIZES);
        adaptersize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adaptersize);

        spinnerPlayer1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerPlayer1.getSelectedItem().toString() != "Choisir un joueur...") {
                    player1 = app.getPlayers().get(spinnerPlayer1.getSelectedItem().toString());
                    editTextNamePlayer1.setText(player1.getName());
                    imagePlayer1.setImageBitmap(base64ToBitmap(player1.getBase64image()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        spinnerPlayer2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerPlayer2.getSelectedItem().toString() != "Choisir un joueur...") {
                    player2 = app.getPlayers().get(spinnerPlayer2.getSelectedItem().toString());
                    editTextNamePlayer2.setText(player2.getName());
                    imagePlayer2.setImageBitmap(base64ToBitmap(player2.getBase64image()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gridSize = Integer.parseInt(sizeSpinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gridSize=3;
            }
        });

        buttonPhotoPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_1);
                } catch (ActivityNotFoundException e) {
                    System.out.println((e.toString()));
                }
            }
        });

        buttonPhotoPlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_2);
                } catch (ActivityNotFoundException e) {
                    System.out.println(e.toString());
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player1 == null) {
                    if(app.getPlayers().containsKey(editTextNamePlayer1.getText().toString()) ||editTextNamePlayer1.getText().toString()==""){
                       Toast.makeText(MainActivity.this, "Le nom choisi par joueur 1 est déjà pris ou vide",Toast.LENGTH_SHORT).show();
                    } else {
                        if (base64player1Image ==""){
                            Toast.makeText(MainActivity.this, "Mettez une photo pour le joueur 1",Toast.LENGTH_SHORT).show();
                        } else {
                            app.addPlayer(new User(editTextNamePlayer1.getText().toString(),base64player1Image));
                            player1=app.getPlayers().get(editTextNamePlayer1.getText().toString());
                        }
                    }
                }

                if (player2 == null) {
                    if(app.getPlayers().containsKey(editTextNamePlayer2.getText().toString()) || editTextNamePlayer2.getText().toString()==""){
                        Toast.makeText(MainActivity.this, "Le nom choisi par joueur 2 est déjà pris ou vide",Toast.LENGTH_SHORT).show();
                    } else {
                        if (base64player2Image ==""){
                            Toast.makeText(MainActivity.this, "Mettez une photo pour le joueur 2",Toast.LENGTH_SHORT).show();
                        } else {
                            app.addPlayer(new User(editTextNamePlayer2.getText().toString(),base64player2Image));
                            player2=app.getPlayers().get(editTextNamePlayer2.getText().toString());
                        }
                    }
                }
                if (player1!=null && player2!=null){
                    Toast.makeText(MainActivity.this,"Création de la partie",Toast.LENGTH_SHORT).show();
                    app.setGame(new Partie(gridSize,player1,player2));
                    Toast.makeText(MainActivity.this,"Partie créée",Toast.LENGTH_SHORT).show();
                    Intent toPlateau = new Intent(MainActivity.this, PlateauActivity.class);
                    startActivity(toPlateau);
                }
                }
        });

    } //end of constructor

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_IMAGE_CAPTURE_1 || requestCode == REQUEST_IMAGE_CAPTURE_2) && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (requestCode == REQUEST_IMAGE_CAPTURE_1) {
                imagePlayer1.setImageBitmap(imageBitmap);
                base64player1Image = bitmapToBase64(imageBitmap);
            } else {
                imagePlayer2.setImageBitmap(imageBitmap);
                base64player2Image = bitmapToBase64(imageBitmap);
            }
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

}