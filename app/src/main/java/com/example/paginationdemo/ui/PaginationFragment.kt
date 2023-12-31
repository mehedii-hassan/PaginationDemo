package com.example.paginationdemo.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.paginationdemo.adapters.PaginationAdapter
import com.example.paginationdemo.databinding.FragmentPaginationBinding
import com.example.paginationdemo.models.PhotoModel
import com.example.paginationdemo.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaginationFragment : Fragment() {


    private var page = 1
    private var perPage = 25
    private val imageList = mutableListOf<PhotoModel>()
    private lateinit var adapter: PaginationAdapter
    private lateinit var binding: FragmentPaginationBinding
    private lateinit var gridLayoutManager: GridLayoutManager
    private var count = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Initialization---------------------------------------------------------------
        binding = FragmentPaginationBinding.inflate(inflater, container, false)
        gridLayoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        binding.progressBarTwo.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        getData(page, perPage)

        adapter = PaginationAdapter(imageList)
        binding.rvPaginationTwo.layoutManager = gridLayoutManager
        binding.rvPaginationTwo.adapter = adapter

        binding.nestedScrollViewId.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == binding.nestedScrollViewId
                    .getChildAt(0).measuredHeight - binding.nestedScrollViewId.measuredHeight
            ) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                page++
                binding.progressBarTwo.visibility = View.VISIBLE
                getData(page, perPage)
            }
        }

    }

    private fun getData(page: Int, perPage: Int) {

        ApiClient.getService().getImageListResponse(page, perPage)
            .enqueue(object : Callback<List<PhotoModel>> {
                override fun onResponse(
                    call: Call<List<PhotoModel>>,
                    response: Response<List<PhotoModel>>
                ) {

                    binding.progressBarTwo.visibility = View.VISIBLE
                    if (response.body() != null) {
                        imageList.addAll(response.body()!!)
                        adapter.submitNewImageList(imageList)
                        binding.progressBarTwo.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "size ${imageList.size}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Log.e("TAG", "size= ${imageList.size} count =${count}")
                }

                override fun onFailure(call: Call<List<PhotoModel>>, t: Throwable) {
                    Log.e("TAG", "failed ${t.localizedMessage}")
                }
            })

    }

}