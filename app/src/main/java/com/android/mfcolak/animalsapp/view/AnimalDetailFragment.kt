package com.android.mfcolak.animalsapp.view

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.android.mfcolak.animalsapp.R
import com.android.mfcolak.animalsapp.databinding.FragmentAnimalDetailBinding
import com.android.mfcolak.animalsapp.databinding.SmsDialogBinding
import com.android.mfcolak.animalsapp.model.Animal
import com.android.mfcolak.animalsapp.model.AnimalPalette
import com.android.mfcolak.animalsapp.model.SmsInfo
import com.android.mfcolak.animalsapp.util.createPlaceholder
import com.android.mfcolak.animalsapp.util.downloadImage
import com.android.mfcolak.animalsapp.viewModel.AnimalDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class AnimalDetailFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentAnimalDetailBinding
    private lateinit var viewModel: AnimalDetailViewModel
    private var sendSmsStart: Boolean = false
    private var currentAnimal: Animal? = null

    private var animalId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_animal_detail, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        arguments?.let {
            animalId = AnimalDetailFragmentArgs.fromBundle(it).animalId
        }
        viewModel = ViewModelProvider(this)[AnimalDetailViewModel::class.java]
        viewModel.checkRoomData(animalId)

        observeLiveData()

    }

    private fun observeLiveData() {
        viewModel.animalLiveData.observe(viewLifecycleOwner, Observer { animal ->
            currentAnimal = animal
            animal?.let { it ->

                setupBackgroundColor(animal.animalImage)

                binding.animalName.text = it.animalName
                binding.animalLocation.text = it.animalLocation
                binding.animalLifespan.text = it.animalLifeSpan
                context?.let {
                    binding.animalImage.downloadImage(animal.animalImage, createPlaceholder(it))
                }
            }
        })
    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource).generate { palette ->
                        val intColor = palette?.mutedSwatch?.rgb ?: 0
                        val myPalette = AnimalPalette(intColor)
                        binding.palette = myPalette
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.send_sms -> {
                if (!sendSmsStart) {
                    sendSmsStart = true
                    (activity as MainActivity).checkSmsPermission()
                }
                true
            }
            R.id.share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog")
                intent.putExtra(
                    Intent.EXTRA_TEXT, "${currentAnimal?.animalName} " +
                            "location : ${currentAnimal?.animalLocation}" +
                            "life span: ${currentAnimal?.animalLifeSpan}"
                )
                intent.putExtra(Intent.EXTRA_STREAM, currentAnimal?.animalImage)
                startActivity(Intent.createChooser(intent, "Share with"))
                true
            }
            else -> return false
        }
    }

    fun onPermissionResult(permissionGranded: Boolean) {
        if (sendSmsStart && permissionGranded) {
            context?.let {
                val smsInfo = SmsInfo(
                    "",
                    "${currentAnimal?.animalName} location: ${currentAnimal?.animalLocation} life span: ${currentAnimal?.animalLifeSpan}",
                    currentAnimal?.animalImage
                )

                val dialogBinding = DataBindingUtil.inflate<SmsDialogBinding>(
                    LayoutInflater.from(it),
                    R.layout.sms_dialog,
                    null,
                    false
                )
                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") { _, _ ->
                        if (!dialogBinding.destination.text.isNullOrEmpty()) {
                            smsInfo.to = dialogBinding.destination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel") { _, _ -> }
                    .show()

                dialogBinding.smsInfo = smsInfo
            }
        }
        sendSmsStart = false
    }

    private fun sendSms(smsInfo: SmsInfo) {
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, 0, intent, 0);

        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to, null, smsInfo.text, pi, null)
    }

}