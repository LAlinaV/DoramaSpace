package com.example.filmspace.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.filmspace.R
import com.example.filmspace.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    private lateinit var mBinding:ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.inputName.onFocusChangeListener = this
        mBinding.inputSurname.onFocusChangeListener = this
        mBinding.inputEmail.onFocusChangeListener = this
        mBinding.inputPassword.onFocusChangeListener = this
        mBinding.inputPassword2.onFocusChangeListener = this

        auth = Firebase.auth
        mBinding.buttonReg.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        if(!validateEmail() || !validatePassword() || !validatePasswordConfirmPassword()){
            return
        }
        auth.createUserWithEmailAndPassword(mBinding.inputEmail.text.toString(), mBinding.inputPassword.text.toString()).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, getString(R.string.successfully_singed_up), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            } else {
                Toast.makeText(this, getString(R.string.fail_singed_up), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun validateFirstName(): Boolean {
        var errorMessg:String? = null
        val value: String = mBinding.inputName.text.toString()
        if(value.isEmpty()){
            errorMessg = getString(R.string.name_require)
        }
        if(errorMessg != null){
            mBinding.inputNameL.apply {
                isErrorEnabled = true
                error = errorMessg
            }
        }
        return errorMessg == null
    }

    private fun validateLastName(): Boolean {
        var errorMessg:String? = null
        val value: String = mBinding.inputSurname.text.toString()
        if(value.isEmpty()){
            errorMessg = getString(R.string.surname_require)
        }
        if(errorMessg != null){
            mBinding.inputSurnameL.apply {
                isErrorEnabled = true
                error = errorMessg
            }
        }
        return errorMessg == null
    }

    private fun validateEmail(): Boolean {
        var errorMessg:String? = null
        val value: String = mBinding.inputEmail.text.toString()
        if(value.isEmpty()){
            errorMessg = getString(R.string.email_require)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessg = getString(R.string.email_invalid)
        }
        if(errorMessg != null){
            mBinding.inputEmailL.apply {
                isErrorEnabled = true
                error = errorMessg
            }
        }
        return errorMessg == null
    }

    private fun validatePassword(): Boolean {
        var errorMessg:String? = null
        val value: String = mBinding.inputPassword.text.toString()
        if(value.isEmpty()){
            errorMessg = getString(R.string.password_require)
        } else if (value.length < 6) {
            errorMessg = getString(R.string.password_min_length)
        }
        if(errorMessg != null){
            mBinding.inputPasswordL.apply {
                isErrorEnabled = true
                error = errorMessg
            }
        }
        return errorMessg == null
    }

    private fun validateConfirmPassword(): Boolean {
        var errorMessg:String? = null
        val value: String = mBinding.inputPassword2.text.toString()
        if(value.isEmpty()){
            errorMessg = getString(R.string.confirm_password_require)
        } else if (value.length < 6) {
            errorMessg = getString(R.string.confirm_password_min_length)
        }
        if(errorMessg != null){
            mBinding.inputPassword2L.apply {
                isErrorEnabled = true
                error = errorMessg
            }
        }
        return errorMessg == null
    }

    private fun validatePasswordConfirmPassword(): Boolean {
        var errorMessg:String? = null
        val value1: String = mBinding.inputPassword.text.toString()
        val value2: String = mBinding.inputPassword2.text.toString()
        if(value1 != value2){
            errorMessg = getString(R.string.confirm_password_password)
        }
        if(errorMessg != null){
            mBinding.inputPassword2L.apply {
                isErrorEnabled = true
                error = errorMessg
            }
        }
        return errorMessg == null
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if(v != null){
            when(v.id){
                R.id.inputName -> {
                    if(hasFocus){
                        if(mBinding.inputNameL.isErrorEnabled){
                            mBinding.inputNameL.isErrorEnabled = false
                        }
                    } else {
                        validateFirstName()
                    }
                }
                R.id.inputSurname -> {
                    if(hasFocus){
                        if(mBinding.inputSurnameL.isErrorEnabled){
                            mBinding.inputSurnameL.isErrorEnabled = false
                        }
                    } else {
                        validateLastName()
                    }

                }
                R.id.inputEmail -> {
                    if(hasFocus){
                        if(mBinding.inputEmailL.isErrorEnabled){
                            mBinding.inputEmailL.isErrorEnabled = false
                        }
                    } else {
                        if (validateEmail()) {

                        }
                    }

                }
                R.id.inputPassword -> {
                    if(hasFocus){
                        if(mBinding.inputPasswordL.isErrorEnabled){
                            mBinding.inputPasswordL.isErrorEnabled = false
                        }
                    } else {
                        if (validatePassword() && mBinding.inputPassword2.text!!.isNotEmpty() && validateConfirmPassword() && validatePasswordConfirmPassword()){
                            if(mBinding.inputPasswordL.isErrorEnabled){
                                mBinding.inputPasswordL.isErrorEnabled = false
                            }
                            mBinding.inputPassword2L.setStartIconDrawable(R.drawable.check)
                        }
                    }

                }
                R.id.inputPassword2 -> {
                    if(hasFocus){
                        if(mBinding.inputPassword2L.isErrorEnabled){
                            mBinding.inputPassword2L.isErrorEnabled = false
                        }
                    } else {
                        if(validateConfirmPassword() && validatePassword() && validatePasswordConfirmPassword()) {
                            if(mBinding.inputPasswordL.isErrorEnabled){
                                mBinding.inputPasswordL.isErrorEnabled = false
                            }
                            mBinding.inputPassword2L.apply{
                                setStartIconDrawable(R.drawable.check)
                            }
                        }
                    }

                }
            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}