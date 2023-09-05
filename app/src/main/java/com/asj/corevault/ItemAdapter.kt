package com.asj.corevault

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val itemList:ArrayList<ItemDataClass>):RecyclerView.Adapter<ItemAdapter.ItemViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemViewHolder
    {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.item_row,parent,false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int)
    {
        val currentItem=itemList[position]
        holder.name.text=currentItem.itemname
        holder.uname.text=currentItem.username
        holder.pass.text=currentItem.password
    }

    override fun getItemCount(): Int
    {
        return itemList.size
    }
    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val name:TextView=itemView.findViewById(R.id.tvName)
        val uname:TextView=itemView.findViewById(R.id.tvUname)
        val pass:TextView=itemView.findViewById(R.id.tvPwd)
    }
}