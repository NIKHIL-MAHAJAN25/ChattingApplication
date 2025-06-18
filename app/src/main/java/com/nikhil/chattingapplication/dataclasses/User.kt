package com.nikhil.chattingapplication.dataclasses

data class User (
    var userid:String?=null,
    val name:String?=null,
    val age:Int?=null,
    val gender:Int?=null,
    val devicetoken:String?=null,
    val profileimageurl: String?="https://wicgbqzoazarxzlnwsme.supabase.co/storage/v1/object/sign/sample/uploads/1749203718614.jpg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV80NDIzMTRhYS1iMjIxLTQ0YjEtYjVhNC1lODBlMDA4M2M5Y2IiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJzYW1wbGUvdXBsb2Fkcy8xNzQ5MjAzNzE4NjE0LmpwZyIsImlhdCI6MTc0OTIxNTQ3NiwiZXhwIjoxNzgwNzUxNDc2fQ.wvDrBz-_FMerOPU6Sa1sxjmF8_MUWpDxM__RbNurq5M"
)