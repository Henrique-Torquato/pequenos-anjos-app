package com.example.projeto_app_developer_finalversion;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class EsqueciActivity extends AppCompatActivity {

    private EditText editEmail;
    private MaterialButton btnRecuperarSenha;

    // Instância do DBHelper para acessar o banco de dados
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci);

        // Inicializa os componentes da UI
        editEmail = findViewById(R.id.edit_email);
        btnRecuperarSenha = findViewById(R.id.btnRecuperarSenha);

        // Inicializa o DBHelper
        dbHelper = new DBHelper(this);

        // Define o comportamento do botão de recuperação
        btnRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();

                // Verifica se o e-mail foi preenchido
                if (email.isEmpty()) {
                    Toast.makeText(EsqueciActivity.this, "Por favor, insira seu e-mail", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verifica se o e-mail está cadastrado no banco de dados
                if (dbHelper.checkUser(email)) {
                    // E-mail encontrado, mensagem de sucesso
                    Toast.makeText(EsqueciActivity.this, "Link de recuperação enviado para: " + email, Toast.LENGTH_SHORT).show();
                } else {
                    // E-mail não encontrado, mensagem de erro
                    Toast.makeText(EsqueciActivity.this, "E-mail não cadastrado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
