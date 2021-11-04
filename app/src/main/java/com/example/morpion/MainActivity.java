package com.example.morpion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.example.morpion.db.DatabaseClient;
import com.example.morpion.model.ApplicationMorpion;
import com.example.morpion.model.Partie;
import com.example.morpion.model.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    //adapters for spinners
    private ArrayAdapter adapterplayer1;
    private ArrayAdapter adapterplayer2;

    private DatabaseClient db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        db = DatabaseClient.getInstance(getApplicationContext());

        this.gridSize = 3;

        player1 = null;
        player2 = null;
        base64player1Image = "";
        base64player2Image ="";

        ApplicationMorpion app =(ApplicationMorpion) getApplicationContext();

        ArrayList<String> tabPlayers = new ArrayList<>();


        adapterplayer1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,tabPlayers);
        spinnerPlayer1.setAdapter(adapterplayer1);


        adapterplayer2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,tabPlayers);
        spinnerPlayer2.setAdapter(adapterplayer2);
        initPlayerSpinner(adapterplayer1);
        initPlayerSpinner(adapterplayer2);

        ArrayAdapter<String> adaptersize = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, POSSIBLE_SIZES);
        adaptersize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adaptersize);

        spinnerPlayer1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spinnerPlayer1.getSelectedItem().toString().equals("Choisir un joueur...")) {
                    class SetPlayer1Task extends AsyncTask<Void, Void, User> {
                    @Override
                    protected User doInBackground(Void... voids) {
                        return db.getAppDatabase().userDao().getUserByName(spinnerPlayer1.getSelectedItem().toString());
                    }

                        @Override
                        protected void onPostExecute(User user) {
                            super.onPostExecute(user);
                            if(user != null){
                                editTextNamePlayer1.setText(user.getName());
                                imagePlayer1.setImageBitmap(base64ToBitmap(user.getBase64image()));
                                base64player1Image = user.getBase64image();
                            }
                        }
                    }
                    SetPlayer1Task t = new SetPlayer1Task();
                    t.execute();
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
                if (!spinnerPlayer2.getSelectedItem().toString().equals("Choisir un joueur...")) {
                    class SetPlayer2Task extends AsyncTask<Void, Void, User> {
                        @Override
                        protected User doInBackground(Void... voids) {
                            return db.getAppDatabase().userDao().getUserByName(spinnerPlayer2.getSelectedItem().toString());
                        }

                        @Override
                        protected void onPostExecute(User user) {
                            super.onPostExecute(user);
                            if(user != null){
                                editTextNamePlayer2.setText(user.getName());
                                imagePlayer2.setImageBitmap(base64ToBitmap(user.getBase64image()));
                                base64player2Image = user.getBase64image();
                            }
                        }
                    }
                    SetPlayer2Task t = new SetPlayer2Task();
                    t.execute();
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
                //Disclaimer : ce morceau de code contient de la sorcellerie à base de returnCodes forcés comme alternative au bête et méchant thread.sleep pour organiser les threads asynchrones.
                if(checkForValidInputs(editTextNamePlayer1, base64player1Image) && checkForValidInputs(editTextNamePlayer2,base64player1Image)){ //si les champs image et pseudo sont non vides (s'ils ont été saisis manuellement ou récupérés dans la BDD
                    Integer playersCheckReturnCode = checkPlayers(); //on vérifie qui est connu de la BD et qui ne l'est pas : 0 = personne n'est connu, 1 = joueur 1 seulement, 2 = joueur 2 seulement, 3 = les deux
                    System.out.println("Nombre de joueurs trouvés : " + (int)playersCheckReturnCode);
                    if ( !((int) playersCheckReturnCode % 2 == 1)){ //équivaut à tous les cas où joueur 1 inconnu
                        System.out.println("Joueur 1 pas en base");
                        int res = addUser(editTextNamePlayer1.getText().toString(),base64player1Image); //forcer le renvoi d'une valeur à la fin de l'appel asynchrone permet d'empêcher le démarrage du jeu avant l'ajout en base du nouveau joueur. C'est sale, mais à moins de bidouiller avec des mutex, difficile de faire mieux.
                    }

                    if ( !((int) playersCheckReturnCode / 2 == 1)){ //équivaut à tous les cas où joueur 2 inconnu
                        System.out.println("Joueur 2 pas en base");
                        int res = addUser(editTextNamePlayer2.getText().toString(),base64player2Image);
                    }

                    startGame();
                    }
                }
        });

    } //end of constructor

    //handles images from camera
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

    private void initPlayerSpinner(ArrayAdapter adapter ){

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
                spinnerPlayersValues.add("Choisir un joueur...");
                class UpdateSpinner extends AsyncTask<Void, Void, List<User>>{
                    @Override
                    protected List<User> doInBackground(Void... voids) {
                        ApplicationMorpion app =(ApplicationMorpion) getApplicationContext();

                        return (List<User>) db.getAppDatabase().userDao().getAll();
                    }

                    @Override
                    protected void onPostExecute(List<User> users) {
                        super.onPostExecute(users);

                        for (int i = 1; i<=users.size();i++){
                            spinnerPlayersValues.add(users.get(i-1).getName());
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

    public int addUser(String name,String base64image ){
        class SaveTask extends AsyncTask<Void, Void, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {

                // creating a user
                User user = new User(name,base64image);

                // adding to database
                db.getAppDatabase()
                        .userDao()
                        .insert(user);

                return new Integer(1);
            }

        }
        SaveTask st = new SaveTask();
        Integer res = 0;
        try {
            res = st.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (int) res;
    }

    private boolean checkForValidInputs(EditText editText, String base64playerImage){
        if(editText.getText().toString().equals("")){
            Toast.makeText(MainActivity.this, "Le nom choisi par joueur 1 est vide",Toast.LENGTH_SHORT).show();
            return false;
        } else if (base64playerImage=="") {
            Toast.makeText(MainActivity.this, "Mettez une photo pour le joueur 1",Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
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

    private void startGame(){
        ApplicationMorpion app =(ApplicationMorpion) getApplicationContext();
        class StartGame extends AsyncTask<Void, Void, List<User> >{
            @Override
            protected List<User> doInBackground(Void... voids) {
                ArrayList<User> res = new ArrayList<>();
                User res1 = db.getAppDatabase().userDao().getUserByName(editTextNamePlayer1.getText().toString());
                User res2 = db.getAppDatabase().userDao().getUserByName(editTextNamePlayer2.getText().toString());



                if (res1 == null){
                    toastFromWorkerThread("Impossible de trouver le joueur "+ editTextNamePlayer1.getText().toString());

                } else {
                    res.add(res1);
                }

                if (res2 == null){
                    toastFromWorkerThread("Impossible de trouver le joueur "+ editTextNamePlayer2.getText().toString());
                } else {
                    res.add(res2);
                }

                if (res1.equals(res2)){
                    toastFromWorkerThread("Les 2 joueurs sont identiques !");
                    return new ArrayList<User>(); //sera de taille 0 donc on PostExecute ne sera pas lancé
                }

                return res;
            }

            @Override
            protected void onPostExecute(List<User> res) {
                super.onPostExecute(res);
                if (res.size() ==2) {
                    player1 = res.get(0);
                    player2 = res.get(1);
                    if (player1!=null && player2!=null){
                        Toast.makeText(MainActivity.this,"Création de la partie",Toast.LENGTH_SHORT).show();
                        app.setGame(new Partie(gridSize,player1,player2));
                        Toast.makeText(MainActivity.this,"Partie créée",Toast.LENGTH_SHORT).show();
                        Intent toPlateau = new Intent(MainActivity.this, PlateauActivity.class);
                        startActivity(toPlateau);
                    }
                }
            }

            private void toastFromWorkerThread(String message){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(MainActivity.this,message, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        }
        StartGame s = new StartGame();
        s.execute();
    }

    //permet de savoir qui il faut ajouter en base et qui y est déjà

    private int checkPlayers() {
        class CheckPlayers extends AsyncTask<Void, Void, Integer >{
            @Override
            protected Integer doInBackground(Void... voids) {
                Integer returnCode = 0;
                User u1 = db.getAppDatabase().userDao().getUserByName(editTextNamePlayer1.getText().toString());
                User u2 = db.getAppDatabase().userDao().getUserByName(editTextNamePlayer2.getText().toString());
                if(u1!=null){
                    returnCode = returnCode+1;
                }
                if (u2!=null){
                    returnCode = returnCode + 2;
                }
                return returnCode;
            }
            }
            CheckPlayers cn = new CheckPlayers();
        int res = 0;
        try {
            res = cn.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
        }


}