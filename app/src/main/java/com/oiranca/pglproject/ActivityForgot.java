package com.oiranca.pglproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ActivityForgot extends AppCompatActivity {

    private EditText forgoten;
    private DatabaseReference databaseReference;
    private String emailforSendEmail;

    private String passWord;
    private static Session session;
    private static Properties properties;
    private static Transport transport;
    private static MimeMessage mensaje;


    private static String direccionCorreo;
    private static String contrasenyaCorreo;


    private static String destintatarioCorreo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_forgot);
        Toolbar toolbar = findViewById(R.id.toolbarForgot);
        setSupportActionBar(toolbar);
        forgoten = (EditText) findViewById(R.id.textForgot);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

       final FloatingActionButton fab = findViewById(R.id.fabForgot);


       // Crea el Alert dialog para advertir si no llega el mensaje, ya que hay que
        // realizar una confoguración el correo de gmail

        AlertDialog.Builder alertRemember = new AlertDialog.Builder(ActivityForgot.this);
        alertRemember.setTitle("Advertencia\n --Si no llega el mensaje--");

        alertRemember.setMessage("1- Revise bandeja Spam\n2- Revise configuración de Gmail");
        alertRemember.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickAndRemember(fab);
            }
        });
        alertRemember.show();




    }

    //En este método vamos a realizar el check si el editext está vacio
    //Además recogemos el correo para enviar la recuperación y mediante un alertdialog pedimos la contraseña del correo

    private void clickAndRemember(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                forgoten.setError(null);

                String forget = forgoten.getText().toString();

                if (TextUtils.isEmpty(forget)) {

                    forgoten.setError(getString(R.string.empty));
                    forgoten.requestFocus();


                } else {

                    direccionCorreo = forgoten.getText().toString();
                    destintatarioCorreo = forgoten.getText().toString();

                    final AlertDialog.Builder alert = new AlertDialog.Builder(ActivityForgot.this);
                    final EditText edittext = new EditText(ActivityForgot.this);
                    alert.setTitle("Selecciona una opción");
                    alert.setMessage("Introduce la contraseña de tu correo");

                    alert.setView(edittext);

                    alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Editable yourMailPassword = edittext.getText();
                            contrasenyaCorreo=yourMailPassword.toString();
                            dataInDataBase();
                        }
                    });

                    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(getApplicationContext(),"Recuperación no enviada",Toast.LENGTH_SHORT).show();

                        }
                    });

                    alert.show();


                }
            }
        });
    }

    private void dataInDataBase() {


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot chanceData : dataSnapshot.getChildren()) {
                    String keyForChance = chanceData.getKey();


                    assert keyForChance != null;
                    String emailProf = forgoten.getText().toString();
                    passWord = dataSnapshot.child(keyForChance).child(emailProf.replace(".", "-")).child("pass").getValue(String.class);


                    if (passWord != null) {


                        sendMail();

                        Intent fortgot = new Intent(getApplicationContext(), MainActivity.class);
                        Toast.makeText(getApplicationContext(), "Enviado contaseña del administrador ", Toast.LENGTH_SHORT).show();
                        startActivity(fortgot);


                    } else {

                        passWord = dataSnapshot.child(keyForChance).child(emailProf.replace(".", "-")).child("passF").getValue(String.class);

                        if (passWord != null) {

                            sendMail();
                            Intent fortgot = new Intent(getApplicationContext(), MainActivity.class);
                            Toast.makeText(getApplicationContext(), "Enviado contaseña del usuario ", Toast.LENGTH_SHORT).show();
                            startActivity(fortgot);

                        } else {

                            Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();

                        }
                    }


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendMail() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        //Configuramos la sesión
        session = Session.getDefaultInstance(properties, null);

        try {
            enviarMensaje("Recordatorio de contraseña", "Su contraseña es : " + passWord);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }

    public static void enviarMensaje(String subject, String content) throws MessagingException {


        // Configuramos los valores de nuestro mensaje
        mensaje = new MimeMessage(session);
        mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destintatarioCorreo));
        mensaje.setSubject(subject);
        mensaje.setContent(content, "text/html");

        // Configuramos como sera el envio del correo
        transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", direccionCorreo, contrasenyaCorreo);
        transport.sendMessage(mensaje, mensaje.getAllRecipients());
        transport.close();

        // Mostramos que el mensaje se ha enviado correctamente
        System.out.println("--------------------------");
        System.out.println("Mensaje enviado");
        System.out.println("---------------------------");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_back) {

            Intent back = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(back);
        }
        return true;

    }

}
