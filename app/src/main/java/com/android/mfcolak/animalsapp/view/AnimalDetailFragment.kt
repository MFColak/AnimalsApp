package com.android.mfcolak.animalsapp.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.palette.graphics.Palette
import com.android.mfcolak.animalsapp.R
import com.android.mfcolak.animalsapp.databinding.FragmentAnimalDetailBinding
import com.android.mfcolak.animalsapp.databinding.FragmentAnimalListBinding
import com.android.mfcolak.animalsapp.model.Animal
import com.android.mfcolak.animalsapp.model.AnimalPalette
import com.android.mfcolak.animalsapp.util.createPlaceholder
import com.android.mfcolak.animalsapp.util.downloadImage
import com.android.mfcolak.animalsapp.util.loadImage
import com.android.mfcolak.animalsapp.viewModel.AnimalDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class AnimalDetailFragment : Fragment() {

    private lateinit var binding: FragmentAnimalDetailBinding
    private lateinit var viewModel: AnimalDetailViewModel

    private var animalId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =DataBindingUtil.inflate(inflater, R.layout.fragment_animal_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            animalId = AnimalDetailFragmentArgs.fromBundle(it).animalId
        }
        viewModel = ViewModelProvider(this)[AnimalDetailViewModel::class.java]
        viewModel.checkRoomData(animalId)

        observeLiveData()

    }

    private fun observeLiveData() {
        viewModel.animalLiveData.observe(viewLifecycleOwner, Observer {animal->
            animal?.let { it ->

                binding.selectedAnimal = it
                setupBackgroundColor(animal.animalImage)

               /* binding.animalName.text = it.animalName
                binding.animalLocation.text = it.animalLocation
                binding.animalLifespan.text = it.animalLifeSpan
                context?.let {
                    binding.animalImage.downloadImage(animal.animalImage, createPlaceholder(it))
                }*/
            }
        })
    }

    private fun setupBackgroundColor(url: String){
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





}