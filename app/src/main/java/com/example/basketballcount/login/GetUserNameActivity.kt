package com.example.basketballcount.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.basketballcount.MainActivity.Companion.database
import com.example.basketballcount.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_get_user_name.*

class GetUserNameActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_user_name)
        val intent = Intent()
        auth = Firebase.auth
        getNameWatcher()
        go_signup_tv.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        get_user_btn.setOnClickListener {
            if (get_name_et.text.toString().isNotEmpty() && get_password_tv.text.toString()
                    .isNotEmpty()
            ) {
                auth.signInWithEmailAndPassword(
                    get_name_et.text.toString(),
                    get_password_tv.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = Firebase.auth.currentUser
                            var name = ""
                            var resultGson = ""
                            var wingame :Long
                            var losegame :Long
                            user?.let {
                                user.displayName
                                name = user.displayName.toString()
                            }
                            val readUser = user?.email?.let { it1 ->
                                database.collection("users")
                                    .document(it1)
                            }
                            readUser?.get()?.addOnSuccessListener { document ->
                                resultGson = document.data?.get("result_gson") as String
                                wingame = document.data?.get("wingame") as Long
                                losegame = document.data?.get("losegame") as Long
                                intent.putExtra("get_win_fire", wingame)
                                intent.putExtra("get_lose_fire", losegame)
                            }
                            Toast.makeText(applicationContext, "로그인 되었습니다", Toast.LENGTH_SHORT)
                                .show()
                            intent.putExtra("get_name", name)
                            intent.putExtra("get_result", resultGson)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "이메일,비밀번호를 확인해주세요",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(applicationContext, "이메일,비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        finish()
        android.os.Process.killProcess(android.os.Process.myPid())

    }

    private fun getNameWatcher() {
        get_password_tv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (get_name_et.text.toString().isNotEmpty()) {
                    get_user_btn.setBackgroundColor(Color.GREEN)
                } else {
                    get_user_btn.setBackgroundColor(Color.GRAY)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    get_user_btn.setBackgroundColor(Color.GREEN)

                } else {
                    get_user_btn.setBackgroundColor(Color.GRAY)
                }
            }
        })
    }

}