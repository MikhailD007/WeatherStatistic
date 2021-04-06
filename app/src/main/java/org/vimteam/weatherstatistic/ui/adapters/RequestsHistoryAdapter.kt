package org.vimteam.weatherstatistic.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.vimteam.weatherstatistic.R
import org.vimteam.weatherstatistic.base.formatShort
import org.vimteam.weatherstatistic.base.inflate
import org.vimteam.weatherstatistic.databinding.ListitemRequestHistoryBinding
import org.vimteam.weatherstatistic.domain.models.RequestHistory

class RequestsHistoryAdapter(
    val requestsHistory: ArrayList<RequestHistory>,
    private val fragment: OnItemClickListener
): RecyclerView.Adapter<RequestsHistoryAdapter.RequestHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHistoryViewHolder {
        val inflatedView = parent.inflate(R.layout.listitem_request_history, false)
        return RequestHistoryViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RequestHistoryViewHolder, position: Int) {
        holder.bindItem(requestsHistory[position])
    }

    override fun getItemCount() = requestsHistory.size

    inner class RequestHistoryViewHolder(v: View): RecyclerView.ViewHolder(v) {

        private val binding = ListitemRequestHistoryBinding.bind(v)
        private lateinit var requestHistory: RequestHistory

        fun bindItem(requestHistory: RequestHistory) {
            this.requestHistory = requestHistory
            binding.placeNameTextView.text = requestHistory.place.name
            binding.periodTextView.text = "${requestHistory.dateFrom.formatShort()} - ${requestHistory.dateTo.formatShort()}"
            itemView.setOnClickListener {
                fragment.onItemClick(requestHistory)
            }
        }

    }

    interface OnItemClickListener {

        fun onItemClick(requestHistory: RequestHistory)

    }
}