package com.example.basketballcount

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_get_user_name.*
import org.w3c.dom.Text

class GetUserNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_user_name)
        val intent= Intent()
        getNameWatcher()
        get_user_btn.setOnClickListener {
            if(get_name_et.text.toString().isNotEmpty()){
                intent.putExtra("get_name",get_name_et.text.toString())
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
            else{
                Toast.makeText(applicationContext,"이름을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        finish()
        android.os.Process.killProcess(android.os.Process.myPid())

    }
    private fun getNameWatcher(){
        get_name_et.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(get_name_et.text.toString().isNotEmpty()){
                    get_user_btn.setBackgroundColor(Color.GREEN)
                }
                else{
                    get_user_btn.setBackgroundColor(Color.GRAY)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count>0){
                    get_user_btn.setBackgroundColor(Color.GREEN)

                }
                else{
                    get_user_btn.setBackgroundColor(Color.GRAY)
                }
            }
        })
    }

}