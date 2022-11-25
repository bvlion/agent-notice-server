package net.ambitious.agentnotice.index

import net.ambitious.agentnotice.data.AppFirestore
import net.ambitious.agentnotice.request.AuthToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController(private val appFireStore: AppFirestore) {

  @GetMapping("/")
  fun index(
    @RequestParam("title") title: String,
  ): Map<String, Any> {
    val token = appFireStore.createToken(title)
    return mapOf("token" to token)
  }

  @GetMapping("/test")
  fun user(
    @AuthToken token: String,
    @RequestParam("user") user: String,
    @RequestParam("fcm") fcm: String,
  ): Map<String, Any> {
    val time = appFireStore.addUser(token, user, fcm)
    return mapOf("save" to "success ${time.toDate()}")
  }
}