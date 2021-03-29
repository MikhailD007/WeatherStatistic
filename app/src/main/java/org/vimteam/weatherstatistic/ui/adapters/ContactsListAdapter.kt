package org.vimteam.weatherstatistic.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.vimteam.weatherstatistic.R
import org.vimteam.weatherstatistic.base.inflate
import org.vimteam.weatherstatistic.databinding.ListitemContactBinding
import org.vimteam.weatherstatistic.domain.models.Contact

class ContactsListAdapter(
    val contacts: ArrayList<Contact>,
) : RecyclerView.Adapter<ContactsListAdapter.ContactsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsListViewHolder {
        val inflatedView = parent.inflate(R.layout.listitem_contact, false)
        return ContactsListViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ContactsListViewHolder, position: Int) {
        holder.bindItem(contacts[position])
    }

    override fun getItemCount() = contacts.size

    inner class ContactsListViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val binding = ListitemContactBinding.bind(v)
        private lateinit var contact: Contact

        fun bindItem(contact: Contact) {
            this.contact = contact
            binding.nameTextView.text = contact.name
            binding.phoneTextView.text = contact.phone
        }

    }

}