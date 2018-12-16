package com.example.ahmadzada.slackclone.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.ahmadzada.slackclone.Models.Message
import com.example.ahmadzada.slackclone.R
import com.example.ahmadzada.slackclone.Utilities.Utilities
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(val context: Context, val messages: ArrayList<Message>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMessage(context, messages[position])
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val messagesUserImage = itemView?.findViewById<ImageView>(R.id.messageUserImage)
        val messageUsernameText = itemView?.findViewById<TextView>(R.id.messageUsernameText)
        val messageTimestampText = itemView?.findViewById<TextView>(R.id.messageTimestampText)
        val messageMessageText = itemView?.findViewById<TextView>(R.id.messageMessageText)

        fun bindMessage(context: Context, message: Message) {
            val resourceId = context.resources.getIdentifier(message.userAvatarName, "drawable", context.packageName)
            this.messagesUserImage?.setImageResource(resourceId)
            this.messagesUserImage?.setBackgroundColor(Utilities.parsedColor(message.userAvatarColor))
            this.messageUsernameText?.text = message.username
            this.messageTimestampText?.text = this.dateString(message.timeStamp)
            this.messageMessageText?.text = message.message
        }

        fun dateString(isoString: String) : String {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")
            var convertedDate = Date()
            try {
                convertedDate = isoFormat.parse(isoString)
            } catch (e : ParseException) {
                Log.d("PARSE", "EXC: " + e.toString())
            }

            val dateFormat = SimpleDateFormat("E, hh:mm a", Locale.getDefault())
            return dateFormat.format(convertedDate)
        }
    }
}