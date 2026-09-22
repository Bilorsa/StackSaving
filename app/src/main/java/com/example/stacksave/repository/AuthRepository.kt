package com.example.stacksave.repository
import android.content.Context
import com.example.stacksave.util.FirebaseManager
import kotlinx.coroutines.tasks.await
class AuthRepository(private val context: Context) {
    private val auth get() = FirebaseManager.auth(context)
    fun isFirebaseConfigured() = FirebaseManager.isConfigured(context)
    fun currentUid() = auth?.currentUser?.uid
    suspend fun login(email: String, password: String): Result<String> = try {
        val a = auth
        if (a != null) Result.success(a.signInWithEmailAndPassword(email, password).await().user?.uid ?: "")
        else Result.success("local-demo-user")
    } catch (e: Exception) { Result.failure(e) }
    suspend fun register(email: String, password: String, displayName: String): Result<String> = try {
        val a = auth
        if (a != null) Result.success(a.createUserWithEmailAndPassword(email, password).await().user?.uid ?: "")
        else Result.success("local-demo-user")
    } catch (e: Exception) { Result.failure(e) }
    fun signOut() { auth?.signOut() }
}
