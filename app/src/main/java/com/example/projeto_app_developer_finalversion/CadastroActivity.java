package com.example.projeto_app_developer_finalversion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class CadastroActivity extends AppCompatActivity {

    private EditText editName, editEmail, editPassword;
    private DBHelper dbHelper;
    private MaterialButton btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro); // Certifique-se de que esse é o nome correto do seu layout

        // Inicializa os campos de entrada e o DBHelper
        editName = findViewById(R.id.edit_nome);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        dbHelper = new DBHelper(this);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        // Define o clique do botão para cadastrar o usuário
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                // Log para verificar os dados antes de processá-los
                Log.d("Cadastro", "Tentando cadastrar usuário: ");
                Log.d("Cadastro", "Nome: " + name);
                Log.d("Cadastro", "Email: " + email);
                Log.d("Cadastro", "Senha: " + password);

                // Valida os dados inseridos
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
                } else {
                    // Verifica se o e-mail já está cadastrado
                    if (dbHelper.checkUser(email)) {
                        Toast.makeText(CadastroActivity.this, "Este e-mail já está cadastrado", Toast.LENGTH_SHORT).show();
                    } else {
                        // Insere o novo usuário no banco de dados
                        boolean isInserted = dbHelper.insertUser(name, email, password);
                        if (isInserted) {
                            Log.d("Cadastro", "Usuário cadastrado com sucesso.");
                            Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                            finish(); // Finaliza a Activity e retorna para a tela anterior
                        } else {
                            Log.d("Cadastro", "Erro ao tentar cadastrar usuário.");
                            Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
