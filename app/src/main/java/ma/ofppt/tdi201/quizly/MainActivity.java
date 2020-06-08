package ma.ofppt.tdi201.quizly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ma.ofppt.tdi201.quizly.Common.Common;
import ma.ofppt.tdi201.quizly.Model.User;

public class MainActivity extends AppCompatActivity {

    //press back again  to exit
    private long backPressedTime;

    MaterialEditText edtNewUserName, edtNewPassword, edtNewEmail,edNewPrenom; // for Sign up
    MaterialEditText edtUserName, edtPassword; // for Sign in
    Button btnSignUp, btnSignIn,btntryagain;
    Spinner filieresp;
    FirebaseDatabase database;
    DatabaseReference users;
    //initial wifiManager
    private WifiManager wifiManager;
    private  AlertDialog alertDialog;
    NetReciever netReciever;




    //class loadingDialog
    final DialogLoading loadingdialog = new DialogLoading(MainActivity.this);

    //press back again  to exit method
    @Override
    public void onBackPressed() {

        if(backPressedTime +2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(this, "Appuyez deux fois pour quitter l'application", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Notification
        Notifymethod();


        //register broadcast of internet detector
        netReciever= new NetReciever();

        IntentFilter intentFilter= new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReciever,intentFilter);

        btntryagain=(Button) findViewById(R.id.cnxTryAgain);


        //Firease
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        edtUserName = (MaterialEditText) findViewById(R.id.edtUserName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);





        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);

        btnSignUp.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // showSignUpDialog();
                        Intent intent=new Intent(MainActivity.this,sinscrire.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                signin(edtUserName.getText().toString(), edtPassword.getText().toString());


            }
        });



    }

    private void Notifymethod() {
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,11);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,0);

        Intent intent= new Intent(MainActivity.this,NetReciever.class);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager=(AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }


    private void signin(final String userName, final String password) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(userName).exists()){
                    if(!userName.isEmpty()){
                        User user = dataSnapshot.child(userName).getValue(User.class);
                        if(user.getPassword().equals(password)) {

                            loadingdialog.startdialogNotimeout();
                            Toast.makeText(MainActivity.this, "Bienvenue, " + userName + "!", Toast.LENGTH_SHORT).show();
                            Intent homeActivity = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeActivity);
                            finish();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Informations incorrect.", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        //call methode for loadingdialog
                        loadingdialog.startdialog();
                        Toast.makeText(MainActivity.this, "Entrer votre nom d'utilisateur", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    //call methode for loadingdialog
                    loadingdialog.startdialog();
                    Toast.makeText(MainActivity.this, "I'l n'y a pas d'utilisateur avec ce nom, essayer de s'inscrire.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Se connecter");
        alertDialog.setMessage("Remplir tous les informations SVP");

        LayoutInflater inflater = this.getLayoutInflater();
        View signUp = inflater.inflate(R.layout.sign_up_layout,null);

        edtNewUserName = (MaterialEditText) signUp.findViewById(R.id.edtNewUserName);
        edtNewPassword = (MaterialEditText) signUp.findViewById(R.id.edtNewPassword);
        edtNewEmail    = (MaterialEditText) signUp.findViewById(R.id.edtNewEmail);
        edNewPrenom =(MaterialEditText) signUp.findViewById(R.id.edtNewPrenom);
        filieresp=(Spinner) findViewById(R.id.edNewFiliere);




        alertDialog.setView(signUp);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.setPositiveButton("Inscrire", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                if(!edtNewUserName.getText().toString().isEmpty() && !edNewPrenom.getText().toString().isEmpty()){
                    if(!edtNewPassword.getText().toString().isEmpty()){
                        final User user = new User(edtNewUserName.getText().toString(),
                                edtNewPassword.getText().toString(),
                                edtNewEmail.getText().toString(),edNewPrenom.getText().toString(), (String) filieresp.getSelectedItem());
                        users.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child(user.getUserName()).exists()){
                                    Toast.makeText(MainActivity.this,"Ce nom d'utilisateur exist deja!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Inscrit avec succes!", Toast.LENGTH_SHORT).show();
                                    users.child(user.getUserName()).setValue(user);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        dialog.dismiss();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Remplir votre mot de passe svp.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Remplir votre nom et prenom d'utilisateur svp.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();


    }
}
