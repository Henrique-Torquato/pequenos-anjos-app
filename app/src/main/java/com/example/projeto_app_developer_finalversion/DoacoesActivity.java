package com.example.projeto_app_developer_finalversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DoacoesActivity extends AppCompatActivity {

    private EditText editTextNome, editTextEmail, editTextEndereco, editTextDoacao;
    private Spinner spinnerFormaPagamento;
    private RadioGroup radioGroupPagamento;
    private RadioButton radioPagamentoUnico, radioPagamentoRecorrente;
    private Button buttonConfirmarDoacao;
    private String userEmail; // E-mail do usuário, passado pela InicialActivity

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doacoes);

        // Recebe o e-mail da InicialActivity
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("USER_EMAIL"); // Recupera o e-mail

        // Verifica se o e-mail foi passado e faz algo com ele
        if (userEmail != null) {
            // Exemplo de exibição do e-mail recebido
            Toast.makeText(this, "Usuário: " + userEmail, Toast.LENGTH_SHORT).show();
        }

        // Inicializando os elementos da interface
        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextEndereco = findViewById(R.id.editTextEndereco);
        editTextDoacao = findViewById(R.id.editTextDoacao);

        spinnerFormaPagamento = findViewById(R.id.spinnerFormaPagamento);
        radioGroupPagamento = findViewById(R.id.radioGroupPagamento);
        radioPagamentoUnico = findViewById(R.id.radioPagamentoUnico);
        radioPagamentoRecorrente = findViewById(R.id.radioPagamentoRecorrente);
        buttonConfirmarDoacao = findViewById(R.id.buttonConfirmarDoacao);

        // Configurando o Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.formas_pagamento,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormaPagamento.setAdapter(adapter);

        // Preenche o e-mail e busca o nome no banco de dados
        if (userEmail != null) {
            editTextEmail.setText(userEmail); // Preenche o campo de e-mail

            // Busca nome do usuário no banco
            DBHelper dbHelper = new DBHelper(this);
            String userName = dbHelper.getUserNameByEmail(userEmail);

            if (userName != null) {
                editTextNome.setText(userName); // Preenche o campo de nome
            } else {
                Toast.makeText(this, "Nome não encontrado para o e-mail fornecido.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "E-mail do usuário não recebido.", Toast.LENGTH_SHORT).show();
        }

        // Configurando o botão de confirmação de doações
        buttonConfirmarDoacao.setOnClickListener(v -> confirmarDoacao());

        // FAB (Floating Action Button) - Navega para outra atividade ou realiza ação
        findViewById(R.id.floatingActionButton).setOnClickListener(v -> {
            Toast.makeText(DoacoesActivity.this, "Ação do FAB clicada", Toast.LENGTH_SHORT).show();
        });

        // Barra inferior
        findViewById(R.id.imageView13).setOnClickListener(v -> {
            // Navegar para InicialActivity ao clicar em "Iniciar"
            Intent resultIntent = new Intent();
            resultIntent.putExtra("USER_EMAIL_UPDATED", userEmail); // Passa o e-mail de volta para a InicialActivity
            setResult(RESULT_OK, resultIntent); // Retorna o resultado para a InicialActivity
            finish(); // Finaliza a Activity
        });

        findViewById(R.id.imageView15).setOnClickListener(v -> {
            // Exibir texto "Carteira"
            Toast.makeText(DoacoesActivity.this, "Carteira clicada", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.imageView11).setOnClickListener(v -> {
            // Exibir texto "Perfil"
            Toast.makeText(DoacoesActivity.this, "Perfil clicado", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.imageView12).setOnClickListener(v -> {
            // Exibir texto "Config"
            Toast.makeText(DoacoesActivity.this, "Config clicado", Toast.LENGTH_SHORT).show();
        });
    }

    private void confirmarDoacao() {
        // Obtendo os valores dos campos
        String nome = editTextNome.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String endereco = editTextEndereco.getText().toString().trim();
        String valorDoacao = editTextDoacao.getText().toString().trim();
        String formaPagamento = spinnerFormaPagamento.getSelectedItem().toString();
        int pagamentoSelecionado = radioGroupPagamento.getCheckedRadioButtonId();

        String tipoPagamento = "";
        if (pagamentoSelecionado == R.id.radioPagamentoUnico) {
            tipoPagamento = "Único";
        } else if (pagamentoSelecionado == R.id.radioPagamentoRecorrente) {
            tipoPagamento = "Recorrente";
        }

        // Validações
        if (nome.isEmpty() || email.isEmpty() || endereco.isEmpty() || valorDoacao.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "E-mail inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorDoacao);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor de doação inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (valor <= 0) {
            Toast.makeText(this, "O valor da doação deve ser maior que zero", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerFormaPagamento.getSelectedItemPosition() == 0) { // Supondo que a primeira posição seja um valor padrão
            Toast.makeText(this, "Por favor, selecione uma forma de pagamento", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pagamentoSelecionado == -1) { // Nenhuma opção de pagamento foi selecionada
            Toast.makeText(this, "Por favor, selecione o tipo de pagamento", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inserindo no banco de dados
        DBHelper dbHelper = new DBHelper(this);
        String dataAtual = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        boolean sucesso = dbHelper.insertPayment(email, valor, formaPagamento, tipoPagamento, dataAtual);

        if (sucesso) {
            // Recupera o total de doações do usuário
            double totalDoacoes = dbHelper.getTotalDoacoesByEmail(email);

            Toast.makeText(this, String.format(
                            "Parabéns, você já ajudou com: R$ %.2f", totalDoacoes),
                    Toast.LENGTH_LONG
            ).show();

            limparCampos();
        } else {
            Toast.makeText(this, "Erro ao registrar a doação. Tente novamente.", Toast.LENGTH_SHORT).show();
        }

    }

    // Método para limpar os campos após confirmação
    private void limparCampos() {
        editTextDoacao.setText("");
        spinnerFormaPagamento.setSelection(0);
        radioGroupPagamento.clearCheck();
    }

}