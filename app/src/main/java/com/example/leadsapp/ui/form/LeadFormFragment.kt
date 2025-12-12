package com.example.leadsapp.ui.form

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.leadsapp.data.Lead
import com.example.leadsapp.databinding.FragmentLeadFormBinding
import com.example.leadsapp.ui.LeadViewModel
import java.io.File

class LeadFormFragment : Fragment() {

    private var _binding: FragmentLeadFormBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LeadViewModel by activityViewModels()
    private val args: LeadFormFragmentArgs by navArgs()

    private var currentPhotoUri: Uri? = null
    private var currentLead: Lead? = null

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success && currentPhotoUri != null) {
                binding.imagePhoto.setImageURI(currentPhotoUri)
            } else {
                Toast.makeText(requireContext(), "No se pudo tomar la foto", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeadFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val leadId = args.leadId
        if (leadId > 0) {
            viewModel.loadLead(leadId)
        }

        viewModel.selectedLead.observe(viewLifecycleOwner) { lead ->
            currentLead = lead
            if (lead != null) {
                binding.editName.setText(lead.name)
                binding.editPhone.setText(lead.phone)
                binding.editEmail.setText(lead.email)
                binding.editNotes.setText(lead.notes)
                if (!lead.photoPath.isNullOrEmpty()) {
                    currentPhotoUri = Uri.parse(lead.photoPath)
                    binding.imagePhoto.setImageURI(currentPhotoUri)
                }
            }
        }

        viewModel.message.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
                if (it.contains("creado") || it.contains("actualizado")) {
                    findNavController().navigateUp()
                }
            }
        }

        binding.buttonTakePhoto.setOnClickListener {
            checkCameraPermissionAndOpen()
        }

        binding.buttonSave.setOnClickListener {
            saveLead()
        }

        binding.buttonSync.setOnClickListener {
            val lead = currentLead
            if (lead != null) {
                viewModel.syncLead(lead)
            } else {
                Toast.makeText(requireContext(), "Guarda el lead antes de sincronizar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkCameraPermissionAndOpen() {
        val permission = Manifest.permission.CAMERA
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(
                    requireContext(),
                    "La cámara es necesaria para tomar la foto del lead",
                    Toast.LENGTH_LONG
                ).show()
                requestCameraPermission.launch(permission)
            }
            else -> requestCameraPermission.launch(permission)
        }
    }

    private fun openCamera() {
        val photoFile = createImageFile()
        val authority = requireContext().packageName + ".fileprovider"
        currentPhotoUri = FileProvider.getUriForFile(requireContext(), authority, photoFile)
        takePicture.launch(currentPhotoUri)
    }

    private fun createImageFile(): File {
        val dir = File(requireContext().filesDir, "images")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "lead_" + System.currentTimeMillis() + ".jpg")
    }

    private fun saveLead() {
        val id = currentLead?.id
        val name = binding.editName.text?.toString().orEmpty()
        val phone = binding.editPhone.text?.toString().orEmpty()
        val email = binding.editEmail.text?.toString()
        val notes = binding.editNotes.text?.toString()
        val photoPath = currentPhotoUri?.toString()

        var isValid = true

        if (name.isBlank()) {
            binding.editName.error = "El nombre es obligatorio"
            isValid = false
        } else {
            binding.editName.error = null
        }

        if (phone.isBlank()) {
            binding.editPhone.error = "El teléfono es obligatorio"
            isValid = false
        } else {
            binding.editPhone.error = null
        }

        if (isValid) {
            viewModel.saveLead(id, name, phone, email, notes, photoPath)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
