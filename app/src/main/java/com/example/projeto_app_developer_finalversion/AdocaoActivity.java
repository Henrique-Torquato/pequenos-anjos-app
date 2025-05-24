package com.example.projeto_app_developer_finalversion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;
import java.util.List;

public class AdocaoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdocaoAdapter adocaoAdapter;
    private List<AdocaoItem> adocaoItemList;
    private FloatingActionButton fabDoacao;
    private String userEmail; // E-mail do usuário, passado pela InicialActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adocao);

        // Recebe o e-mail da InicialActivity
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("USER_EMAIL"); // Recupera o e-mail

        // Verifica se o e-mail foi passado e faz algo com ele
        if (userEmail != null) {
            // Exemplo de exibição do e-mail recebido
            Toast.makeText(this, "Usuário: " + userEmail, Toast.LENGTH_SHORT).show();
        }

        // Configuração do RecyclerView
        recyclerView = findViewById(R.id.adocoesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa a lista de adoções
        adocaoItemList = new ArrayList<>();
        adocaoItemList.add(new AdocaoItem("Jordan Souza", "9 Anos", "Ama jogar futebol e fazer piadas", R.drawable.adocao_image, true));
        adocaoItemList.add(new AdocaoItem("Amanda Silva", "2 Anos", "Ama doces e bagunças", R.drawable.adocao_image1, true));
        adocaoItemList.add(new AdocaoItem("Jaqueline Veneza", "7 Anos", "Ama correr e pular corda", R.drawable.adocao_image2, false));
        adocaoItemList.add(new AdocaoItem("Aline Morais", "6 Anos", "Ama dançar e tirar fotos", R.drawable.adocao_image3, false));

        // Criar o Adapter e associar ao RecyclerView
        adocaoAdapter = new AdocaoAdapter(adocaoItemList, new AdocaoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AdocaoItem adocaoItem) {
                // Implementar ação quando um item for clicado
                Toast.makeText(AdocaoActivity.this, "Criança: " + adocaoItem.getTitulo(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adocaoAdapter);

        // Configuração do BottomAppBar
        BottomAppBar bottomAppBar = findViewById(R.id.app_bar);
        ImageView inicioIcon = findViewById(R.id.imageViewInicio2);
        TextView inicioText = findViewById(R.id.textViewInicio2);
        ImageView carteiraIcon = findViewById(R.id.imageViewCarteira2);
        TextView carteiraText = findViewById(R.id.textViewCarteira2);
        ImageView perfilIcon = findViewById(R.id.imageViewPerfil2);
        TextView perfilText = findViewById(R.id.textViewPerfil2);
        ImageView configIcon = findViewById(R.id.imageViewConfig2);
        TextView configText = findViewById(R.id.textViewConfig2);

        // Definindo comportamento dos itens na barra inferior
        inicioIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vai para a InicialActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("USER_EMAIL_UPDATED", userEmail); // Passa o e-mail de volta para a InicialActivity
                setResult(RESULT_OK, resultIntent); // Retorna o resultado para a InicialActivity
                finish(); // Finaliza a Activity
            }
        });
        inicioText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vai para a InicialActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("USER_EMAIL_UPDATED", userEmail); // Passa o e-mail de volta para a InicialActivity
                setResult(RESULT_OK, resultIntent); // Retorna o resultado para a InicialActivity
                finish(); // Finaliza a Activity
            }
        });

        carteiraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdocaoActivity.this, "Carteira", Toast.LENGTH_SHORT).show();
            }
        });
        carteiraText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdocaoActivity.this, "Carteira", Toast.LENGTH_SHORT).show();
            }
        });

        perfilIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdocaoActivity.this, "Perfil", Toast.LENGTH_SHORT).show();
            }
        });
        perfilText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdocaoActivity.this, "Perfil", Toast.LENGTH_SHORT).show();
            }
        });

        configIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdocaoActivity.this, "Configurações", Toast.LENGTH_SHORT).show();
            }
        });
        configText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdocaoActivity.this, "Configurações", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuração do FloatingActionButton (FAB)
        fabDoacao = findViewById(R.id.floatingActionButton);
        fabDoacao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Vai para a DoacoesActivity
                Intent intent = new Intent(AdocaoActivity.this, DoacoesActivity.class);
                startActivity(intent);
            }
        });
    }

    // Enviar o e-mail de volta para a InicialActivity quando a AdocaoActivity for encerrada
    @Override
    public void onBackPressed() {
        // Criando a Intent para retornar à InicialActivity com o e-mail atualizado
        Intent resultIntent = new Intent();
        resultIntent.putExtra("USER_EMAIL_UPDATED", userEmail); // Passa o e-mail atualizado
        setResult(RESULT_OK, resultIntent); // Define o código de resultado
        finish(); // Finaliza a Activity e retorna para a InicialActivity
    }
}
