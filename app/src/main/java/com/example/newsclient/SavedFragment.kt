package com.example.newsclient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.newsclient.databinding.FragmentSavedBinding
import com.example.newsclient.presentation.adapter.NewsRecyclerViewAdapter
import com.example.newsclient.presentation.viewmodel.NewsViewModel

class SavedFragment : Fragment() {

    private var binding: FragmentSavedBinding? = null
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBinding.inflate(layoutInflater)
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manager = GridLayoutManager(this.context, 1)

        lifecycleScope.launchWhenStarted {

            binding?.rvSaved?.apply {
                newsViewModel.getSavedNews().observe(viewLifecycleOwner, Observer {
                    val adapter2 = NewsRecyclerViewAdapter()
                    adapter2.differ.submitList(it.toList())

                    adapter = adapter2

                        adapter2.setOnItemClickListener {
                            val bundle = Bundle().apply {
                                putSerializable("selected_article",it)
                            }
                            findNavController().navigate(
                                R.id.action_savedFragment_to_infoFragment,
                                bundle
                            )
                        }

                    layoutManager = manager
                })

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SavedFragment()
    }

}