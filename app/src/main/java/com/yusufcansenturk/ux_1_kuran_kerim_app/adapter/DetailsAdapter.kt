package com.yusufcansenturk.ux_1_kuran_kerim_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufcansenturk.ux_1_kuran_kerim_app.R
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.ItemSureDetailBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerDetails.Verse
import kotlinx.android.synthetic.main.item_sure_detail.view.*

class DetailsAdapter(private val verseList: List<Verse>) : RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder>() {

    inner class DetailsViewHolder(private val binding: ItemSureDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(verse: Verse) {
            binding.apply {
                itemSureDetailOrgin.text = verse.verse
                itemDetailOkunusu.text = verse.transcription
                itemDetailAnlami.text = verse.translation!!.text
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val binding = ItemSureDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(verseList[position])
    }

    override fun getItemCount() = verseList.size
}


