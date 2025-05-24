package com.example.projeto_app_developer_finalversion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VoluntarioActivity extends AppCompatActivity {

    private Button btnCuidados, btnSaude, btnCozinha, btnSignUp;
    private String selectedCategory = null; // Categoria selecionada
    private DBHelper dbHelper; // Instância do banco de dados
    private String userEmail; // E-mail do usuário, passado pela InicialActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voluntarios);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("USER_EMAIL"); // Recupera o e-mail

        // Verifica se o e-mail foi passado e faz algo com ele
        if (userEmail != null) {
            // Exemplo de exibição do e-mail recebido
            Toast.makeText(this, "Usuário: " + userEmail, Toast.LENGTH_SHORT).show();
        }

        // Inicializar os botões de categoria
        btnCuidados = findViewById(R.id.btnCuidados);
        btnSaude = findViewById(R.id.btnSaude);
        btnCozinha = findViewById(R.id.btnCozinha);
        btnSignUp = findViewById(R.id.btnSignUp);

        dbHelper = new DBHelper(this);

        // Recuperar o e-mail do usuário, passado pela InicialActivity
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        if (userEmail == null) {
            Toast.makeText(this, "Erro: e-mail não encontrado!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Configurar botões de categorias
        setupCategoryButton(btnCuidados, "Cuidados");
        setupCategoryButton(btnSaude, "Saúde");
        setupCategoryButton(btnCozinha, "Cozinha");

        // Configurar o botão "Cadastrar-se"
        btnSignUp.setOnClickListener(v -> {
            if (selectedCategory == null) {
                Toast.makeText(this, "Por favor, selecione uma categoria antes de continuar!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Inserir no banco de dados
            boolean isInserted = dbHelper.insertVolunteer(userEmail, selectedCategory);
            if (isInserted) {
                Toast.makeText(this, "Voluntário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                finish(); // Finalizar a Activity
            } else {
                Toast.makeText(this, "Erro ao cadastrar voluntário!", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar a Barra Inferior (Bottom App Bar)
        setupBottomNavBar();

        // Configurar o FAB (Floating Action Button) - Pode ser usado para uma ação especial
        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VoluntarioActivity.this, "Ação do FAB", Toast.LENGTH_SHORT).show();
                // Substituir pelo Intent ou ação desejada
                Intent intent = new Intent(VoluntarioActivity.this, DoacoesActivity.class);
                startActivity(intent);
            }
        });


    }

    /**
     * Configura os botões de categoria para selecionar a categoria
     */
    private void setupCategoryButton(Button button, String category) {
        button.setOnClickListener(v -> {
            // Atualiza a categoria selecionada
            selectedCategory = category;

            // Atualiza o estilo visual dos botões de categoria
            resetButtonStyles();
            button.setBackgroundColor(getResources().getColor(R.color.secondary)); // Muda cor do botão selecionado
            button.setTextColor(getResources().getColor(R.color.primary));

            Toast.makeText(this, "Categoria selecionada: " + category, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Reseta o estilo visual dos botões de categoria
     */
    private void resetButtonStyles() {
        // Resetando as cores dos botões de categoria para o estado padrão
        btnCuidados.setBackgroundColor(getResources().getColor(R.color.primary));
        btnSaude.setBackgroundColor(getResources().getColor(R.color.primary));
        btnCozinha.setBackgroundColor(getResources().getColor(R.color.primary));

        btnCuidados.setTextColor(getResources().getColor(R.color.white));
        btnSaude.setTextColor(getResources().getColor(R.color.white));
        btnCozinha.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * Configura as ações da Barra Inferior (Bottom App Bar)
     */
    private void setupBottomNavBar() {
        // Navegação para a tela inicial
        findViewById(R.id.imageViewInicio2).setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("USER_EMAIL_UPDATED", userEmail); // Passa o e-mail de volta para a InicialActivity
            setResult(RESULT_OK, resultIntent); // Retorna o resultado para a InicialActivity
            finish(); // Finaliza a Activity
        });


        findViewById(R.id.imageViewCarteira2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VoluntarioActivity.this, "Abrindo Carteira", Toast.LENGTH_SHORT).show();
                // Adicionar navegação ou ação específica para "Carteira"
            }
        });

        findViewById(R.id.imageViewPerfil2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VoluntarioActivity.this, "Abrindo Perfil", Toast.LENGTH_SHORT).show();
                // Adicionar navegação ou ação específica para "Perfil"
            }
        });

        findViewById(R.id.imageViewConfig2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VoluntarioActivity.this, "Abrindo Configurações", Toast.LENGTH_SHORT).show();
                // Adicionar navegação ou ação específica para "Configurações"
            }
        });
    }
}