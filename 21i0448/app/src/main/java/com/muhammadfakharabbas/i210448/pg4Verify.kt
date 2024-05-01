package com.muhammadfakharabbas.i210448

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class pg4Verify : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg4verifynum)

        var num = findViewById<TextView>(R.id.num)
        var optET1 = findViewById<EditText>(R.id.optET1)
        var optET2 = findViewById<EditText>(R.id.optET2)
        var optET3 = findViewById<EditText>(R.id.optET3)
        var optET4 = findViewById<EditText>(R.id.optET4)
        var optET5 = findViewById<EditText>(R.id.optET5)
        var optET6 = findViewById<EditText>(R.id.optET6)

        val otpp = optET1.text.toString() +
                optET2.text.toString() +
                optET3.text.toString() +
                optET4.text.toString() +
                optET5.text.toString() +
                optET6.text.toString()


        //
        var verify_btn = findViewById<Button>(R.id.verify_btn)
        var back_btn = findViewById<Button>(R.id.back_btn)

        val otp = intent.getStringExtra("token")
        num.text = intent.getStringExtra("number")

        verify_btn.setOnClickListener {
            val otpp =
                optET1.text.toString() +
                        optET2.text.toString() +
                        optET3.text.toString() +
                        optET4.text.toString() +
                        optET5.text.toString() +
                        optET6.text.toString()

            if (otpp.length == 6) {
                val otpVerificationUrl = "http://192.168.1.6/smd/verify.php"

                val otpVerificationParams = HashMap<String, String>()
                otpVerificationParams["otp"] = otpp

                val requestQueue: RequestQueue = Volley.newRequestQueue(this)
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, otpVerificationUrl, JSONObject(otpVerificationParams as Map<String, String>),
                    Response.Listener { response ->
                        val success = response.getBoolean("success")
                        val message = response.getString("message")
                        if (success) {
                            // OTP verification successful
                            val intent = Intent(this@pg4Verify, Login::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            Toast.makeText(this, "Successful verification", Toast.LENGTH_LONG).show()
                            startActivity(intent)
                        } else {
                            // OTP verification failed
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                )
                requestQueue.add(jsonObjectRequest)
            } else {
                Toast.makeText(this, "Input incomplete for verification", Toast.LENGTH_LONG).show()
            }
        }
        back_btn.setOnClickListener{
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

       //showOtp(optET1)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                // Implement afterTextChanged
            }
        }
        //moveNumber()
        optET1.requestFocus()
        optET1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Implement beforeTextChanged
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    optET2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Implement afterTextChanged
            }
        })
        optET2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Implement beforeTextChanged
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    optET3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Implement afterTextChanged
            }
        })
        optET3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Implement beforeTextChanged
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    optET4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Implement afterTextChanged
            }
        })
        optET4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Implement beforeTextChanged
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    optET5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Implement afterTextChanged
            }
        })
        optET5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Implement beforeTextChanged
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    optET6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Implement afterTextChanged
            }
        })

    }

    }

  /*  private fun showOtp(optET: EditText) {
        optET.requestFocus()

        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(optET, InputMethodManager.SHOW_IMPLICIT)
    }*/


