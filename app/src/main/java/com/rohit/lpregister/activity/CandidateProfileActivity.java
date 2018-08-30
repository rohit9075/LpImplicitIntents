package com.rohit.lpregister.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.rohit.lpregister.R;
import com.rohit.lpregister.database.Constants;
import com.rohit.lpregister.database.DatabaseHelper;
import com.rohit.lpregister.model.Candidate;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CandidateProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener, RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener{


    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1001;
    private static final String TAG = "CandidateProfile";
    byte [] mCandidateImageBytes;

    private EditText mEditTextFirstName,mEditTextLastName,mEditTextEmail,mEditTextMobile,mEditTextDob;

    private ImageView mImageViewCandidateImage;

    private RadioGroup mRadioGroupGender;
    // RadioButton object Declaration.
    private RadioButton mRadioButton , mRadioButtonMale, mRatioButtonFemale;


    // Switch referenceVariable
    private Switch mEditUpdateSwitch;

    private DatabaseHelper mDatabaseHelper;

    private ImageView mHeaderImageViewProfileImage;

    private TextView mHeaderTextViewName , mHeaderTextViewEmail;

    NavigationView navigationView;
    View header;

    Candidate candidate;

    static String mEmailFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);

        header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        initView(); // initView() method call

        clickListener(); // clickListener(); method call

        initObject(); // initObject method call

        mEmailFromIntent = getIntent().getStringExtra("email");

        getCandidateDataFromDb(mEmailFromIntent);

        mImageViewCandidateImage.setEnabled(false);


    }


    /**
     * clickListener() method definition
     */

    private void clickListener() {

     mEditUpdateSwitch.setOnCheckedChangeListener(this);
        mImageViewCandidateImage.setOnClickListener(this);
        mRadioGroupGender.setOnCheckedChangeListener(this);

    }

    /**
     * intiView Method definition
     */
    private void initView() {

        mEditTextFirstName = findViewById(R.id.edittext_update_first_name);
        mEditTextLastName = findViewById(R.id.edittext_update_last_name);
        mEditTextEmail = findViewById(R.id.edittext_update_email);
        mEditTextMobile = findViewById(R.id.edittext_update_mobile_number);
        mEditTextDob = findViewById(R.id.edittext_update_date_of_birth);

        mImageViewCandidateImage = findViewById(R.id.update_imageview);

        mRadioGroupGender = findViewById(R.id.radioGroup_update_gender);

        mRadioButtonMale = findViewById(R.id.radioButton_update_male);
        mRatioButtonFemale = findViewById(R.id.radioButton_update_female);

        mEditUpdateSwitch = findViewById(R.id.toggle_editCandidate);

        mHeaderImageViewProfileImage = header.findViewById(R.id.header_imageView);

        mHeaderTextViewName = header.findViewById(R.id.header_name);
        mHeaderTextViewEmail = header.findViewById(R.id.header_email);


    }

    public void initObject(){

        // database helper object instantiation
      mDatabaseHelper = new DatabaseHelper(this);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.candidate_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

       switch (v.getId()){
           case R.id.update_imageview:
               openImageFile();
               break;
       }

    }

    private void openImageFile() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(CandidateProfileActivity.this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        // getting the checked button id.
        mRadioButton = group.findViewById(checkedId);
    }


    /**
     * getData() method definition
     */
    public void getData(){

        String mRegisterGender =null;

        candidate = new Candidate();

        if (mRadioGroupGender != null) {

            mRegisterGender = mRadioButton.getText().toString().trim();
        }

        candidate.setEmailId(mEditTextEmail.getText().toString().trim());
        candidate.setFirstName(mEditTextFirstName.getText().toString().trim());
        candidate.setLastName(mEditTextLastName.getText().toString().trim());
        candidate.setDateOfBirth(mEditTextDob.getText().toString().trim());
        candidate.setMobileNumber(mEditTextMobile.getText().toString().trim());
        candidate.setGender(mRegisterGender);

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.toggle_editCandidate){

            if (isChecked){
                enableEditText();
            }
            else {


                getData();
                boolean status = mDatabaseHelper.updateCandidate(candidate,mCandidateImageBytes);
                disableEditText();
                getCandidateDataFromDb(mEmailFromIntent);
                if (status){
                    Toast.makeText(this, "Data updated Successfully", Toast.LENGTH_SHORT).show();
                }
//                updateCandidateProfile();

            }
        }
    }


    public void enableEditText(){

        mEditTextFirstName.setEnabled(true);
        mEditTextLastName.setEnabled(true);
        mEditTextDob.setEnabled(true);
        mEditTextMobile.setEnabled(true);
        mRatioButtonFemale.setClickable(true);
        mRadioButtonMale.setClickable(true);
        mImageViewCandidateImage.setEnabled(true);

    }

    public void disableEditText(){

        mEditTextFirstName.setEnabled(false);
        mEditTextLastName.setEnabled(false);
        mEditTextDob.setEnabled(false);
        mEditTextMobile.setEnabled(false);

        mRatioButtonFemale.setClickable(false);
        mRadioButtonMale.setClickable(false);
        mImageViewCandidateImage.setEnabled(false);

    }

   public void getCandidateDataFromDb(String email) {

       Cursor cursor = mDatabaseHelper.getCandidateProfile(email);

       if (cursor.moveToFirst()) {
           do {

               String Email  = (cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CANDIDATE_EMAIL)));
               mEditTextEmail.setText(Email);
               mHeaderTextViewEmail.setText(Email);

               String mFirstName = (cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CANDIDATE_FIRST_NAME)));
               String mLastName  = (cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CANDIDATE_LAST_NAME)));

               mEditTextFirstName.setText(mFirstName);
               mEditTextLastName.setText(mLastName);
               String mFistAndLastName = mFirstName.concat(" ").concat(mLastName);

               mHeaderTextViewName.setText(mFistAndLastName);

               mEditTextDob.setText(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CANDIDATE_DATE_OF_BIRTH)));
               String gender = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CANDIDATE_GENDER));
               mEditTextMobile.setText(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CANDIDATE_PHONE)));

               if ("Male".equals(gender)){
                   mRadioButtonMale.setChecked(true);
               }else {
                   mRatioButtonFemale.setChecked(true);
               }

               byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(Constants.COLUMN_CANDIDATE_IMAGE));

               if (byteArray != null) {
                   Bitmap bp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                   mImageViewCandidateImage.setImageBitmap(bp);
                   mHeaderImageViewProfileImage.setImageBitmap(bp);
               }

           } while (cursor.moveToNext());

       }
   }




    /**
     * method to handle image result
     * @param requestCode image request code
     * @param resultCode result status
     * @param data intent data
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                Uri resultUri = result.getUri();

                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inSampleSize = 4;

                try {
                 Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    Paint paint = new Paint();
                    paint.setShader(shader);
                    paint.setAntiAlias(true);
                    Canvas c = new Canvas(circleBitmap);
                    c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);

                    mImageViewCandidateImage.setImageBitmap(circleBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // converting the image to bytes Arrays

                mImageViewCandidateImage.setDrawingCacheEnabled(true);
                mImageViewCandidateImage.buildDrawingCache();
                Bitmap bitmap=mImageViewCandidateImage.getDrawingCache();
                ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

                mCandidateImageBytes = outputStream.toByteArray();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, "onActivityResult: " + error);
            }
        }
    }
}
