package com.yusufcansenturk.ux_1_kuran_kerim_app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufcansenturk.ux_1_kuran_kerim_app.R
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.favorite.Favorite
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.details.DetailsActivity
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.favorite.OnFavoriteClickListener
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home.OnLikeButtonClickListener
import kotlinx.android.synthetic.main.item_sure_name.view.*

class FavoriteAdapter(var favoriteList: ArrayList<Favorite>, private var onFavoriteClickListener: OnFavoriteClickListener)
    : RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {


    class FavoriteHolder(var view : View) : RecyclerView.ViewHolder(view.rootView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_sure_name, parent, false)
        return FavoriteHolder(view)
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.view.txt_item_sure_name.text = "${favoriteList[position].name} Suresi"
        holder.view.txt_item_sure_name.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("sure_id", favoriteList[position].id)
            intent.putExtra("name", "${favoriteList[position].name} Suresi")
            holder.itemView.context.startActivities(arrayOf(intent))
        }
        holder.view.img_like_btn1.visibility = View.GONE
        holder.view.img_like_btn2.visibility = View.VISIBLE
        holder.view.img_like_btn2.setOnClickListener {
            onFavoriteClickListener.onFavoriteClickListener(position, favoriteList)
        }

    }

    fun updateNameList(newCountryList: List<Favorite>) {
        favoriteList.clear()
        favoriteList.addAll(newCountryList)
        notifyDataSetChanged()
    }

}