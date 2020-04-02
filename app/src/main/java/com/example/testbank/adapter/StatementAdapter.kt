package com.example.testbank.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testbank.R
import com.example.testbank.domain.models.Statement
import kotlinx.android.synthetic.main.statements_item.view.*

class StatementAdapter constructor() :
    RecyclerView.Adapter<StatementAdapter.ProductCellViewHolder>() {

    private val statements: MutableList<Statement> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCellViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.statements_item,
            parent,
            false
        )
        return ProductCellViewHolder(view)
    }

    override fun getItemCount(): Int {
        return statements.size
    }

    fun addStatements(statemenets: List<Statement>) {
        this.statements.addAll(statemenets)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductCellViewHolder, position: Int) {
        val product = statements[position]
        holder.bind(product)
    }

    class ProductCellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Statement) = with(itemView) {
            title.text = item.title
            date.text = item.date
            description.text = item.desc
            value.text = "R$ ${item.value}"
        }
    }
}