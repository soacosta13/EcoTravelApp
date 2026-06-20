package com.example.ecotravel.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.example.ecotravel.adapter.TravelAdapter
import com.example.ecotravel.databinding.ActivityHistoryBinding
import com.example.ecotravel.model.TravelRecord
class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    /**
     * setea el recycler view
     */
    private fun setupRecyclerView() {
        @Suppress("UNCHECKED_CAST")
        val travels = intent.getSerializableExtra("travels") as? ArrayList<TravelRecord>
            ?: arrayListOf()

        var adapter: TravelAdapter? = null
        adapter = TravelAdapter(
            onLongClick = { record ->
                travels.remove(record)
                adapter?.submitList(travels.toList())
                updateEmptyState(travels)
            }
        )

        binding.rvHistory.adapter = adapter
        adapter.submitList(travels.toList())
        updateEmptyState(travels)
    }

    /**
     * mensaje vacio si no hay viajes
     */
    private fun updateEmptyState(travels: ArrayList<TravelRecord>) {
        if (travels.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
            binding.rvHistory.visibility = View.GONE
        } else {
            binding.tvEmpty.visibility = View.GONE
            binding.rvHistory.visibility = View.VISIBLE
        }
    }


}