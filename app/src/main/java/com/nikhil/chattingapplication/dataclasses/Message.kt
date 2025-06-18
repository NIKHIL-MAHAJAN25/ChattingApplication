package com.nikhil.chattingapplication.dataclasses

import com.google.firebase.Timestamp


data class Message(
    val senderid:String?=null,
    val receiverid:String?=null,
    val message:String?=null,
   val timestamp: Timestamp =Timestamp.now()
           //maps keyAIzaSyAraKJmMrqWcLd63Yr7JtVmUZADu_H1z_M
//AIzaSyB43pGEFMJDEJdmwSovmLnaWW-9a54JmhY
)