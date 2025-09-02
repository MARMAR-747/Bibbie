package com.example.handy_shopping.Faq

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.handy_shopping.databinding.FragmentInviaBinding


class InviaFragment : Fragment() {

    private lateinit var binding : FragmentInviaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInviaBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnInvia.setOnClickListener{
            val testo = binding.editText.text.toString()
            if(testo.isEmpty()){Toast.makeText(context, "Inserisci una domanda!", Toast.LENGTH_SHORT).show()
            }else {
                binding.editText.text.clear()
                Toast.makeText(context, "Domanda inviata!", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }
    }

}