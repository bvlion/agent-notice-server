package net.ambitious.agentnotice.data

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.Timestamp
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.firestore.SetOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class AppFirestore(appParams: AppParams) {

  private val firestore: Firestore = if (appParams.firestoreAdminSdk.isEmpty()) {
    FirestoreOptions
      .newBuilder()
      .setProjectId("test")
      .setEmulatorHost("localhost:8081")
      .build()
      .service
  } else {
    val credential = Base64.getDecoder().decode(appParams.firestoreAdminSdk)
    val options = FirebaseOptions.builder()
      .setCredentials(GoogleCredentials.fromStream(ByteArrayInputStream(credential)))
      .build()
    FirebaseApp.initializeApp(options)
    FirestoreClient.getFirestore()
  }

  fun createToken(title: String): String {
    val token = MessageDigest.getInstance("SHA-256")
      .digest(Date().toString().toByteArray())
      .joinToString(separator = "") {
        "%02x".format(it)
      }
    save(token, mapOf("00_title" to title))
    return token
  }

  fun addUser(token: String, userName: String, fcm: String): Timestamp =
    save(token, mapOf(userName to fcm))

  private fun get(token: String): Map<String, Any>? =
    getDocument(token).get().get().data

  private fun save(token: String, map: Map<String, Any>): Timestamp =
    try {
      getDocument(token).set(map, SetOptions.merge()).get(1000, TimeUnit.MILLISECONDS).updateTime
    } catch (e: Exception) {
      throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
    }

  private fun getDocument(document: String): DocumentReference =
    firestore.collection("apps").document(document)
}