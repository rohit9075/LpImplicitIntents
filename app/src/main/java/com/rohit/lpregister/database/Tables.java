package com.rohit.lpregister.database;

class Tables {

  static final String CREATE_CAN_TABLE = "CREATE TABLE " + Constants.TABLE_CANDIDATE + " ( " +
            Constants.COLUMN_CANDIDATE_EMAIL + " TEXT PRIMARY KEY," +
            Constants.COLUMN_CANDIDATE_FIRST_NAME + " TEXT," +
            Constants.COLUMN_CANDIDATE_LAST_NAME + " TEXT," +
            Constants.COLUMN_CANDIDATE_DATE_OF_BIRTH + " TEXT," +
            Constants.COLUMN_CANDIDATE_PHONE + " TEXT," +
            Constants.COLUMN_CANDIDATE_PASSWORD + " TEXT," +
            Constants.COLUMN_CANDIDATE_GENDER + " TEXT," +
            Constants.COLUMN_CANDIDATE_IMAGE + " BLOB" + ")";

  static final String DROP_CAN_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_CANDIDATE;
}
