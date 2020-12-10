package com.example.basketballcount.login

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.basketballcount.MainActivity.Companion.database
import com.example.basketballcount.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var makePasswordCheck = false
    var checkPasswordCheck = false
    private var checkDrawble: Drawable? = null
    private var baseDrawble: Drawable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        checkDrawble = ContextCompat.getDrawable(applicationContext,
            R.drawable.ic_baseline_done_24
        )
        baseDrawble = ContextCompat.getDrawable(
            applicationContext,
            R.drawable.ic_baseline_check_circle_outline_24
        )
        baseDrawble?.setBounds(1, 0, baseDrawble!!.intrinsicWidth, baseDrawble!!.intrinsicHeight)
        checkDrawble?.setBounds(0, 0, checkDrawble!!.intrinsicWidth, checkDrawble!!.intrinsicHeight)
        passwordWatcher()
        checkPasswordWatcher()
        auth= Firebase.auth
        signup_button.setOnClickListener {
            if(signup_get_id.text.toString().isNotEmpty()&&makePasswordCheck&&checkPasswordCheck&&signup_get_nickname.text.toString().isNotEmpty()&&signup_get_password.text.toString().isNotEmpty()){
                auth.createUserWithEmailAndPassword(signup_get_id.text.toString(),signup_get_password.text.toString())
                    .addOnCompleteListener(this){
                        task->
                        if(task.isSuccessful){
                            val user=Firebase.auth.currentUser
                            val profileUpdate= userProfileChangeRequest {
                                displayName=signup_get_nickname.text.toString()
                            }
                            user!!.updateProfile(profileUpdate)
                                .addOnCompleteListener{
                                    task->
                                    if(task.isSuccessful){
                                        val user= hashMapOf(
                                            "result_gson" to "",
                                            "losegame" to 0,
                                            "wingame" to 0
                                        )
                                        database.collection("users").document(signup_get_id.text.toString()).set(user).addOnSuccessListener{
                                            Toast.makeText(this,"회원가입 되었습니다",Toast.LENGTH_SHORT).show()
                                            finish()
                                        }
                                            .addOnFailureListener {
                                                Toast.makeText(this,"오류가 발생했습니다",Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                    else{
                                        Toast.makeText(this,"이미 존재하는 계정입니다",Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }else{
                            Toast.makeText(this,"오류가 발생했습니다",Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
    }
    private fun passwordWatcher() {
        signup_get_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                makeErrorCheck()
                checkErrorCheck()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                makeErrorCheck()
                checkErrorCheck()
            }

        }
        )
    }

    private fun checkPasswordWatcher() {
        signup_check_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkErrorCheck()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkErrorCheck()
            }

        }
        )
    }

    private fun makeErrorCheck() {
        if (signup_get_password.text.toString().length in 7..20) {
            signup_get_password_layout.isErrorEnabled = false
            signup_get_password_layout.error = null
            makePasswordCheck = true
        } else {
            signup_get_password_layout.isErrorEnabled = true
            signup_get_password_layout.error = "비밀번호의 길이를 확인하세요"
            makePasswordCheck = false
        }
    }

    private fun checkErrorCheck() {
        if (signup_get_password.text.toString() != signup_check_password.text.toString()) {
            checkPasswordCheck = false
            if (checkDrawble != null) {
                DrawableCompat.setTint(checkDrawble!!, Color.RED)
            }

            signup_check_password_layout.error = "비밀번호가 다릅니다"
        } else if (signup_get_password.text.toString() == signup_check_password.text.toString() && makePasswordCheck) {
            signup_check_password_layout.error = null
            checkPasswordCheck = true
            if (checkDrawble != null) {
                DrawableCompat.setTint(checkDrawble!!, Color.GREEN)
            }

        }
    }
}