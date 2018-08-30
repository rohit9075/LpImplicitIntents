package com.rohit.lpregister.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.rohit.lpregister.R;

import java.util.Objects;

/**
 * InputValidation java class to validate the user input.
 */


public class InputValidation {

    // Instance variable
    private Context context;

    /**
     * constructor
     *
     * @param context Activity context
     */
  public InputValidation(Context context) {
        this.context = context;
    }

    /**
     * Checking the is TextInputEditText field is filled by the user or Empty
     *
     * @param editText View
     * @param message  String message passed form the calling class or method
     * @return Method is returning the boolean value
     */
  public boolean isInputEditTextFilled(EditText editText, String message) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) {
            editText.setError(message);
            hideKeyboardFrom(editText);
            return true;
        }else {
            editText.setError(null);
            return false;
        }
    }


    /**
     * method to Hide keyboard
     *
     * @param view Widgets
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     * Validating the email id
     *
     * @param editText view
     * @return This method is returning the boolean value
     */
  public   boolean isInputEditTextEmail(EditText editText) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            editText.setError("Invalid Email");
            hideKeyboardFrom(editText);
            return true;
        } else {
            editText.setError(null);
            return false;
        }

    }


    /* *************************************** TextInputEditText Field Validation **************************************  */

    /**
     * @param textInputEditText view
     * @param message this message will be provided by the calling method or class
     * @return method is returning boolean value
     */
  public   boolean isInputTextInputEditTextFilled(TextInputEditText textInputEditText, String message) {
        String value = Objects.requireNonNull(textInputEditText.getText()).toString().trim();
        if (value.isEmpty()) {
            textInputEditText.setError(message);
            hideKeyboardFrom(textInputEditText);
            return true;
        }else {
            textInputEditText.setError(null);
            return false;
        }


    }

    /**
     * This method validates the entered password matched or not.
     * @param firstPassword first view
     * @param secondPassword second view
     * @return boolean
     */

  public  boolean isTextInputEditTextPasswordMatches(TextInputEditText firstPassword, TextInputEditText secondPassword) {
        String value1 = Objects.requireNonNull(firstPassword.getText()).toString().trim();
        String value2 = Objects.requireNonNull(secondPassword.getText()).toString().trim();
        if (!value1.contentEquals(value2)) {

            secondPassword.setError("Password Mismatch");
            hideKeyboardFrom(secondPassword);
            return true;
        }
        else {
            secondPassword.setError(null);
            return false;
        }


    }


    /**
     * Method to check password length
     * @param passwordLength view
     * @param message message provided by the calling method or class
     * @return this method is returning the boolean value.
     */

  public   boolean isPasswordLengthTextInputEditText(TextInputEditText passwordLength, String message) {
        String password = Objects.requireNonNull(passwordLength.getText()).toString().trim();
//        String pattern = "^(?=.*[0-9])(?=.*[!@#$%^&*+=?-]).{8,15}$";
        if(password.length()<=6) {

            passwordLength.setError(message);
            hideKeyboardFrom(passwordLength);
            return true;
        }
        else {
            passwordLength.setError(null);
            return false;
        }
    }


    /**
     * This method validates the radiobutton selected or not
     * @param radioGroup viewGroup
     * @return boolean
     */

   public boolean isRadioButtonSelected(RadioGroup radioGroup, RelativeLayout relativeLayout) {
        if ( radioGroup.getCheckedRadioButtonId() == -1) {

            Snackbar.make(relativeLayout, "Please Select Gender", Snackbar.LENGTH_LONG).show();

            return true;
        }
        return false;
    }

}
