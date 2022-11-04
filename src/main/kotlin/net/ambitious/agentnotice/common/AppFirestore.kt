package net.ambitious.agentnotice.common

import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.firestore.SetOptions
import com.google.cloud.firestore.WriteResult
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import java.io.ByteArrayInputStream

class AppFirestore(private val credentials: ByteArray?) {

  private var firestore: Firestore = if (credentials == null) {
    FirestoreOptions
      .newBuilder()
      .setProjectId("test")
      .setEmulatorHost("localhost:8081")
      .build()
      .service
  } else {
    val options = FirebaseOptions.builder()
      .setCredentials(GoogleCredentials.fromStream(ByteArrayInputStream(credentials)))
      .build()
    FirebaseApp.initializeApp(options)
    FirestoreClient.getFirestore()
  }

  private fun getDocument(document: String = "y2MVid9IBrJo5AdHfO4i"): DocumentReference =
    firestore.collection("apps").document(document)

  fun save(map: Map<String, Any>) {
    ApiFutures.addCallback(
      getDocument().set(map, SetOptions.merge()),
      object : ApiFutureCallback<WriteResult> {

        override fun onFailure(t: Throwable?) {
        }

        override fun onSuccess(result: WriteResult?) {
        }
      }
    ) {
      it.run()
    }
  }
}