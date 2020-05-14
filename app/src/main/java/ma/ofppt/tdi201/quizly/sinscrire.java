package ma.ofppt.tdi201.quizly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import ma.ofppt.tdi201.quizly.Model.User;

public class sinscrire extends AppCompatActivity {
    MaterialEditText edtNewUserName, edtNewPassword, edtNewEmail,edNewPrenom; // for Sign up
    MaterialEditText edtUserName, edtPassword; // for Sign in
    Button btnSignUp, btnSignIn;
    Spinner filieresp;
    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinscrire);

        //Firease
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        edtUserName = (MaterialEditText) findViewById(R.id.edtUserName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);





        btnSignUp = (Button) findViewById(R.id.btnSignUpp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        edtNewUserName = (MaterialEditText) findViewById(R.id.edtNewUserName);
        edtNewPassword = (MaterialEditText) findViewById(R.id.edtNewPassword);
        edtNewEmail    = (MaterialEditText) findViewById(R.id.edtNewEmail);
        edNewPrenom =(MaterialEditText) findViewById(R.id.edtNewPrenom);
        filieresp=(Spinner) findViewById(R.id.edNewFiliere);


        btnSignUp.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signup();
                        Intent in = new Intent(sinscrire.this,MainActivity.class);
                        startActivity(in);
                        finish();



                    }
                }
        );

        btnSignIn.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(sinscrire.this,MainActivity.class);
                        startActivity(in);
                        finish();



                    }
                }
        );


    }

    public void signup(){

        if(!edtNewUserName.getText().toString().isEmpty() && !edNewPrenom.getText().toString().isEmpty()){
            if(!edtNewPassword.getText().toString().isEmpty()){
                final User user = new User(edtNewUserName.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtNewEmail.getText().toString(),edNewPrenom.getText().toString(), (String) filieresp.getSelectedItem());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUserName()).exists() || dataSnapshot.child(user.getPrenom()).exists() ){
                            Toast.makeText(sinscrire.this,"Ce utilisateur exist deja!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(sinscrire.this, "Inscrit avec succes!", Toast.LENGTH_SHORT).show();
                            users.child(user.getUserName()).setValue(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
            else {
                Toast.makeText(sinscrire.this, "Remplir votre mot de passe svp.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(sinscrire.this, "Remplir votre nom et prenom d'utilisateur svp.", Toast.LENGTH_SHORT).show();
        }
    }
}
