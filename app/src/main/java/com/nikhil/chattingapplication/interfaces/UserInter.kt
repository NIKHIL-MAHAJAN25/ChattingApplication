package com.nikhil.chattingapplication.interfaces

import android.icu.text.Transliterator.Position
import com.nikhil.chattingapplication.dataclasses.User

interface UserInter {
    fun chatclick(user: User,position: Int)
}