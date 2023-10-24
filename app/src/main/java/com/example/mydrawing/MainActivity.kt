package com.example.mydrawing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mydrawing.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ControlBackward.setOnClickListener {
            binding.PaintArea.setUndo()
        }
        binding.ControlForward.setOnClickListener {
            binding.PaintArea.setRedo()
        }
        binding.ControlDelete.setOnClickListener {
            binding.PaintArea.setdelete()
        }
        binding.ControlSend.setOnClickListener {
            binding.PaintArea.setSaveSend()
        }

    }

}