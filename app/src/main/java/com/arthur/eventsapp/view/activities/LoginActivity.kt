package com.arthur.eventsapp.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.arthur.eventsapp.R
import com.arthur.eventsapp.data.domain.DTO.LoginDTO
import com.arthur.eventsapp.util.Constants.TOKEN
import com.arthur.eventsapp.util.MyApplication
import com.arthur.eventsapp.util.SharedPreferences
import com.arthur.eventsapp.util.openActivity
import com.arthur.eventsapp.viewmodel.AllViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    lateinit var viewModel: AllViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(AllViewModel::class.java)

        btn_login.setOnClickListener {

            if (til_login.text.toString().isNotEmpty() &&
                til_password.text.toString().isNotEmpty()
            ) {
                if (emailValidator(til_login.text.toString())) {
                    if (til_password.length() >= 8) {
                        viewModel.login(
                            LoginDTO(
                                til_login.text.toString(),
                                til_password.text.toString()
                            )
                        ).observe(this, Observer {
                            if (it.data == null) {
                                Snackbar.make(
                                    login,
                                    it.error.toString(),
                                    Snackbar.LENGTH_LONG
                                )
                                    .show()
                            } else {
                                val sharedPref = this!!.getSharedPreferences("App", 0)
                                sharedPref.edit().remove("TOKEN").apply()

                                it.data

                                openActivity<MainActivity>()

                                SharedPreferences.getInstance(MyApplication.appContext!!)
                                    .saveString(TOKEN, it.data.authToken)
                            }
                        })
                    } else
                        Snackbar.make(
                            login,
                            "Sua senha precisa ter mais de 8 digitos!",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                } else
                    Snackbar.make(
                        login,
                        "Email invalido!",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
            } else
                Snackbar.make(
                    login,
                    "Preencha todos os campos!",
                    Snackbar.LENGTH_LONG
                )
                    .show()
        }
    }

    fun emailValidator(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    override fun onBackPressed() {
       super.onBackPressed()
    }

}