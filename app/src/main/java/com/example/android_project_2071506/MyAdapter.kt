package com.example.android_project_2071506

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



data class Item(val remain: String, val price: Int, val title: String, val details: String, val email: String, val docID: String, val UserID: String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc["remain"].toString(), doc["price"].toString().toIntOrNull() ?: 0,
            doc["title"].toString(), doc["details"].toString(), doc["email"].toString(), doc["docID"].toString(), doc["UserID"].toString())
}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class MyAdapter(private val context: Context, private var items: List<Item>)
    : RecyclerView.Adapter<MyViewHolder>() {

    fun interface OnItemClickListener {
        fun onItemClick(student_id: String)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }



    fun updateList(newList: List<Item>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.items, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val db: FirebaseFirestore = Firebase.firestore
        val itemsCollectionRef = db.collection("items")
        val usersCollectionRef = db.collection("users")
        lateinit var currentUserEmail: String

        val item = items[position]
        val modiPrice = "${item.price.toString()}원"
        holder.view.findViewById<TextView>(R.id.remainText).text = item.remain
        holder.view.findViewById<TextView>(R.id.priceText).text = modiPrice
        holder.view.findViewById<TextView>(R.id.titleText).text = item.title



        holder.view.setOnClickListener {
            println(item)


            val ClickedEmail = item.email

            usersCollectionRef.document(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener {
                currentUserEmail = it["email"].toString()


                if(currentUserEmail == ClickedEmail){
                    val intent = Intent(holder.view.context, MydetailActivity::class.java)
                    intent.putExtra("price", item.price)
                    intent.putExtra("remain", item.remain)
                    intent.putExtra("docID", item.docID)

                    ContextCompat.startActivity(holder.view.context, intent, null)

                    (holder.view.context as? ListActivity)?.finish()

                }else{
                    val intent = Intent(holder.view.context, OtherdetailActivity::class.java)

                    intent.putExtra("otherEmail", item.email)
                    intent.putExtra("otherPrice", item.price)
                    intent.putExtra("otherTitle", item.title)
                    intent.putExtra("otherDetails", item.details)
                    intent.putExtra("otherRemain", item.remain)
                    intent.putExtra("otherdocID", item.docID)
                    intent.putExtra("otherUserID", item.UserID)



                    ContextCompat.startActivity(holder.view.context, intent, null)

                    (holder.view.context as? ListActivity)?.finish()
                }

            }


        }



    }

    override fun getItemCount() = items.size
}
