package com.oiranca.pglproject.ui.conection;


public class BaseCreate {

    public BaseCreate() {

    }


    public static final String NAME_TABLE = "administradores";
    public static final String ID = "id";
    public static final String COLUMN_NAME = "nombre";
    public static final String COLUMN_SURNAME = "apellidos";
    public static final String COLUMN_MAIL = "correo";
    public static final String COLUMN_PASS = "password";
    public static final String COLUMN_RANGE = "rango";

    public static final String NAME_BASE_F = COLUMN_MAIL;
    public static final String ID_F = "id";
    public static final String COLUMN_NAME_F = "nombre";
    public static final String COLUMN_SURNAME_F = "apellidos";
    public static final String COLUMN_MAIL_F = "correo";
    public static final String COLUMN_PASS_F = "password";
    public static final String COLUMN_RANGE_F = "rango";


    public static final String NAME_TABLE_HW = "homework";
    public static final String ID_HW = "id";
    public static final String COLUMN_MAIL_HW = "correo";
    public static final String COLUMN_WORK = "tarea";
    public static final String COLUMN_DATE = "fecha";
    public static final String COLUMN_CHECK = "realizada";


    public static final String SQL_CREATE_ADMIN =
            "CREATE TABLE " + NAME_TABLE + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_SURNAME + " TEXT," +
                    COLUMN_MAIL + " TEXT UNIQUE," +
                    COLUMN_PASS + " TEXT UNIQUE," +
                    COLUMN_RANGE + " TEXT)";

   public static final String SQL_CREATE_FAM =
            "CREATE TABLE " + BaseCreate.NAME_BASE_F + " (" +
                    BaseCreate.ID_F + " INTEGER PRIMARY KEY," +
                    BaseCreate.COLUMN_NAME_F + " TEXT," +
                    BaseCreate.COLUMN_SURNAME_F + " TEXT," +
                    BaseCreate.COLUMN_MAIL_F + "TEXT UNIQUE," +
                    BaseCreate.COLUMN_PASS_F + "TEXT UNIQUE," +
                    BaseCreate.COLUMN_RANGE_F + "TEXT)";


    public static final String SQL_CREATE_HW =
            "CREATE TABLE " + BaseCreate.NAME_TABLE_HW + " (" +
                    BaseCreate.ID_HW + " INTEGER PRIMARY KEY," +
                    BaseCreate.COLUMN_MAIL_HW + " TEXT," +
                    BaseCreate.COLUMN_WORK + " TEXT," +
                    BaseCreate.COLUMN_DATE + "TEXT," +
                    BaseCreate.COLUMN_CHECK + "TEXT)";

    public static final String SQL_DELETE_AD =

            "DROP TABLE IF EXISTS " + NAME_TABLE;


   public static final String SQL_DELETE_FAM =

            "DROP TABLE IF EXISTS " + BaseCreate.NAME_BASE_F;

    public static final String SQL_DELETE_HW =

            "DROP TABLE IF EXISTS " + BaseCreate.NAME_TABLE_HW;


}
