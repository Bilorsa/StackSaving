package com.example.stacksave.util
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
object FirebaseManager {
    fun isConfigured(context: Context) = FirebaseApp.getApps(context).isNotEmpty()
    fun auth(context: Context): FirebaseAuth? = if (isConfigured(context)) FirebaseAuth.getInstance() else null
    fun firestore(context: Context): FirebaseFirestore? = if (isConfigured(context)) FirebaseFirestore.getInstance() else null
}
