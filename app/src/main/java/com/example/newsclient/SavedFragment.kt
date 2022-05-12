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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsclient.databinding.FragmentSavedBinding
import com.example.newsclient.presentation.adapter.NewsRecyclerViewAdapter
import com.example.newsclient.presentation.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedFragment : Fragment() {

    private var binding: FragmentSavedBinding? = null
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var newsAdapter: NewsRecyclerViewAdapter
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
        newsAdapter = NewsRecyclerViewAdapter()

        lifecycleScope.launchWhenStarted {

            binding?.rvSaved?.apply {
                newsViewModel.getSavedNews().observe(viewLifecycleOwner, Observer {
                    newsAdapter.differ.submitList(it.toList())

                    adapter = newsAdapter

                        newsAdapter.setOnItemClickListener {
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

        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                newsViewModel.deleteArticle(article)
                Snackbar.make(view,"Deleted Successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        newsViewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(binding?.rvSaved)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SavedFragment()
    }

}