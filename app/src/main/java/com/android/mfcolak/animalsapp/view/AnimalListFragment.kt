package com.android.mfcolak.animalsapp.view
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mfcolak.animalsapp.R
import com.android.mfcolak.animalsapp.adapter.AnimalRecyclerAdapter
import com.android.mfcolak.animalsapp.databinding.FragmentAnimalListBinding
import com.android.mfcolak.animalsapp.model.Animal
import com.android.mfcolak.animalsapp.viewModel.AnimalListViewModel

class AnimalListFragment : Fragment() {

    private lateinit var binding: FragmentAnimalListBinding
    private lateinit var animalListViewModel: AnimalListViewModel
    private val animalRecyclerAdapter = AnimalRecyclerAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_animal_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animalListViewModel = ViewModelProvider(this).get(AnimalListViewModel::class.java)
        animalListViewModel.refleshData()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = animalRecyclerAdapter

        binding.swipeRefleshLayout.setOnRefreshListener {

            binding.loading.visibility = View.VISIBLE
            binding.errorMessage.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            animalListViewModel.refleshFromInternet()
            binding.swipeRefleshLayout.isRefreshing = false
        }

        observeLiveData()
        }

    fun observeLiveData(){

        animalListViewModel.animals.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.recyclerView.visibility = View.VISIBLE
                animalRecyclerAdapter.updateAnimalList(it)
            }
        })

        animalListViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {

                binding.errorMessage.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        animalListViewModel.animalLoading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.recyclerView.visibility = View.GONE
                    binding.errorMessage.visibility = View.GONE
                    binding.loading.visibility = View.VISIBLE
                }else{
                    binding.loading.visibility = View.GONE
                }
            }
        })
    }



}
