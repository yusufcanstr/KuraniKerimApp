package com.yusufcansenturk.ux_1_kuran_kerim_app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufcansenturk.ux_1_kuran_kerim_app.R
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerList.Data
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.details.DetailsActivity
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home.OnLikeButtonClickListener
import kotlinx.android.synthetic.main.item_sure_name.view.*


class HomeAdapter(val sureList: ArrayList<Data> , private var onLikeButtonClickListener: OnLikeButtonClickListener?)
    : RecyclerView.Adapter<HomeAdapter.HomeHolder>() {


    class HomeHolder(var view: View) : RecyclerView.ViewHolder(view.rootView)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_sure_name, parent, false)
        return HomeHolder(view)
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        holder.view.txt_item_sure_name.text = "${sureList[position].name} Suresi"
        val img1 = holder.view.img_like_btn1
        val img2 = holder.view.img_like_btn2
        holder.view.txt_item_sure_name.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("sure_id", sureList[position].id.toString())
            intent.putExtra("name", "${sureList[position].name} Suresi")
            holder.itemView.context.startActivities(arrayOf(intent))
        }

        holder.view.img_like_btn1.setOnClickListener {
            onLikeButtonClickListener?.onLikeButtonClicked(position, sureList, img1, img2)
        }

    }

    override fun getItemCount(): Int {
        return sureList.size
    }

    fun updateNameList(newCountryList: List<Data>) {
        sureList.clear()
        sureList.addAll(newCountryList)
        notifyDataSetChanged()
    }
}