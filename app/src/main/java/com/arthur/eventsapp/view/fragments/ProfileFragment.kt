package com.arthur.eventsapp.view.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.arthur.eventsapp.R
import com.arthur.eventsapp.data.domain.DTO.CpfDTO
import com.arthur.eventsapp.util.CpfCnpjUtil
import com.arthur.eventsapp.util.MaskCpfCnpj
import com.arthur.eventsapp.viewmodel.AllViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private val CAMERA = 1
    private val REQUEST_IMAGE_CAPTURE = 1

    var appPermission = arrayOf<String>(
        Manifest.permission.CAMERA
    )

    lateinit var viewModel: AllViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(AllViewModel::class.java)

        til_cpf.addTextChangedListener(
            MaskCpfCnpj.insert(
                til_cpf
            )
        )

        btn_save.setOnClickListener {
            if (CpfCnpjUtil.isValid(til_cpf.text.toString())) {
                var cpf = MaskCpfCnpj.unmask(til_cpf.text.toString())

                viewModel.updateCpf(
                    CpfDTO(
                        cpf
                    )
                )
                    .observe(this, Observer {
                        if (it.data == null) {
                            Snackbar.make(
                                profile,
                                it.error.toString(),
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        } else {
                            Snackbar.make(profile, "CPF ATUALIZADO!", Snackbar.LENGTH_LONG)
                                .show()
                        }
                    })

            } else
                Snackbar.make(profile, "Digite um CPF valido!", Snackbar.LENGTH_LONG)
                    .show()
        }

        viewModel.user()
            .observe(this, Observer {
                if (it.data == null) {
                    Snackbar.make(
                        profile,
                        it.error.toString(),
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                } else {
                    Picasso
                        .get()
                        .load(it.data.photo)
                        .into(img_photoUser)
                    tv_nameUser.text = it.data.name
                    tv_emailUser.text = it.data.email
                    tv_descriptionUser.text = it.data.description
                }
            })


        if (checkPermissions()) {
            ic_photo.isEnabled
        }

        ic_photo.setOnClickListener {
            cameraIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA) {
            val imageBitmap: Bitmap = data!!.extras!!.get("data") as Bitmap
            img_photoUser.setImageBitmap(imageBitmap)
        }
    }

    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        else {
            ActivityCompat.requestPermissions(
                getActivity()!!,
                appPermission,
                100
            )
        }
      return false
    }

    fun cameraIntent() {
        val tirarFoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(tirarFoto, CAMERA)
    }
}
