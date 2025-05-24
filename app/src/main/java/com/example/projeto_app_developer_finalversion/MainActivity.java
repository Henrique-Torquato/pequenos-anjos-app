package com.example.projeto_app_developer_finalversion;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private CheckBox checkBoxRemember;
    private MaterialButton btnLogin;
    private TextView textViewCadastrar, textViewEsqueciSenha;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Inicializar os componentes do layout
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        checkBoxRemember = findViewById(R.id.checkBoxRemember);
        btnLogin = findViewById(R.id.btnLogin);
        textViewCadastrar = findViewById(R.id.textViewCadastrar);
        textViewEsqueciSenha = findViewById(R.id.textViewEsqueciSenha);

        // Criar o banco de dados
        dbHelper = new DBHelper(this);

        // Ação do botão de login
        btnLogin.setOnClickListener(view -> validateLogin());

        // Ação do TextView de "Cadastre-se"
        textViewCadastrar.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

        // Ação do TextView de "Esqueci minha senha"
        textViewEsqueciSenha.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EsqueciActivity.class);
            startActivity(intent);
        });
    }


    private void validateLogin() {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) {
            Toast.makeText(this, "Erro ao acessar o banco de dados", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = null;
        try {
            cursor = db.query(DBHelper.TABLE_USERS, null,
                    DBHelper.COLUMN_EMAIL + " = ? AND " + DBHelper.COLUMN_PASSWORD + " = ?",
                    new String[]{email, password}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show();

                // Se o usuário selecionou "Lembrar", salvar o e-mail no SharedPreferences
                if (checkBoxRemember.isChecked()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USER_EMAIL", email); // Salvar o e-mail
                    editor.apply();
                }

                decideNextActivity(email);  // Redirecionar para a próxima activity
            } else {
                Toast.makeText(this, "E-mail ou senha incorretos", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Erro ao acessar banco de dados: " + e.getMessage());
            Toast.makeText(this, "Erro ao acessar banco de dados", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void decideNextActivity(String email) {
        // Redirecionar para InicialActivity independentemente do tipo de usuário
        Intent intent = new Intent(MainActivity.this, InicialActivity.class);

        // Passar o e-mail para a próxima Activity
        intent.putExtra("USER_EMAIL", email);
        startActivity(intent);
        finish();
    }


    // Método para adicionar um usuário ao banco de dados, mas deve ser utilizado somente para cadastros, não de forma automática
    private void addUserToDatabase(String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_EMAIL, email);
        values.put(DBHelper.COLUMN_PASSWORD, password);
        try {
            db.insertOrThrow(DBHelper.TABLE_USERS, null, values);
        } catch (SQLException e) {
            Toast.makeText(this, "Erro ao adicionar usuário", Toast.LENGTH_SHORT).show();
        }
    }
}
