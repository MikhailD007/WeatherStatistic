package org.vimteam.weatherstatistic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.vimteam.weatherstatistic.databinding.FragmentContactsListBinding
import org.vimteam.weatherstatistic.domain.contracts.ContactsListContract
import org.vimteam.weatherstatistic.domain.models.state.ContactsListState
import org.vimteam.weatherstatistic.ui.adapters.ContactsListAdapter
import org.vimteam.weatherstatistic.ui.interfaces.LoadState

class ContactsListFragment : Fragment() {

    private var _binding: FragmentContactsListBinding? = null
    private val binding get() = _binding!!

    private val contactsListViewModel by viewModel<ContactsListContract.ViewModel>()

    private val contactsAdapter: ContactsListAdapter = ContactsListAdapter(ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.contactsRecyclerView.adapter = contactsAdapter
        contactsListViewModel.contactsListState.observe(viewLifecycleOwner) { renderView(it) }
        contactsListViewModel.getContactsList()
    }

    private fun renderView(state: ContactsListState) {
        when (state) {
            is ContactsListState.Success -> {
                contactsAdapter.contacts.clear()
                contactsAdapter.contacts.addAll(state.contacts)
                contactsAdapter.notifyDataSetChanged()
                (activity as LoadState).setLoadState(false)
            }
            is ContactsListState.Loading -> {
                (activity as LoadState).setLoadState(true)
            }
            is ContactsListState.Error -> {
                (activity as LoadState).setLoadState(false)
                Snackbar
                    .make(requireView(), state.error.toString(), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}