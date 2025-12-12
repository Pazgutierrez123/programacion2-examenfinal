package com.example.leadsapp.ui.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leadsapp.R
import com.example.leadsapp.data.Lead
import com.example.leadsapp.databinding.FragmentLeadListBinding
import com.example.leadsapp.ui.LeadViewModel

class LeadListFragment : Fragment() {

    private var _binding: FragmentLeadListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LeadViewModel by activityViewModels()

    private lateinit var adapter: LeadListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeadListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ⭐ Inicializar adapter con callback para editar lead
        adapter = LeadListAdapter { lead ->
            navigateToForm(lead)
        }

        // ⭐ Configurar RecyclerView
        binding.recyclerLeads.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLeads.adapter = adapter

        // ⭐ FAB -> crear nuevo lead
        binding.fabAdd.setOnClickListener {
            navigateToForm(null)
        }

        // ⭐ OBSERVAR LA BD DE ROOM EN VIVO (CORREGIDO)
        viewModel.leads.observe(viewLifecycleOwner) { leads ->
            adapter.submitList(leads)
        }
    }

    private fun navigateToForm(lead: Lead?) {
        val action = LeadListFragmentDirections
            .actionLeadListFragmentToLeadFormFragment(lead?.id ?: -1)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_lead_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logs -> {
                findNavController()
                    .navigate(R.id.action_leadListFragment_to_logsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
