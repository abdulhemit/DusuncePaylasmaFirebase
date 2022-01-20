package com.okuuyghur.dusuncepaylasmafirebase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.okuuyghur.dusuncepaylasmafirebase.databinding.DusunceRowBinding
import com.okuuyghur.dusuncepaylasmafirebase.model.Paylasim
import com.squareup.picasso.Picasso

class DusunceAdapter(val paylasimlar : ArrayList<Paylasim>): RecyclerView.Adapter<DusunceAdapter.Dusunce_VH>() {
    inner class Dusunce_VH(val binding_row : DusunceRowBinding):RecyclerView.ViewHolder(binding_row.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Dusunce_VH {
        val inflater = DusunceRowBinding.inflate(LayoutInflater.from(parent.context))
         return Dusunce_VH(inflater)
    }

    override fun onBindViewHolder(holder: Dusunce_VH, position: Int) {
        holder.binding_row.kullaniciAdiId.setText(paylasimlar.get(position).kullaniciAdi)
        holder.binding_row.paylasimId.setText(paylasimlar.get(position).paylasilanYorumlar)
        Picasso.get().load(paylasimlar.get(position).gorselURL).into(holder.binding_row.imageView)
    }

    override fun getItemCount(): Int {
        return paylasimlar.size
    }
}