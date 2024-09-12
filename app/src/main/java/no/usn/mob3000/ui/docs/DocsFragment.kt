package no.usn.mob3000.ui.docs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.usn.mob3000.databinding.FragmentDocsBinding
import no.usn.mob3000.ui.news.NewsViewModel
import no.usn.mob3000.ui.news.NewsFragment

/**
 * Docs fragment.
 *
 * @author frigvid
 * @created 2024-09-11
 * @see NewsViewModel
 * @see NewsFragment
 */
class DocsFragment : Fragment() {
    /* Nullable binding object to access views efficiently. */
    private var _binding: FragmentDocsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* Initialize the ViewModel using ViewModelProvider. */
        val docsViewModel =
            ViewModelProvider(this)[DocsViewModel::class.java]

        /* Inflate the layout for this fragment. */
        _binding = FragmentDocsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /* Get reference to the TextView from the binding. */
        val textView: TextView = binding.textDocs

        /* Observe the text LiveData from the ViewModel and update the TextView. */
        docsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}