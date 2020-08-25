package com.duxetech.vinayagarmantras
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout.view.*

class ExampleAdapter(
    private val exampleList: Array<String>,
    private val listener: OnItemClickListener
) :

    RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,
            parent, false)
        return ExampleViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.textView1.text = currentItem
        holder.iv.setImageResource(ganeshImages.random())
    }
    override fun getItemCount() = exampleList.size
    inner class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val textView1: TextView = itemView.chapterTitle
        val iv = itemView.titleImage
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)


    }

}