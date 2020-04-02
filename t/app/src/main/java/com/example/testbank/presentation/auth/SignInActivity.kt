package com.example.testbank.presentation.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.example.testbank.R
import com.example.testbank.di.AppViewModelFactory
import com.example.testbank.extensions.vm
import com.example.testbank.presentation.statements.StatementsActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {


    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by lazy { vm(viewModelFactory, SignInViewModel::class) }

    override fun onStart() {
        super.onStart()
        viewModel.getUserDetails()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        user.doAfterTextChanged { text ->
            viewModel.onChange(
                text.toString(),
                password.text.toString()
            );
        }
        password.doAfterTextChanged { text ->
            viewModel.onChange(
                user.text.toString(),
                text.toString()
            );
        }
        login_btn.setOnClickListener {
            viewModel.signIn(
                user.text.toString(),
                password.text.toString()
            )
        }
        logo_img.setOnClickListener {
            user.setText("test_user")
            password.setText("Test@1")
        }
        viewModel.showProgress.observe(
            this,
            Observer {
                if (it == true)
                    progress.visibility = View.VISIBLE
                else
                    progress.visibility = View.GONE
            })
        viewModel.userAccount.observe(
            this,
            Observer { if (it != null) goToMainActivity() else displayError() })

        viewModel.userExist.observe(
            this,
            Observer { if (it) goToMainActivity() })

        viewModel.errorState.observe(this, Observer { throwable ->
            throwable?.let {
                Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.btnSelected.observe(this, Observer { login_btn.isEnabled = it })
    }

    private fun displayError() {
        Toast.makeText(this, "error signin", Toast.LENGTH_SHORT).show()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, StatementsActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unbound()
    }
}
