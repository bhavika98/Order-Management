package com.app.incroyable.order_management.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.incroyable.order_management.R
import com.app.incroyable.order_management.databinding.ActivityStartBinding
import com.app.incroyable.order_management.utils.isInternetAvailable

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)

        binding.btnStart.setOnClickListener {
            redirect()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showNoInternetDialog() {
        val builder = AlertDialog.Builder(this)
        val layoutInflater = layoutInflater
        val view1 = layoutInflater.inflate(R.layout.dialog_no_internet, null)
        val dpi = resources.displayMetrics.density
        builder.setView(view1, 0, 0, 0, 0)
        builder.setCancelable(false)
        val retryButton = view1.findViewById<TextView>(R.id.btnRetry)
        val dismissButton = view1.findViewById<ImageView>(R.id.btnDismiss)

        val alertDialog = builder.create()

        retryButton.setOnClickListener {
            alertDialog.dismiss()
            redirect()
        }

        dismissButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun redirect() {
        if (!isInternetAvailable()) {
            showNoInternetDialog()
        } else {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }
}
