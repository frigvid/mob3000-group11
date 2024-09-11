package no.usn.mob3000.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.usn.mob3000.databinding.FragmentHomeBinding
import no.usn.mob3000.databinding.FragmentNewsBinding
import no.usn.mob3000.ui.news.NewsViewModel

/**
 * A Fragment for displaying news content in an Android application.
 *
 * This class extends Fragment and is responsible for creating and managing the UI
 * for the news section of the app. It uses View Binding for efficient view access
 * and a ViewModel to manage UI-related data.
 *
 * The fragment inflates a layout (FragmentHomeBinding), observes data from a ViewModel,
 * and updates a TextView accordingly.
 *
 * Note, this is a bit over explained so it can serve as a reference to the other
 * fragments that will be created.
 *
 * @author frigvid
 * @created 2024-09-11
 */
class NewsFragment : Fragment() {
    /* Nullable binding object to access views efficiently. */
    private var _binding: FragmentNewsBinding? = null

    /**
     * Non-null binding property that's only valid between onCreateView and onDestroyView.
     * This approach ensures type-safe view access and helps prevent memory leaks.
     */
    private val binding get() = _binding!!

    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * This method inflates the layout, sets up the ViewModel, and binds the UI components.
     * It also observes changes in the ViewModel and updates the UI accordingly.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* Initialize the ViewModel using ViewModelProvider. */
        val newsViewModel =
            ViewModelProvider(this)[NewsViewModel::class.java]

        /* Inflate the layout for this fragment. */
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /* Get reference to the TextView from the binding. */
        val textView: TextView = binding.textNews

        /* Observe the text LiveData from the ViewModel and update the TextView. */
        newsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    /**
     * Called when the view hierarchy associated with the fragment is being removed.
     *
     * This method nullifies the binding object to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}