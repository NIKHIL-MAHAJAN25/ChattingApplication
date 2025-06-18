package com.nikhil.chattingapplication.navigation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.firestore
import com.nikhil.chattingapplication.R
import com.nikhil.chattingapplication.databinding.FragmentHomeBinding
import com.nikhil.chattingapplication.dataclasses.User
import com.nikhil.chattingapplication.interfaces.UserInter
import com.nikhil.chattingapplication.recyclers.Adapterr

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), UserInter {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var recadapter:Adapterr
    var userlist= arrayListOf<User>()
    var collectioname="Users"

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var binding: FragmentHomeBinding
    val db = Firebase.firestore
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
        binding=FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recadapter = Adapterr(userlist,this)
        binding.userrecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.userrecycler.adapter = recadapter
        userlist.clear()
        db.collection(collectioname).addSnapshotListener{snapshots,e->
            if (e != null) {
                Log.e("Firestore Error", e.message.toString())
                return@addSnapshotListener
            }
            for (snapshot in snapshots!!.documentChanges) {
                val userModel = convertObject(snapshot.document)

                when (snapshot.type) {
                    DocumentChange.Type.ADDED -> {
                        userModel?.let {

                            if (it.userid != currentUserId) {//for excluding current suigned in user
                                userlist.add(it)
                            }
                        }
                    }

                    DocumentChange.Type.MODIFIED -> {
                        userModel?.let {
                            val index = getIndex(userModel)
                            if (index > -1) userlist[index] = it
                        }
                    }

                    DocumentChange.Type.REMOVED -> {
                        userModel?.let {
                            val index = getIndex(userModel)
                            if (index > -1) userlist.removeAt(index)
                        }
                    }
                }
            }
            recadapter.notifyDataSetChanged()
        }

        }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun convertObject(snapshot: QueryDocumentSnapshot): User? {
        val user = snapshot.toObject(User::class.java)
        user.userid = snapshot.id
        return user
    }

    // Find user index by userId
    private fun getIndex(userModel: User): Int {
        return userlist.indexOfFirst { it.userid == userModel.userid }
    }

    override fun chatclick(user: User, position: Int) {
        val bundle=Bundle().apply{
            putString("username",user.name)
            putString("profile",user.profileimageurl)
            putString("recid",user.userid)
            putString("sendid",currentUserId)
        }
      findNavController().navigate(R.id.graphfragment,bundle)
    }
}