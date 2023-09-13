package com.example.restaurantteamsdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.restaurantteamsdb.R
import com.example.restaurantteamsdb.application.RestaurantTeamsDBApp
import com.example.restaurantteamsdb.data.TeamRepository
import com.example.restaurantteamsdb.data.db.model.TeamEntity
import com.example.restaurantteamsdb.databinding.TeamDialogBinding
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch
import java.io.IOException


class TeamDialog(
    private val newTeam: Boolean = true,
    private var team: TeamEntity = TeamEntity(
        name = "",
        manager = "",
        country = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit,
): DialogFragment() {

    private var _binding: TeamDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: TeamRepository

    private var spinner: Spinner? = null




    //Se configura el dialogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = TeamDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as RestaurantTeamsDBApp). repository

        builder = AlertDialog.Builder(requireContext())

        binding.tietTitle.setText(team.name)
        binding.tietGenre.setText(team.manager)
        spinner = binding.spCountry



        val listCountries = arrayOf(getString(R.string.selecciona_un_pais),getString(R.string.suecia), getString(
                    R.string.india), getString(R.string.korea_del_sur), getString(R.string.italia), getString(
                                R.string.brazil), getString(R.string.canada))

        var adaptador: ArrayAdapter<String> = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, listCountries)
        spinner?.adapter = adaptador

        dialog = if (newTeam){
            buildDialog(getString(R.string.boton_guardar), getString(R.string.boton_cancelar), {
                //Create (Guardar)
                team.name = binding.tietTitle.text.toString()
                team.manager = binding.tietGenre.text.toString()
                //team.country = binding.tietDeveloper.text.toString()
                team.country = binding.spCountry.selectedItem.toString()

                try {
                    lifecycleScope.launch {
                        repository.insertTeam(team)
                    }
                    message(getString(R.string.exito_al_guardar_el_equipo))
                    updateUI()
                }catch (e: IOException){
                    e.printStackTrace()
                    message(getString(R.string.error_al_guardar_el_equipo))
                }

            }, {
                //Cancelar
            })
        }else {
            buildDialog(getString(R.string.boton_actualizar), getString(R.string.boton_borrar), {
                //Update
                team.name = binding.tietTitle.text.toString()
                team.manager = binding.tietGenre.text.toString()
//                team.country = binding.tietDeveloper.text.toString()
                team.country = binding.spCountry.selectedItem.toString()

                try {
                    lifecycleScope.launch {
                        repository.updateTeam(team)
                    }
                    message(getString(R.string.equipo_actualizado_exitosamente))
                    updateUI()
                }catch (e: IOException){
                    e.printStackTrace()
                    message(getString(R.string.error_al_actualizar_el_equipo))
                }

            }, {
                //Delete
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirmacion_dialog))
                    .setMessage(getString(R.string.realmente_deseas_eliminar_el_equipo) + {team.name} + "?")
                    .setPositiveButton(getString(R.string.aceptar)) { _, _ ->
                        try {
                            lifecycleScope.launch {
                                repository.deleteTeam(team)
                            }
                            message(getString(R.string.equipo_eliminado_exitosamente))

                            updateUI()
                        } catch (e: IOException) {
                            e.printStackTrace()
                            message(getString(R.string.error_al_eliminar_el_equipo))
                        }
                    }
                    .setNegativeButton(getString(R.string.boton_cancelar)){dialog,_ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            })
        }

        return dialog
    }

    // Cuando se destruye el fragment
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private var fieldsChanged = false

    override fun onStart() {
        super.onStart()

        val alertDialog = dialog as AlertDialog
        saveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false
        val adapter = binding.spCountry.adapter as ArrayAdapter<String>



        val textWatchers = arrayOf(
            binding.tietTitle,
            binding.tietGenre
        )

        for (editText in textWatchers) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    fieldsChanged = true
                    saveButton?.isEnabled = validateFields()
                }
            })
        }

        // Agrega un OnItemSelectedListener al Spinner
//        binding.spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                // Este método se llama cuando se selecciona un elemento del Spinner
//                val selectedCountry = binding.spCountry.selectedItem.toString()
//                fieldsChanged = true // Establece fieldsChanged como true
//                saveButton?.isEnabled = validateFields()
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                // Este método se llama cuando no se ha seleccionado ningún elemento
//            }
//        }
    }

//        binding.spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                // Este método se llama cuando se selecciona un elemento del Spinner
//                val selectedCountry = binding.spCountry.selectedItem.toString()
//                fieldsChanged = true
//                // Habilitar el botón "Actualizar" si los campos "Name", "Manager" y el Spinner no están vacíos
//                saveButton?.isEnabled = validateFields()
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                // Este método se llama cuando no se ha seleccionado ningún elemento
//            }


private fun validateFields() =
            (binding.tietTitle.text.toString().isNotEmpty() &&
                    binding.tietGenre.text.toString().isNotEmpty()) // Verifica que se haya seleccionado un país





//    private fun  validateFields(position:Int) =
//        (binding.tietTitle.text.toString().isNotEmpty() &&
//                binding.tietGenre.text.toString().isNotEmpty() &&
//                binding.spCountry.selectedItemPosition != position)

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle(getString(R.string.equipo))
            .setPositiveButton(btn1Text, DialogInterface.OnClickListener{ dialog, which ->
                //Acc
                positiveButton()
            })
            .setNegativeButton(btn2Text){ _, _ ->
                negativeButton()
            }
            .create()


}

