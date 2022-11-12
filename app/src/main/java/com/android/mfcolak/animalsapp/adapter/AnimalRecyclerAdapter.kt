package com.android.mfcolak.animalsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.android.mfcolak.animalsapp.R
import com.android.mfcolak.animalsapp.databinding.AnimalRecyclerRowBinding
import com.android.mfcolak.animalsapp.model.Animal
import com.android.mfcolak.animalsapp.util.createPlaceholder
import com.android.mfcolak.animalsapp.util.downloadImage
import com.android.mfcolak.animalsapp.view.AnimalListFragmentDirections

class AnimalRecyclerAdapter(
    private val animalList: ArrayList<Animal>
) :
    RecyclerView.Adapter<AnimalRecyclerAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(
        private val binding: AnimalRecyclerRowBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(animal: Animal) {
            binding.name.text = animal.animalName
            binding.location.text = animal.animalLocation
            binding.lifespan.text = animal.animalLifeSpan
            binding.imageView.downloadImage(animal.animalImage, createPlaceholder(itemView.context))

            binding.animalItem.setOnClickListener {

                val action =
                    AnimalListFragmentDirections.actionAnimalListFragmentToAnimalDetailFragment()
                action.animalId = animal.uuid
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {

        val binding = DataBindingUtil.inflate<AnimalRecyclerRowBinding>(
            LayoutInflater.from(parent.context), R.layout.animal_recycler_row, parent, false
        )
        return AnimalViewHolder(binding)

    }


    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(animalList[position])
    }

    override fun getItemCount(): Int {
        return animalList.size
    }

    fun updateAnimalList(newList: List<Animal>) {
        animalList.clear()
        animalList.addAll(newList)
        notifyDataSetChanged()
    }
}