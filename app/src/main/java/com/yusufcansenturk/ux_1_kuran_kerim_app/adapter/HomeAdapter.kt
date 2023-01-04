package com.yusufcansenturk.ux_1_kuran_kerim_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.yusufcansenturk.ux_1_kuran_kerim_app.R
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerList.Data
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home.HomeFragmentDirections
import kotlinx.android.synthetic.main.item_sure_name.view.*

class HomeAdapter(val sureList: ArrayList<Data>): RecyclerView.Adapter<HomeAdapter.HomeHolder>() {

    class HomeHolder(var view: View) : RecyclerView.ViewHolder(view.rootView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_sure_name, parent, false)
        return HomeHolder(view)
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        holder.view.txt_item_sure_name.text = "${sureList[position].name} Suresi"
        holder.view.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return sureList.size
    }

    fun updateCountryList(newCountryList: List<Data>){
        sureList.clear()
        sureList.addAll(newCountryList)
        notifyDataSetChanged()
    }

}
