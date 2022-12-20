package jx.lessons.firebasesmschatwithmvvm.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jx.lessons.firebasesmschatwithmvvm.databinding.ActivitySmsBinding

class SmsActivity : AppCompatActivity() {
    lateinit var binding:ActivitySmsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}