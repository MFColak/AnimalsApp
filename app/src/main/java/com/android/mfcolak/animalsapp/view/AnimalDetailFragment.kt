package com.android.mfcolak.animalsapp.view

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
import com.android.mfcolak.animalsapp.R
import com.android.mfcolak.animalsapp.databinding.FragmentAnimalDetailBinding
import com.android.mfcolak.animalsapp.databinding.FragmentAnimalListBinding
import com.android.mfcolak.animalsapp.model.Animal
import com.android.mfcolak.animalsapp.util.createPlaceholder
import com.android.mfcolak.animalsapp.util.downloadImage
import com.android.mfcolak.animalsapp.viewModel.AnimalDetailViewModel

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
            animal?.let {

                binding.selectedAnimal = it
               /* binding.animalName.text = it.animalName
                binding.animalLocation.text = it.animalLocation
                binding.animalLifespan.text = it.animalLifeSpan
                context?.let {
                    binding.animalImage.downloadImage(animal.animalImage, createPlaceholder(it))
                }*/
            }
        })
    }


}