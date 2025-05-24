package com.example.projeto_app_developer_finalversion;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper"; // Tag para logs
    private static final String DB_NAME = "app_database"; // Nome do banco de dados
    private static final int DB_VERSION = 7; // Versão do banco de dados atualizada

    // Constantes para a tabela de usuários
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_IS_LOGGED_IN = "is_logged_in"; // Novo campo

    // Constantes para a tabela de pagamentos
    public static final String TABLE_PAYMENTS = "payments";
    public static final String COLUMN_PAYMENT_ID = "payment_id";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_PAYMENT_METHOD = "payment_method";
    public static final String COLUMN_PAYMENT_TYPE = "payment_type";
    public static final String COLUMN_PAYMENT_DATE = "payment_date";

    // Constantes para a tabela de voluntários
    public static final String TABLE_VOLUNTEERS = "volunteers";
    public static final String COLUMN_VOLUNTEER_ID = "volunteer_id";
    public static final String COLUMN_VOLUNTEER_USER_EMAIL = "user_email";
    public static final String COLUMN_CATEGORY = "category";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Criar tabela de usuários com o campo is_logged_in
            String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                    + COLUMN_NAME + " TEXT NOT NULL, "
                    + COLUMN_EMAIL + " TEXT PRIMARY KEY, "
                    + COLUMN_PASSWORD + " TEXT NOT NULL, "
                    + COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0);"; // Novo campo para indicar login
            db.execSQL(createUsersTable);

            // Criar tabela de pagamentos
            String createPaymentsTable = "CREATE TABLE " + TABLE_PAYMENTS + " ("
                    + COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USER_EMAIL + " TEXT NOT NULL, "
                    + COLUMN_AMOUNT + " REAL NOT NULL, "
                    + COLUMN_PAYMENT_METHOD + " TEXT NOT NULL, "
                    + COLUMN_PAYMENT_TYPE + " TEXT NOT NULL, "
                    + COLUMN_PAYMENT_DATE + " TEXT NOT NULL, "
                    + "FOREIGN KEY(" + COLUMN_USER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + "));";
            db.execSQL(createPaymentsTable);

            // Criar tabela de voluntários
            String createVolunteersTable = "CREATE TABLE " + TABLE_VOLUNTEERS + " ("
                    + COLUMN_VOLUNTEER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_VOLUNTEER_USER_EMAIL + " TEXT NOT NULL, "
                    + COLUMN_CATEGORY + " TEXT NOT NULL, "
                    + "FOREIGN KEY(" + COLUMN_VOLUNTEER_USER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + "));";
            db.execSQL(createVolunteersTable);

        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar tabelas: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (oldVersion < 6) {
                // Adicionar o novo campo is_logged_in na tabela users se estiver em uma versão anterior
                String alterUsersTable = "ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0;";
                db.execSQL(alterUsersTable);
            }
            if (oldVersion < 7) {
                // Criar tabela de voluntários
                String createVolunteersTable = "CREATE TABLE " + TABLE_VOLUNTEERS + " ("
                        + COLUMN_VOLUNTEER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_VOLUNTEER_USER_EMAIL + " TEXT NOT NULL, "
                        + COLUMN_CATEGORY + " TEXT NOT NULL, "
                        + "FOREIGN KEY(" + COLUMN_VOLUNTEER_USER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + "));";
                db.execSQL(createVolunteersTable);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar o banco de dados: " + e.getMessage());
        }
    }

    // Método para inserir um usuário com verificação de duplicação
    public boolean insertUser(String name, String email, String password) {
        if (checkUser(email)) {
            Log.d(TAG, "Usuário já existe com o email: " + email);
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_IS_LOGGED_IN, 0); // Inicialmente, o usuário não está logado

        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    // Método para verificar se um usuário já existe
    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
            return cursor != null && cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Método para inserir um pagamento
    public boolean insertPayment(String userEmail, double amount, String paymentMethod, String paymentType, String paymentDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_EMAIL, userEmail);
        contentValues.put(COLUMN_AMOUNT, amount);
        contentValues.put(COLUMN_PAYMENT_METHOD, paymentMethod);
        contentValues.put(COLUMN_PAYMENT_TYPE, paymentType);
        contentValues.put(COLUMN_PAYMENT_DATE, paymentDate);

        long result = db.insert(TABLE_PAYMENTS, null, contentValues);
        return result != -1;
    }

    // Método para buscar pagamentos de um usuário específico
    public Cursor getPaymentsByUser(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PAYMENTS + " WHERE " + COLUMN_USER_EMAIL + " = ?", new String[]{userEmail});
    }

    // Método para marcar um usuário como logado
    public void setUserLoggedIn(String email, boolean isLoggedIn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_LOGGED_IN, isLoggedIn ? 1 : 0); // Atualiza o status de login
        db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email});
    }

    // Método para verificar se o usuário está logado
    public boolean isUserLoggedIn(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_IS_LOGGED_IN + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int isLoggedIn = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_LOGGED_IN));
            cursor.close();
            return isLoggedIn == 1; // Retorna true se o usuário estiver logado
        }
        return false; // Retorna false se o usuário não estiver logado
    }

    // Método para recuperar o e-mail do último usuário cadastrado
    @SuppressLint("Range")
    public String getLastUserEmailFromDatabase() {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT " + COLUMN_EMAIL + " FROM " + TABLE_USERS + " ORDER BY " + COLUMN_EMAIL + " DESC LIMIT 1",
                null
        );

        if (cursor.moveToFirst()) {
            String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)); // Recupera o e-mail
            cursor.close();
            return email; // Retorna o e-mail
        }
        cursor.close();
        return null; // Retorna null se não encontrar o usuário
    }

    // Método para inserir um voluntário
    public boolean insertVolunteer(String userEmail, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VOLUNTEER_USER_EMAIL, userEmail);
        contentValues.put(COLUMN_CATEGORY, category);

        long result = db.insert(TABLE_VOLUNTEERS, null, contentValues);
        return result != -1;
    }

    // Método para buscar o nome do usuário pelo e-mail
    public String getUserNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email}
        );

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            cursor.close();
            return name;
        }

        if (cursor != null) {
            cursor.close();
        }

        return null; // Retorna null caso não encontre o nome
    }

    public double getTotalDoacoesByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_PAYMENTS +
                " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0); // Obtém o valor da soma
            }
            cursor.close();
        }

        db.close();
        return total;
    }


}
