package net.ambitious.agentnotice.index

import net.ambitious.agentnotice.common.AppFirestore
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

  private val appFireStore: AppFirestore = AppFirestore(null)

  @GetMapping("/")
  fun index(
    @RequestParam("user", defaultValue = "user") user: String,
    @RequestParam("token", defaultValue = "token") token: String,
  ): Map<String, Any> {
    val param = mapOf(user to token)
    appFireStore.save(param)
    return param
  }
}