package net.ambitious.agentnotice.data

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app")
class AppParams {
  lateinit var firestoreAdminSdk: String
}