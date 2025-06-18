package com.nikhil.chattingapplication.supabase

import android.app.Application
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage

class Myapp:Application(){
    lateinit var supabaseClient: SupabaseClient
    override fun onCreate() {
        super.onCreate()
        supabaseClient= createSupabaseClient(
            "https://wicgbqzoazarxzlnwsme.supabase.co",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndpY2dicXpvYXphcnh6bG53c21lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDc4MjgwMzIsImV4cCI6MjA2MzQwNDAzMn0.-n-d39F4HKsYEs_L-WWb84SCONiNoQw1OpXtoo5NRws"
        ){
            install(Storage)
        }
        // Now you can access Supabase Auth and Storage through the `supabaseClient`
//        val auth = supabaseClient.auth
        val bucket = supabaseClient.storage.from("sample")
    }
}