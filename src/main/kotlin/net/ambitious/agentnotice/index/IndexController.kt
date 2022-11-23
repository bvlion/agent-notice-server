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
  fun user(@AuthToken token: String): Map<String, Any> {
    return mapOf("token" to token)
  }
}