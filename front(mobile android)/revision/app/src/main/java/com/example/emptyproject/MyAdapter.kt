package com.example.emptyproject

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val offreData: List<Offre>, val listener: ItemClickListener) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var selectedItemPosition: Int = -1

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var offreId: TextView = view.findViewById(R.id.offreId)
        var offreIntutile: TextView = view.findViewById(R.id.offreIntutile)
        var offreSpecialiter: TextView = view.findViewById(R.id.offreSpecialiter)

        fun bind(item: Offre, listener: ItemClickListener) {
            if (item != null) {
                offreId.text = item.id.toString()
                offreIntutile.text = item.intutile
                offreSpecialiter.text = item.specialiter
            }

            itemView.setOnClickListener { v ->
                listener.onitemClicked(
                    v,
                    item
                )
            }
            if (adapterPosition == selectedItemPosition) {
                itemView.setBackgroundColor(Color.CYAN) // Changez la couleur ici
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT) // Réinitialisez la couleur ici
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ligne, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return offreData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = offreData[position]
        holder.bind(item, listener)
    }
}
