package com.example.leadsapp.ui.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.leadsapp.data.Lead
import com.example.leadsapp.databinding.ItemLeadBinding

class LeadListAdapter(
    private val onClick: (Lead) -> Unit
) : ListAdapter<Lead, LeadListAdapter.ViewHolder>(LeadDiffCallback()) {

    inner class ViewHolder(val binding: ItemLeadBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLeadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lead = getItem(position)
        with(holder.binding) {
            textName.text = lead.name
            textPhone.text = lead.phone
            textSyncStatus.text = if (lead.synced) "Sincronizado" else "Pendiente"
            if (!lead.photoPath.isNullOrEmpty()) {
                imagePhoto.setImageURI(Uri.parse(lead.photoPath))
            } else {
                imagePhoto.setImageResource(android.R.drawable.sym_def_app_icon)
            }
            root.setOnClickListener { onClick(lead) }
        }
    }
}

class LeadDiffCallback : DiffUtil.ItemCallback<Lead>() {
    override fun areItemsTheSame(oldItem: Lead, newItem: Lead): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Lead, newItem: Lead): Boolean {
        return oldItem == newItem
    }
}
