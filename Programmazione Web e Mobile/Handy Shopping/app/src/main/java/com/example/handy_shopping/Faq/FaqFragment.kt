package com.example.handy_shopping.Faq

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handy_shopping.R
import com.example.handy_shopping.databinding.FragmentFaqBinding


class FaqFragment : Fragment() {
    private lateinit var binding : FragmentFaqBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFaqBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val faqItemLists = listOf(
            FaqItem("Q: Come posso cercare un prodotto?", "A: Clicca sulla lente d'ingrandimento in alto a destra nella schermata Home e scrivi il nome del prodotto che cerchi; puoi anche filtrare le tue ricerche in base a parametri quali il costo e la categoria."),
            FaqItem("Q: Posso cambiare la mia password?", "A: Sì, dal tuo profilo clicca il tasto \"Modifica\" in corrispondenza dei tuoi dati. Una volta inserita la tua vecchia password e confermata quella nuova, il sistema effettuerà la modifica richiesta."),
            FaqItem("Q: Posso eliminare il mio account?", "A: Sì, scorri in fondo al tuo profilo e clicca il tasto \"Elimina account\". Attenzione, una volta cancellato il profilo non potrà più essere recuperato!"),
            FaqItem("Q: Posso effettuare il reso di un prodotto?", "A: Sì, scegli il prodotto dalla sezione \"Acquisti\" e clicca il tasto \"Chiedi reso\". Prima assicurati, però, che siano passati meno di 30 giorni dalla data di acquisto, periodo per il quale permane il diritto di recesso."),
            FaqItem("Q: Come posso recensire un prodotto che ho acquistato?", "A: Visita la pagina dedicata al prodotto che hai acquistato e clicca sul tasto \"Scrivi una recensione\". Oltre a condividere la tua esperienza tramite i tuoi commenti, potrai anche assegnare una valutazione che varia da 0.5 a 5 stelle. Sia la recensione che la valutazione potranno essere modificate in qualsiasi momento."),
            FaqItem("Q: Quali sono i metodi di pagamento disponibili?", "A: Puoi sempre pagare in contanti alla consegna o con la carta (previo inserimento dei dati di fatturazione). Nel caso di pagamento con carta potrai scegliere se pagare in soluzione unica o a rate."),
            FaqItem("Q: Non trovo una risposta, come posso contattarvi?", "A: Clicca sul tasto \"Contattaci\" in fondo alla pagina per inviare una richiesta all'assistenza.")
        )

        binding.faqRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.faqRecyclerView.adapter = FaqAdapter(faqItemLists) { isVisible ->
            binding.btnContattaci.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        binding.btnContattaci.setOnClickListener{
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragments, InviaFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }
}