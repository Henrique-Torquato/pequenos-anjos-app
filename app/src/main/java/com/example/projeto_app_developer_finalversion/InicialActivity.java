package com.example.projeto_app_developer_finalversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class InicialActivity extends AppCompatActivity {


    private DBHelper dbHelper;
    private TextView greetingTextView;
    private String userEmail; // Armazena o e-mail do usuário
    private static final int REQUEST_VOLUNTARIO = 1; // Código de requisição para VoluntarioActivity
    private static final int REQUEST_DOACAO = 2;    // Código de requisição para DoacoesActivity
    private static final int REQUEST_ADOCAO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        dbHelper = new DBHelper(this);

        // Obter o e-mail passado pela MainActivity
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("USER_EMAIL");

        // Atualizar o TextView para exibir a saudação com o nome do usuário
        greetingTextView = findViewById(R.id.textView7);
        if (userEmail != null) {
            String userName = getUserNameFromDatabase(userEmail); // Busca o nome do usuário baseado no e-mail
            if (userName != null) {
                greetingTextView.setText("Bem-vindo, " + userName); // Exibe o nome do usuário
            } else {
                greetingTextView.setText("Usuário não encontrado!");
            }
        } else {
            greetingTextView.setText("Usuário não encontrado!");
        }

        // Configurar o RecyclerView
        RecyclerView recyclerView = findViewById(R.id.viewInicial);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Configurar o Adapter
        List<Trend> trendList = new ArrayList<>();
        trendList.add(new Trend("História da ONG", "Conheça nossa história e como ajudamos diversas crianças", R.drawable.trends));
        trendList.add(new Trend("Ajude Agora", "Doe e faça a diferença na vida de muitas crianças", R.drawable.trends));
        trendList.add(new Trend("Eventos", "Participe dos nossos eventos e ajude", R.drawable.trends));

        TrendAdapter adapter = new TrendAdapter(trendList);
        recyclerView.setAdapter(adapter);

        // Configurar os CardViews para as diversas atividades
        CardView doacoesCard = findViewById(R.id.doacoesCardView);
        doacoesCard.setOnClickListener(view -> {
            Intent intent1 = new Intent(InicialActivity.this, DoacoesActivity.class);
            if (userEmail != null) {
                intent1.putExtra("USER_EMAIL", userEmail); // Passa o e-mail do usuário para a DoacoesActivity
            }
            startActivityForResult(intent1, REQUEST_DOACAO); // Inicia com expectativa de resultado
        });

        CardView voluntarioCard = findViewById(R.id.voluntarioCardView);
        voluntarioCard.setOnClickListener(view -> {
            Intent intent1 = new Intent(InicialActivity.this, VoluntarioActivity.class);
            if (userEmail != null) {
                intent1.putExtra("USER_EMAIL", userEmail); // Passa o e-mail do usuário para a VoluntarioActivity
            }
            startActivityForResult(intent1, REQUEST_VOLUNTARIO); // Inicia com expectativa de resultado
        });

        CardView adocaoCard = findViewById(R.id.adocaoCardView);
        adocaoCard.setOnClickListener(view -> {
            Intent intent1 = new Intent(InicialActivity.this, AdocaoActivity.class);
            if (userEmail != null) {
                intent1.putExtra("USER_EMAIL", userEmail); // Passa o e-mail do usuário para a AdocaoActivity
            }
            startActivityForResult(intent1, REQUEST_ADOCAO); // Inicia com expectativa de resultado
        });

        // Configurar a barra de navegação inferior (Bottom App Bar)
        findViewById(R.id.imageViewInicio).setOnClickListener(v ->
                Toast.makeText(InicialActivity.this, "Tela Inicial", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.imageViewCarteira).setOnClickListener(v ->
                Toast.makeText(InicialActivity.this, "Abrindo Conta do Banco", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.imageViewPerfil).setOnClickListener(v ->
                Toast.makeText(InicialActivity.this, "Perfil Aberto", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.imageViewConfig).setOnClickListener(v ->
                Toast.makeText(InicialActivity.this, "Ajustes", Toast.LENGTH_SHORT).show()
        );

        // FAB (Floating Action Button) - Abre a tela de Doações
        findViewById(R.id.floatingActionButton).setOnClickListener(v -> {
            Intent intent1 = new Intent(InicialActivity.this, DoacoesActivity.class);
            if (userEmail != null) {
                intent1.putExtra("USER_EMAIL", userEmail); // Passa o e-mail do usuário
            }
            startActivityForResult(intent1, REQUEST_DOACAO);
        });
    }

    // Método para tratar os resultados das atividades
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_ADOCAO:
                case REQUEST_VOLUNTARIO:
                case REQUEST_DOACAO:
                    // Recupera o e-mail atualizado
                    userEmail = data.getStringExtra("USER_EMAIL_UPDATED");
                    if (userEmail != null) {
                        String userName = getUserNameFromDatabase(userEmail);
                        greetingTextView.setText("Bem-vindo, " + (userName != null ? userName : "Usuário"));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    // Método para obter o nome do usuário a partir do banco de dados, com base no e-mail
    @SuppressLint("Range")
    private String getUserNameFromDatabase(String userEmail) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT " + DBHelper.COLUMN_NAME + " FROM " + DBHelper.TABLE_USERS +
                        " WHERE " + DBHelper.COLUMN_EMAIL + " = ?",
                new String[]{userEmail}
        );

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)); // Recupera o nome
            cursor.close();
            return name; // Retorna o nome
        }
        cursor.close();
        return null; // Retorna null se não encontrar o usuário
    }
}
