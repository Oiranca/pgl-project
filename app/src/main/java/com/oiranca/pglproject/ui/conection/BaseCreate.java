package com.oiranca.pglproject.ui.conection;


public class BaseCreate {


    public BaseCreate() {

    }


    public static final String NAME_TABLE = "admin";
    public static final String ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_MAIL = "mail";
    public static final String COLUMN_PASS = "password";
    public static final String COLUMN_GRADE = "grade";

    // crea columna de realcion entre tabalas administradores y familiares
    public static final String NAME_TABLE_F = "family";
    public static final String ID_F = "family_id";
    public static final String COLUMN_NAME_F = "family_name";
    public static final String COLUMN_SURNAME_F = "family_surname";
    public static final String COLUMN_MAIL_F = "family_mail";
    public static final String COLUMN_PASS_F = "password";
    public static final String COLUMN_GRADE_F = "grade";
    public static final String COLUMN_ID_ADM = "id_admin";


    public static final String NAME_TABLE_HW = "homework";
    public static final String ID_HW = "work_id";
    public static final String COLUMN_MAIL_HW = "work_mail";
    public static final String COLUMN_WORK = "task";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CHECK = "done";


    public static final String SQL_CREATE_ADMIN =
            "CREATE TABLE " + NAME_TABLE + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_SURNAME + " TEXT," +
                    COLUMN_MAIL + " TEXT UNIQUE," +
                    COLUMN_PASS + " TEXT UNIQUE," +
                    COLUMN_GRADE + " TEXT)";

    public static final String SQL_CREATE_FAM =
            "CREATE TABLE " + NAME_TABLE_F + " (" +
                    ID_F + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_F + " TEXT," +
                    COLUMN_SURNAME_F + " TEXT," +
                    COLUMN_MAIL_F + " TEXT UNIQUE," +
                    COLUMN_PASS_F + " TEXT UNIQUE," +
                    COLUMN_GRADE_F +" TEXT,"+ COLUMN_ID_ADM + " INTEGER)";


    public static final String SQL_CREATE_HW =
            "CREATE TABLE " + BaseCreate.NAME_TABLE_HW + " (" +
                    ID_HW + " INTEGER PRIMARY KEY," +
                    COLUMN_MAIL_HW + " TEXT," +
                    COLUMN_WORK + " TEXT," +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_CHECK + " TEXT)";

    public static final String SQL_DELETE_AD =

            "DROP TABLE IF EXISTS " + NAME_TABLE;


    public static final String SQL_DELETE_FAM =

            "DROP TABLE IF EXISTS " + NAME_TABLE_F;

    public static final String SQL_DELETE_HW =

            "DROP TABLE IF EXISTS " + NAME_TABLE_HW;


}
