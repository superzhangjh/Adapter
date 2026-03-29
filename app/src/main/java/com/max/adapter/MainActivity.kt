package com.max.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.max.adapter.databinding.ActivityMainBinding
import com.max.adapter.databinding.ItemHoverBinding
import com.max.adapter.databinding.ItemTest1Binding
import com.max.adapter.databinding.ItemTest2Binding
import com.max.adapter.databinding.ItemTest3Binding
import com.max.adapter.databinding.ItemTest4Binding
import com.max.adapter.databinding.ItemTest5Binding
import com.max.adapter.databinding.ItemTestBinding
import com.max.adapter.toolbar.controller.sort.SortController
import com.max.adapter.toolbar.controller.sort.A2ZSortStrategy
import com.max.adapter.toolbar.controller.sort.Z2ASortStrategy
import com.max.adapter.toolbar.listener.IDataSourceListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        binding.rv.linear().mutableItems {
//            addItem<ItemTestBinding> {
//                onCreateViewBinding { ItemTestBinding.inflate(it, binding.rv, false) }
//                onBind {
//                    binding.tv.text = "Title"
//                }
//            }
//            addItem<ItemTest1Binding> {
//                onCreateViewBinding { ItemTest1Binding.inflate(it, binding.rv, false) }
//            }
//            addItem<ItemTest2Binding> {
//                onCreateViewBinding { ItemTest2Binding.inflate(it, binding.rv, false) }
//            }
//            addItem<ItemHoverBinding> {
//                onCreateViewBinding { ItemHoverBinding.inflate(it, binding.rv, false) }
//            }
//            addItem<ItemTest3Binding> {
//                onCreateViewBinding { ItemTest3Binding.inflate(it, binding.rv, false) }
//            }
//            addItem<ItemTest4Binding> {
//                onCreateViewBinding { ItemTest4Binding.inflate(it, binding.rv, false) }
//            }
//            addItem<ItemTest5Binding> {
//                onCreateViewBinding { ItemTest5Binding.inflate(it, binding.rv, false) }
//            }
//            addItem<ItemTest1Binding> {
//                onCreateViewBinding { ItemTest1Binding.inflate(it, binding.rv, false) }
//            }
//            addItem<ItemTest2Binding> {
//                onCreateViewBinding { ItemTest2Binding.inflate(it, binding.rv, false) }
//            }
//            addItem<ItemTest3Binding> {
//                onCreateViewBinding { ItemTest3Binding.inflate(it, binding.rv, false) }
//            }
//            addItem<ItemTest4Binding> {
//                onCreateViewBinding { ItemTest4Binding.inflate(it, binding.rv, false) }
//            }
//            addItem<ItemTest5Binding> {
//                onCreateViewBinding { ItemTest5Binding.inflate(it, binding.rv, false) }
//            }
//        }
//        binding.rv.enableSticky { position -> position == 3 }

        val adapter = TestAdapter()
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter
        adapter.data = listOf(
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z"
        )

        binding.dataToolbar.setOptions(listOf(
            SortController(listOf(
                A2ZSortStrategy(),
                Z2ASortStrategy()
            ))
        ))
        binding.dataToolbar.onDataSourceListener = object : IDataSourceListener<String> {
            override fun onProvideDataSource(): List<String> {
                return adapter.data
            }

            override fun onDataSourceChanged(dataSource: List<String>) {
                adapter.data = dataSource
            }
        }
    }
}

class TestAdapter : RecyclerView.Adapter<TestAdapter.VH>() {

    var data: List<String> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): VH {
        return VH(ItemTestBinding.inflate(LayoutInflater.from(p0.context), p0, false))
    }

    override fun onBindViewHolder(p0: VH, p1: Int) {
        p0.binding.tv.text = data[p1]
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class VH(val binding: ItemTestBinding) : RecyclerView.ViewHolder(binding.root)
}
