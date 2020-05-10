package ma.ofppt.tdi201.quizly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import ma.ofppt.tdi201.quizly.Common.Common;
import ma.ofppt.tdi201.quizly.Model.User;

public class MainActivity extends AppCompatActivity {
    MaterialEditText edtNewUserName, edtNewPassword, edtNewEmail; // for Sign up
    MaterialEditText edtUserName, edtPassword; // for Sign in
    Button btnSignUp, btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;
    //class loadingDialog
    final DialogLoading loadingdialog = new DialogLoading(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        showSignUpDialog();
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

    private void signin(final String userName, final String password) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(userName).exists()){
                    if(!userName.isEmpty()){
                        User user = dataSnapshot.child(userName).getValue(User.class);
                        if(user.getPassword().equals(password)) {

                            //loadingdialog.startdialogNotimeout();
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
                if(!edtNewUserName.getText().toString().isEmpty()){
                    if(!edtNewPassword.getText().toString().isEmpty()){
                        final User user = new User(edtNewUserName.getText().toString(),
                                edtNewPassword.getText().toString(),
                                edtNewEmail.getText().toString());
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
                    Toast.makeText(MainActivity.this, "Remplir votre nom d'utilisateur svp.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }
}
