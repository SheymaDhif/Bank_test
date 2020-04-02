package com.example.testbank.presentation.statements

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testbank.R
import com.example.testbank.adapter.StatementAdapter
import com.example.testbank.di.AppViewModelFactory
import com.example.testbank.domain.models.User
import com.example.testbank.extensions.vm
import com.example.testbank.presentation.auth.SignInActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_statements.*
import javax.inject.Inject

class StatementsActivity : AppCompatActivity() {

    lateinit var statementAdapter: StatementAdapter
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by lazy { vm(viewModelFactory, StatementsViewModel::class) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statements)
        statementAdapter = StatementAdapter()
        statements_rcv.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = statementAdapter
        }

        logout_img.setOnClickListener {
            viewModel.logout()
        }
        viewModel.statementsMutable.observe(this, Observer {
            if (it != null)
                statementAdapter.addStatements(it)
        })

        viewModel.userMutable.observe(this, Observer {
            if (it != null)
                displayUser(it)
        })

        viewModel.errorState.observe(this, Observer {
            if (it != null)
                displayMsg(it.message!!)
        })

        viewModel.logOutMutable.observe(this, Observer {
            if (it) {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        viewModel.getUser()

    }

    private fun displayUser(user: User) {
        name_tv.text = user.name
        account_number.text = "${user.bankAccount} / ${user.agency}"
        balance_value.text = user.balance.toString()
    }

    private fun displayMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unbound()
    }
}


