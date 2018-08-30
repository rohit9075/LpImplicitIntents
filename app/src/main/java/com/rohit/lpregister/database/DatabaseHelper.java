package com.rohit.lpregister.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.rohit.lpregister.model.Candidate;

public class DatabaseHelper extends SQLiteOpenHelper {


    private Context mContext;


    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Tables.CREATE_CAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(Tables.DROP_CAN_TABLE);
        onCreate(db);
    }

    public void addCandidate(Candidate candidate, byte [] data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_CANDIDATE_EMAIL, candidate.getEmailId());
        values.put(Constants.COLUMN_CANDIDATE_FIRST_NAME, candidate.getFirstName());
        values.put(Constants.COLUMN_CANDIDATE_LAST_NAME, candidate.getLastName());
        values.put(Constants.COLUMN_CANDIDATE_DATE_OF_BIRTH, candidate.getDateOfBirth());
        values.put(Constants.COLUMN_CANDIDATE_PHONE,candidate.getMobileNumber());
        values.put(Constants.COLUMN_CANDIDATE_PASSWORD, candidate.getPassword());
        values.put(Constants.COLUMN_CANDIDATE_GENDER, candidate.getGender());

        values.put(Constants.COLUMN_CANDIDATE_IMAGE, data);
        // Inserting Row
        db.insert(Constants.TABLE_CANDIDATE, null, values);
        db.close();

        Toast.makeText(mContext, "Registration Successful", Toast.LENGTH_SHORT).show();

    }
    
    
    
    
    public boolean checkCandidate( String email)
    {
        // array of columns to fetch
        String[] columns = {Constants.COLUMN_CANDIDATE_EMAIL};
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = Constants.COLUMN_CANDIDATE_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query candidate table with condition
        /*
         * Here query function is used to fetch records from candidate table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM candidate WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(Constants.TABLE_CANDIDATE,columns,selection,selectionArgs,null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;

    }



    public boolean checkCandidate(String email, String password) {

        // array of columns to fetch
        String[] columns = {Constants.COLUMN_CANDIDATE_EMAIL};
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = Constants.COLUMN_CANDIDATE_EMAIL + " = ?" + " AND " + Constants.COLUMN_CANDIDATE_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query candidate table with conditions
        /*
         * Here query function is used to fetch records from candidate table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM candidate WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(Constants.TABLE_CANDIDATE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();

        return cursorCount > 0;

    }

    public boolean updateCandidate(Candidate candidate, byte[] data) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_CANDIDATE_ID,candidate.getId());
        values.put(Constants.COLUMN_CANDIDATE_EMAIL, candidate.getEmailId());
        values.put(Constants.COLUMN_CANDIDATE_FIRST_NAME, candidate.getFirstName());
        values.put(Constants.COLUMN_CANDIDATE_LAST_NAME, candidate.getLastName());
        values.put(Constants.COLUMN_CANDIDATE_DATE_OF_BIRTH, candidate.getDateOfBirth());
        values.put(Constants.COLUMN_CANDIDATE_PHONE,candidate.getMobileNumber());
        values.put(Constants.COLUMN_CANDIDATE_GENDER, candidate.getGender());

        values.put(Constants.COLUMN_CANDIDATE_IMAGE, data);
        // updating row
        /*db.update(TABLE_CAN, values,null,null);
        db.close();*/

        return db.update(Constants.TABLE_CANDIDATE,
                values,
                Constants.COLUMN_CANDIDATE_EMAIL + "=?",
                new String[]{candidate.getEmailId()}) == 1;

    }

//    public boolean delete(String id)
//    {
//        String[] columns = {COLUMN_CANDIDATE_ID};
//        SQLiteDatabase db= this.getReadableDatabase();
//        String selection = COLUMN_CANDIDATE_ID + " = ?";
//        String[] selectionArgs = {id};
//
//        Cursor cursor=db.query(TABLE_CAN,columns,selection,selectionArgs,null,null,null);
//        int cursorCount =cursor.getCount();
//        cursor.close();
//        if (cursorCount>0)
//        {
//            return true;
//        }
//        return false;
//    }
//    public List<User> getAllUser() {
//        String[] columns = {
//                COLUMN_CANDIDATE_ID,
//                COLUMN_CANDIDATE_FIRST_NAME,
//                COLUMN_CANDIDATE_LAST_NAME,
//                COLUMN_CANDIDATE_DATE_OF_BIRTH,
//                COLUMN_CANDIDATE_EMAIL,
//                COLUMN_CANDIDATE_PHONE,
//                COLUMN_CANDIDATE_PASSWORD,
//
//                COLUMN_CANDIDATE_IMAGE
//        };
//        String sortOrder = COLUMN_CANDIDATE_ID + " ASC";
//        List<User> userList = new ArrayList<User>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_CAN, columns,
//                null,
//                null,
//                null,
//                null,
//                sortOrder);
//
//        if (cursor.moveToFirst()) {
//            do {
//                User candidate = new User();
//                candidate.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_ID))));
//                candidate.setmFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_FIRST_NAME)));
//                candidate.setmLastName(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_LAST_NAME)));
//                candidate.setmEmailId(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_EMAIL)));
//                candidate.setmMobileNo(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_PHONE)));
//                candidate.setmDob(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_DATE_OF_BIRTH)));
//                candidate.setmPassword(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_PASSWORD)));
//
//                userList.add(candidate);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//
//        return userList;
//    }
//
//    public List<User> getAllUserMobileNumber() {
//        String[] columns = {
//                COLUMN_CANDIDATE_PHONE
//        };
////        String sortOrder = COLUMN_CANDIDATE_ID + " ASC";
//        List<User> userList = new ArrayList<User>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_CAN, columns,
//                null,
//                null,
//                null,
//                null,
//                null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                User candidate = new User();
//                candidate.setmMobileNo(cursor.getString(cursor.getColumnIndex(COLUMN_CANDIDATE_PHONE)));
//                userList.add(candidate);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//
//        return userList;
//    }


        public Cursor getCandidateProfile(String email) {



        String[] columns = {
                Constants.COLUMN_CANDIDATE_EMAIL,
                Constants.COLUMN_CANDIDATE_FIRST_NAME,
                Constants.COLUMN_CANDIDATE_LAST_NAME,
                Constants.COLUMN_CANDIDATE_DATE_OF_BIRTH,
                Constants.COLUMN_CANDIDATE_PHONE,
                Constants.COLUMN_CANDIDATE_GENDER,
                Constants.COLUMN_CANDIDATE_IMAGE
        };

            String selection = Constants.COLUMN_CANDIDATE_EMAIL + " = ?";

            // selection arguments
            String[] selectionArgs = {email};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_CANDIDATE, columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        return cursor;
    }
}
