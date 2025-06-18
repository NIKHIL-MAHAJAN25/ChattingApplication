package com.nikhil.chattingapplication.navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.nikhil.chattingapplication.R
import com.nikhil.chattingapplication.databinding.FragmentChatBinding
import com.nikhil.chattingapplication.dataclasses.Message
import com.nikhil.chattingapplication.recyclers.MessageAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentChatBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db= Firebase.firestore
    var name:String?=null
    var link:String?=null
    var receiver:String?=null
    var sendid:String?=null
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentChatBinding.inflate(layoutInflater)
        arguments?.let {
            name=it.getString("username")
            link=it.getString("profile")
            receiver=it.getString("recid")
            sendid=it.getString("sendid")
        }
        fetchdata()
        setupmess()
        refresh()
        binding.imgbt.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.sendButton.setOnClickListener {
            val text=binding.messageEditText.text.toString().trim()
            if(text.isNotEmpty()){
                val message=Message(
                    receiverid = receiver,
                    senderid = sendid,
                    message = text,
                    timestamp = Timestamp.now()

                )
                val chatid = getChatId()
                db.collection("Chats").document(chatid).collection("Messages").add(message).addOnSuccessListener {
                    binding.messageEditText.text.clear()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(),"Failed to send text",Toast.LENGTH_SHORT).show()
                }
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun getChatId(): String {
        return if (sendid!! < receiver!!) "$sendid-$receiver" else "$receiver-$sendid"
    }
    private fun setupmess() {
        messageAdapter = MessageAdapter(messageList, sendid!!)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            adapter = messageAdapter
        }
    }

    private fun refresh() {
        val chatId = getChatId()
        db.collection("Chats")
            .document(chatId)
            .collection("Messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshots, error ->
                if (error != null || snapshots == null) {
                    Toast.makeText(requireContext(), "Error loading messages", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                messageList.clear()
                for (doc in snapshots) {
                    val msg = doc.toObject(Message::class.java)
                    messageList.add(msg)
                }
                messageAdapter.notifyDataSetChanged()
                binding.recyclerView.scrollToPosition(messageList.size - 1)
            }
    }

    private fun fetchdata(){

                    binding.userName.text=name
                    Glide.with(requireContext())
                        .load(link)
                        .into(binding.profileImage)
                }
            }
