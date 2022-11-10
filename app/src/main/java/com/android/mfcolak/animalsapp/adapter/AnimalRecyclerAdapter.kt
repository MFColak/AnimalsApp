package com.android.mfcolak.animalsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.android.mfcolak.animalsapp.R
import com.android.mfcolak.animalsapp.databinding.AnimalRecyclerRowBinding
import com.android.mfcolak.animalsapp.model.Animal
import com.android.mfcolak.animalsapp.view.AnimalListFragmentDirections
import kotlinx.android.synthetic.main.animal_recycler_row.view.*

class AnimalRecyclerAdapter(private val animalList: ArrayList<Animal>): RecyclerView.Adapter<AnimalRecyclerAdapter.AnimalViewHolder>(),
    AnimalClickListener {

    class AnimalViewHolder(var binding: AnimalRecyclerRowBinding): RecyclerView.ViewHolder(binding.root){}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<AnimalRecyclerRowBinding>(inflater, R.layout.animal_recycler_row, parent, false)
        return AnimalViewHolder(binding)

    }


    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {

        holder.binding.animal = animalList[position]
        holder.binding.listener = this
    /*
        holder.binding.name.text = animalList[position].animalName
        holder.binding.location.text = animalList[position].animalLocation
        holder.binding.lifespan.text = animalList[position].animalLifeSpan

        holder.binding.imageView.downloadImage(animalList[position].animalImage, createPlaceholder(holder.itemView.context))

        holder.itemView.setOnClickListener {
           val action = AnimalListFragmentDirections.actionAnimalListFragmentToAnimalDetailFragment()
            action.animalId = animalList[position].uuid
            Navigation.findNavController(it).navigate(action)
        }*/
    }

    override fun getItemCount(): Int {
        return animalList.size
    }

    fun updateAnimalList(newList: List<Animal>){
        animalList.clear()
        animalList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onAnimalClicked(v: View) {
        val action = AnimalListFragmentDirections.actionAnimalListFragmentToAnimalDetailFragment()
        action.animalId = v.AnimalId.text.toString().toInt()
        Navigation.findNavController(v).navigate(action)
    }


}