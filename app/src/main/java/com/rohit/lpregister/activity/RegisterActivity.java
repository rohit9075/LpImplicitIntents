package com.rohit.lpregister.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohit.lpregister.R;
import com.rohit.lpregister.database.DatabaseHelper;
import com.rohit.lpregister.model.Candidate;
import com.rohit.lpregister.utils.InputValidation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener{

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1001;
    byte [] mCandidateImageBytes;
    private static final String TAG = "RegisterActivity";



    Candidate mCandidate;
    String  mRegisterGender;
    DatabaseHelper mDataBaseHelper;
    private EditText mEditTextFirstName,mEditTextLastName,mEditTextEmail,mEditTextMobile,mEditTextDob;
    private TextInputEditText mTextInputEditTextPassword, mTextInputEditTextConfirmPassword;
    private Button mButtonRegister;
    private TextView mTextViewAlreadyMember;
    // ImageView Instance Variable
    private ImageView mImageViewCandidateImage;
    // RadioGroup object Declaration.
    private RadioGroup mRadioGroupGender;
    // RadioButton object Declaration.
    private RadioButton mRadioButton;
    private InputValidation mInputValidation;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView(); // initView() method call

        clickListener(); // clickListener(); method call

        initObject(); // initObject method call

    }

    /**
     * clickListener() method definition
     */

    private void clickListener() {

        mButtonRegister.setOnClickListener(this);

        mTextViewAlreadyMember.setOnClickListener(this);

        mImageViewCandidateImage.setOnClickListener(this);

        mRadioGroupGender.setOnCheckedChangeListener(this);
    }

    /**
     * intiView Method definition
     */
    private void initView() {

        mEditTextFirstName = findViewById(R.id.editText_first_name);
        mEditTextLastName = findViewById(R.id.editText_last_name);
        mEditTextEmail = findViewById(R.id.editText_email);
        mEditTextMobile = findViewById(R.id.editText_mobile_number);
        mTextInputEditTextPassword = findViewById(R.id.editText_password);
        mTextInputEditTextConfirmPassword = findViewById(R.id.editText_confirm_password);
        mEditTextDob = findViewById(R.id.editText_dob);

        mButtonRegister = findViewById(R.id.button_register);

        mTextViewAlreadyMember = findViewById(R.id.textView_already_member);

        mImageViewCandidateImage = findViewById(R.id.imageview);

        mRadioGroupGender = findViewById(R.id.radioGroupP_gender);

        mRelativeLayout = findViewById(R.id.relative_layout);


    }

    public void initObject(){

        mInputValidation = new InputValidation(this);

        mDataBaseHelper = new DatabaseHelper(this);
    }

    /**
     * OnClick() method implementation
     * @param view as a parameter
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.button_register:

                inputFieldValidation();

                break;

            case R.id.textView_already_member:
                break;

            case R.id.imageview:
                checkPermissions();
                break;
        }

    }

    /**
     * getData() method definition
     */
    public void getData(){


        if (mRadioGroupGender != null) {

            mRegisterGender = mRadioButton.getText().toString().trim();
        }

        // candidate object instantiation
        mCandidate = new Candidate();

        // storing candidate data into candidate object

        mCandidate.setFirstName(mEditTextFirstName.getText().toString().trim());
        mCandidate.setLastName(mEditTextLastName.getText().toString().trim());
        mCandidate.setEmailId(mEditTextEmail.getText().toString().trim());
        mCandidate.setMobileNumber(mEditTextMobile.getText().toString().trim());
        mCandidate.setDateOfBirth(mEditTextDob.getText().toString().trim());
        mCandidate.setPassword(Objects.requireNonNull(mTextInputEditTextPassword.getText()).toString().trim());
        mCandidate.setGender(mRegisterGender);

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        // getting the checked button id.
        mRadioButton = group.findViewById(checkedId);
    }

    public void inputFieldValidation() {

        // Checking the First Name Field
        if (mInputValidation.isInputEditTextFilled(mEditTextFirstName, "First Name Required")) {
            return;
        }
        // Checking the Last Name Field
        if (mInputValidation.isInputEditTextFilled(mEditTextLastName, "Last Name Required")) {
            return;
        }

        // Checking the Email Empty Field
        if (mInputValidation.isInputEditTextFilled(mEditTextEmail, "Email Required")) {
            return;
        }

        // Checking the valid email or not Field
        if (mInputValidation.isInputEditTextEmail(mEditTextEmail)) {
            return;
        }

        // Checking the Mobile Number Field
        if (mInputValidation.isInputEditTextFilled(mEditTextMobile, "Mobile Number Required")) {
            return;
        }

        // Checking the DOB Field
        if (mInputValidation.isInputEditTextFilled(mEditTextDob, "DOB Required")) {
            return;
        }

        // Checking the RadioButton
        if (mInputValidation.isRadioButtonSelected(mRadioGroupGender, mRelativeLayout)) {
            return;
        }


        // Checking the Password Field
        if (mInputValidation.isInputTextInputEditTextFilled(mTextInputEditTextPassword, "Password Required")) {
            return;
        }

        // Checking the Confirm Password field Field
        if (mInputValidation.isInputTextInputEditTextFilled(mTextInputEditTextConfirmPassword, "Confirm Password  Required")) {
            return;
        }


        // Checking the matching both password Field
        if (mInputValidation. isTextInputEditTextPasswordMatches(mTextInputEditTextPassword, mTextInputEditTextConfirmPassword)) {
            return;
        }


        getData();   //getData() method call

        postDataToDatabase(); // posting candidate data into database


    }

    /**
     * postDataToDatabase() method definition
     */
    private void postDataToDatabase() {

        if(!mDataBaseHelper.checkCandidate(mEditTextEmail.getText().toString().trim()))
        {

            mDataBaseHelper.addCandidate(mCandidate,mCandidateImageBytes);

//            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
//            intent.putExtra("EMAIL", mEmailId.getText().toString().trim());
//            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Email Already Exists",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * method to initiate request for permissions.
     */

    private void checkPermissions() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?

            // requesting for the permission again

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }


    /**
     * Handle the permission result.
     * @param requestCode permission request code
     * @param permissions number of permission requested
     * @param grantResults permission granted or not
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();


                    // Starting the cropping activity
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(RegisterActivity.this);

                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                        showStoragePermissionRationale();
                        //should provide the explanation for the permission
                        Log.d(TAG, "onRequestPermissionsResult: waiting for permission");
                    } else {
                        Toast.makeText(this, "Should grant permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            // other permission

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
