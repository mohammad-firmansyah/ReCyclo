package com.zeroone.recyclo.ui.authentication.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.zeroone.recyclo.dataStore
import com.zeroone.recyclo.databinding.ActivityLoginBinding
import com.zeroone.recyclo.model.SessionPreference
import com.zeroone.recyclo.ui.authentication.register.RegisterActivity
import com.zeroone.recyclo.ui.home.HomeActivity
import com.zeroone.recyclo.utils.LoadingBar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var vm : LoginViewModel
    private lateinit var loading : LoadingBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = SessionPreference.getInstance(dataStore)

        vm = ViewModelProvider(this, ViewModelFactory(pref)).get(
            LoginViewModel::class.java
        )

        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";

        loading = LoadingBar(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        vm.isLoading.observe(this) {
            showLoading(it)
        }

        vm.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { it1 ->
                Snackbar.make(
                    window.decorView.rootView,
                    it1,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }



        vm.status.observe(this){
            if (it) {
                finish()
                startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
            }
        }

        vm.status.observe(this){
            if (it) {
                Log.d("tok en01",it.toString())
            }
        }

        binding.registerBtn.setOnClickListener{
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }

        binding.login.setOnClickListener{
            vm.login(binding.loginEmail.text.toString(),binding.loginPassword.text.toString())
        }

        binding.loginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(!s.toString().isNullOrEmpty()){
                    if (!s.toString().matches(emailRegex.toRegex())){
                        binding.loginEmail.setError( "email tidak sesuai format")
                        return
                    }
                }else{
                    binding.loginEmail.setError( "email tidak boleh kosong")
                    return
                }
            }

        })


    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loading.startLoading()
        } else {
            loading.isDismiss()
        }
    }


}